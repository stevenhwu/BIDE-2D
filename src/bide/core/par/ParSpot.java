/*******************************************************************************
 * ParSpot.java
 * 
 * This file is part of BIDE-2D
 * 
 * Copyright (C) 2012 Steven Wu
 * 
 * BIDE-2D is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BIDE-2D is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BIDE-2D.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package bide.core.par;

import java.util.ArrayList;

import org.apache.commons.math3.random.RandomDataImpl;

import bide.core.Likelihood;
import bide.core.MHRatio;
import bide.math.Constant;
import bide.math.NormalDistribution;
import bide.math.Transformation;
import bide.math.TwoExpDistribution;

public class ParSpot implements Parameter {
	

	public static final String[] SPOT_LABELS = { "Ite", "mu", "d", "pi", "rho",
			"likleihood" };
	public static final String[] SPOT_LABELS_SUM = { "Ite", "d", "rho",
			"likleihood" };

//	public static final int INDEX_MU = 0;
//	public static final int INDEX_PROB = 2;


	private static RandomDataImpl r = new RandomDataImpl();
	private double mu1;
	private double d;
	private double mu2;
	private double pi;
	private double rho;
	private double sd;
	private double prob1;
	private double prob2;

	private int spotIndex;

	private double tuneSd = 1;
	private double temperature = 1;

	private Spot thisSpot;
	private double spotPosterLi;


	public ParSpot(double limDet, Spot s, ParGlobal gp, Likelihood li, int index) {
		// limit = limDet;
		this.thisSpot = s;
		this.spotIndex = index;
		reset(limDet);
		setupLikeli(gp, li);
	}

	public ParSpot(double mu1, double d, double pi, double rho, double sd) {

		this.mu1 = mu1;
		this.d = d;
		// this.mu2 = mu1 + d;
		this.pi = pi;
		this.rho = rho;
		this.sd = sd;
		reCalcProb();
	}

	public static ParSpot[] init(double limDet, ArrayList<Spot> allSpots,
			ParGlobal gp, Likelihood li) {

		int n = allSpots.size();
		ParSpot[] allSp = new ParSpot[n];

		for (int i = 0; i < allSp.length; i++) {
			allSp[i] = new ParSpot(limDet, allSpots.get(i), gp, li, i);

		}

		return allSp;
	}

	public void setupLikeli(ParGlobal gp, Likelihood li) {
		setSd(gp);

		double tmpLikeli = li.calLogLikeli(this);
		li.setEachLikelihood(spotIndex, tmpLikeli);
		spotPosterLi = tmpLikeli + calculatePrior(gp);

	}

	/**
	 * For testing only
	 */
	public static ParSpot[] init(int n, double limDet) {

		ParSpot[] allSp = new ParSpot[n];
		for (int i = 0; i < allSp.length; i++) {
			allSp[i] = new ParSpot(limDet);

		}
		return allSp;
	}

	private ParSpot(double limDet) {
		reset(limDet);
	}

	public void reset(double limDet) {

		// mu1 = r.nextUniform(limDet, Constant.GEL_MAX);
		mu1 = r.nextUniform(limDet / 4, 0);
		d = r.nextUniform(-1, 1);
		pi = r.nextUniform(Constant.MIN_INIT, Constant.MAX_INIT);
		rho = r.nextUniform(Constant.MIN_INIT, Constant.MAX_INIT);
		sd = r.nextUniform(Constant.MIN_SD, Constant.MAX_INIT);
		mu2 = getMu2();
		// d = mu2 - mu1;
		reCalcProb();

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		return (sb.append("Mu1:\t").append(mu1).append("\tMu2:\t").append(
				getMu2()).append("\tProb1:\t").append(prob1).append(
				"\tProb2:\t").append(prob2).append("\tsd:\t").append(sd)
				.append("\n").append(thisSpot.toString()).toString());
	}

	public void updateLocalPar(ParGlobal gp, Likelihood li, double[] eachTune) {

		setSd(gp);
		updateMuD(gp, li, eachTune[0]);
		updatePiRho(gp, li, eachTune[1]);

	}

	public void updateMuD(ParGlobal gp, Likelihood li, double tune) {

		double limDet = gp.getLimDet();

		double newMu = NormalDistribution.randomDist(mu1, tune);
		double newD = NormalDistribution.randomDist(d, tune * tuneSd);
		double temp = newMu + newD;

		while (temp > Constant.GEL_MAX || temp < limDet
				|| newMu > Constant.GEL_MAX || newMu < limDet) {

			newMu = NormalDistribution.randomDist(mu1, tune);
			newD = NormalDistribution.randomDist(d, tune * tuneSd);
			temp = newMu + newD;
		}

		double newXGivenX = NormalDistribution.logPdfBT(newMu, mu1, tune,
				limDet, Constant.GEL_MAX)
				+ NormalDistribution.logPdfBT(newD, d, tune * tuneSd, (limDet - mu1),
						(Constant.GEL_MAX - mu1));

		double xGivenNewX = NormalDistribution.logPdfBT(mu1, newMu, tune,
				limDet, Constant.GEL_MAX)
				+ NormalDistribution.logPdfBT(d, newD, tune * tuneSd, (limDet - mu1),
						(Constant.GEL_MAX - mu1));

		double newLikeli = li.calLogLikeli(thisSpot, tempParMuDLikeli(newMu, newD));
		double newPrior = calculatePrior(tempParMuDPrior(newMu, newD), gp);
		double newPost = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				spotPosterLi, newPost, temperature);

		if (accept) {

			mu1 = newMu;
			d = newD;
			li.setEachLikelihood(spotIndex, newLikeli);// = newLikeli;
			spotPosterLi = newPost;

		}
	}

	public double[] tempParMuDLikeli(double newMu, double newD) {

		return new double[] { newMu, getProb1(), sd, newMu + newD, getProb2(),
				sd };

	}

	public double[] tempParMuDPrior(double newMu, double newD) {

		return new double[] { newMu, newD, pi, rho };

	}

	public void updatePiRho(ParGlobal gp, Likelihood li, double tune) {

		double newPi = NormalDistribution.randomDist(pi, tune);
		double newRho = NormalDistribution.randomDist(rho, tune);

		double newLikeli = li.calLogLikeli(thisSpot, tempParPiRhoLikeli(newPi,
				newRho));

		double newPrior = calculatePrior(tempParPiRhoPrior(newPi, newRho), gp);
		double newPost = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(0, 0, spotPosterLi, newPost,
				temperature);


		if (accept) {
			setPiRho(newPi, newRho);
			li.setEachLikelihood(spotIndex, newLikeli);
			// spotLikelihood = newLikeli;
			spotPosterLi = newPost;

		}
	}

	public double[] tempParPiRhoLikeli(double newPi, double newRho) {
		double[] probs = calcProb(newPi, newRho);
		return new double[] { mu1, probs[0], sd, getMu2(), probs[1], sd };
	}

	public double[] tempParPiRhoPrior(double newPi, double newRho) {

		return new double[] { mu1, d, newPi, newRho };
	}

	public double calculatePrior(ParGlobal gp) {

		double[] tempPar = { mu1, d, pi, rho };
		return calculatePrior(tempPar, gp);
	}

	public double calculatePrior(double[] tempPar, ParGlobal gp) {

		double prior = 
			NormalDistribution.logPdf(tempPar[0], gp.getMeanMuAlpha(),	gp.getMeanSdAlpha())
			+ TwoExpDistribution.logPdf(tempPar[1], gp.getLambda(), gp.getPhi())
			+ NormalDistribution.logPdf(tempPar[2], gp.getPiMuAlpha(), gp.getPiSdAlpha())
			+ NormalDistribution.logPdf(tempPar[3], gp.getRhoMuAlpha(), gp.getRhoSdAlpha());

		return prior;
	}

	public void setSpot(Spot s) {
		this.thisSpot = s;
	}

	public void setMu1(double mu1) {
		this.mu1 = mu1;
	}

	public void setD(double d) {
		this.d = d;
		// mu2 = mu1 + d;
	}

	public void setPi(double pi) {
		this.pi = pi;
		reCalcProb();
	}

	public void setRho(double rho) {
		this.rho = rho;
		reCalcProb();
	}

	public void setPiRho(double pi, double rho) {
		this.pi = pi;
		this.rho = rho;
		reCalcProb();
	}


	public void setSd(ParGlobal gp) {
		sd = gp.getSpotSd();
	}


	
	@Override
	public double[] getAllParLogOutput() {
		double[] allPar = { mu1, d, pi, rho };

		return allPar;
	}

	@Override
	public double[] getTunePar() {
		double[] allPar = { mu1, pi };

		return allPar;
	}
	
	

	public double[] getParLikeli(Likelihood li) {

		double[] allPar = { mu1, d, pi, rho, li.getEachLikelihood(spotIndex) };
		return allPar;
	}

	public Spot getSpot() {
		return thisSpot;
	}

	public double getLikelihood() {
		return spotPosterLi; 
	}

	public double getPosterior() {
		return spotPosterLi;
	}

	// public double getPriorLi() {
	// return spotPriorLi;
	// }

	public double getMu1() {
		return mu1;
	}

	public double getMu2() {
		mu2 = mu1 + d;
		return mu2;
	}

	// public void setMu2(double mu2) {
	// this.mu2 = mu2;
	// }

	public double getD() {
		return d;
	}

	public double getPi() {
		return pi;
	}

	public double getRho() {
		return rho;
	}

	public double getProb1() {

		return prob1;
	}

	public double getProb2() {

		return prob2;
	}

	public double getSd() {
		return sd;
	}

	public int getSpotIndex() {
		return spotIndex;
	}

	private void reCalcProb() {
		prob1 = Transformation.invLogit(pi);
		prob2 = Transformation.invLogit(pi + rho);
	}

	public static double[] calcProb(double pi, double rho) {
		double[] probs = { Transformation.invLogit(pi),
				Transformation.invLogit(pi + rho) };
		return probs;

	}

}
