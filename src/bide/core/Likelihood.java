/*******************************************************************************
 * Likelihood.java
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
package bide.core;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.math3.stat.StatUtils;

import bide.core.par.ParGlobal;
import bide.core.par.ParSpot;
import bide.core.par.Spot;
import bide.math.Constant;
import bide.math.NormalDistribution;
import bide.math.TwoExpDistribution;

/**
 * @author steven
 * 
 *         P(theta| spots) ~ P(spots | gp, sp)P(sp | gp)P(gp)
 */

public class Likelihood {

	
	final private String[] fixLists = new String[] { ParGlobal.GMUSD, ParGlobal.GDELTA,
			ParGlobal.GPI, ParGlobal.GRHO, ParGlobal.GSD };

	private double upperLim;
	private double lowerLim;
	private double glikelihood;
	private double posterior;
	private double[] eachLikelihood;


	private HashMap<String, Double> priorProb = new HashMap<String, Double>();
	private HashMap<String, Double> paramLikelihood = new HashMap<String, Double>();
	
	public Likelihood(int n, double limDet) {

		eachLikelihood = new double[n];
		setUpperLim(Constant.GEL_MAX);
		setLowerLim(limDet);
		init();
	}

	public void init() {

		this.posterior = 0;
		this.glikelihood = 0;
		for (String string : fixLists) {
			priorProb.put(string, 0.0);
			paramLikelihood.put(string, 0.0);
		}

	}

	public void setLowerLim(double lowerLim) {
		this.lowerLim = lowerLim;
	}

	public void setUpperLim(double upperLim) {
		this.upperLim = upperLim;
	}

	public double calLogLikeli(ParSpot eachSp) {

		Spot eachSpot = eachSp.getSpot();
		double l = calLogLikeli(eachSpot, eachSp);

		return l;
	}

	public double calLogLikeli(Spot eachSpot, double... pars) {

		int i = 0;
		int noSpot = eachSpot.getNumberOfSpot();
		double l = calLogLikeli(eachSpot.getExpressControlSpot(), noSpot,
				pars[i++], pars[i++], pars[i++])
				+ calLogLikeli(eachSpot.getExpressCaseSpot(), noSpot,
						pars[i++], pars[i++], pars[i++]);
		return l;

	}

	private double calLogLikeli(Spot eachSpot, ParSpot eachSp) {

		int noSpot = eachSpot.getNumberOfSpot();
		double l = calLogLikeli(eachSpot.getExpressControlSpot(), noSpot,
				eachSp.getMu1(), eachSp.getProb1(), eachSp.getSd())
				+ calLogLikeli(eachSpot.getExpressCaseSpot(), noSpot, eachSp
						.getMu2(), eachSp.getProb2(), eachSp.getSd());
		return l;

	}

	private double calLogLikeli(double[] expSpot, int noTotal, double u,
			double p, double sd) {

		int express = expSpot.length;
		int notExpress = noTotal - express;


		double logP = (p == 0) ? 0 : Math.log(p) * express; // n*log(p)+

		double logNotExp = (1 - p) + p
				* NormalDistribution.cdf(lowerLim, u, sd);

		logNotExp = (logNotExp == 0) ? 0 : Math.log(logNotExp) * notExpress;

		double likeli = StatUtils.sum(NormalDistribution.logPdfBT(expSpot, u,
				sd, lowerLim, upperLim))
				+ logP + logNotExp;


		return likeli ;

	}
	public double[] returnGlobalLogLikelihood(ParSpot[] sp, double newSd) {

//		double l = 0;
		double[] newEachLikeli = new double[sp.length];
//		double newSdSpot = newSd * Math.sqrt(sp.length);
		double newSdSpot = newSd ;
		for (int i = 0; i < sp.length; i++) {

			double[] newPar = { sp[i].getMu1(), sp[i].getProb1(), newSdSpot,
					sp[i].getMu2(), sp[i].getProb2(), newSdSpot };
			newEachLikeli[i] = calLogLikeli(sp[i].getSpot(), newPar);
//			l += newEachLikeli[i]; 
		}
		
		return newEachLikeli;
	}

	public void updateAllEachLikeli(double[] allNewEachLikeli) {
		for (int i = 0; i < allNewEachLikeli.length; i++) {
			setEachLikelihood(i,allNewEachLikeli[i]);
		}
		
	}

	public double getGlikelihood() {
		return glikelihood;
	}

	/**
	 * calculate sum( log( p(m_control_i | mu1) )) + log(p(mu1)) the likelihood
	 * for each parameter is independent
	 * 
	 * @param sp
	 * @param m
	 * @param sd
	 * @return
	 */
	public double condLikeliMu(ParSpot[] sp, double m, double sd) {

		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			l += NormalDistribution.logPdf(sp[i].getMu1(), m, sd);
		}

		return l;

	}


	public double condLikeliDelta(ParSpot[] sp, double lambda, double phi) {

		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			l += TwoExpDistribution.logPdf(sp[i].getD(), lambda, phi);
		}
		return l;
	}

	/**
	 * Calculate sum ( log ( p1 | mu, sd) )
	 * 
	 * @param sp
	 * @param m
	 * @param sd
	 * @return
	 */
	public double condLikeliPi(ParSpot[] sp, double m, double sd) {

		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			
			l += NormalDistribution.logPdf(sp[i].getPi(), m, sd);
			}

		return l;
	}
	/**
	 * Calculate sum ( log ( p2 | mu, sd) )
	 * 
	 * @param sp
	 * @param m
	 * @param sd
	 * @return
	 */
	public double condLikeliRho(ParSpot[] sp, double m, double sd) {

		double l = 0;
		for (int i = 0; i < sp.length; i++) {
			l += NormalDistribution.logPdf(sp[i].getRho(), m, sd);

		}
		return l;
	}



	public double getPosterior() {
		return posterior;
	}

	public double getPriorProb(String key) {
		return priorProb.get(key);
	}

	public void putPriorProb(String key, double value) {

		posterior = posterior - priorProb.get(key) + value;
		priorProb.put(key, value);
	}

	public double getParamLikelihood(String key) {
		return paramLikelihood.get(key);
	}

	public void putParamLikelihood(String key, double value) {

		replaceLikelihood(paramLikelihood.get(key), value);
		paramLikelihood.put(key, value);

	}

	public double[] getEachLikelihood() {
		return eachLikelihood;
	}

	public double getEachLikelihood(int i) {
		return eachLikelihood[i];
	}

	public void setEachLikelihood(int i, double value) {

		replaceLikelihood(eachLikelihood[i], value);
		eachLikelihood[i] = value;
	}

	private void replaceLikelihood(double oldLikelihood, double newLikelihood) {

		double t =  newLikelihood - oldLikelihood;
		glikelihood = glikelihood + t;
		posterior = posterior +t;


	}

	public double getSumPriorProb() {
		double l = 0;
		for (String s : fixLists) {
			l += priorProb.get(s);
		}
		return l;
	}

	public double getSumParamLikelihood() {
		double l = 0;
		for (String s : fixLists) {
			l += paramLikelihood.get(s);
		}
		
		return l;
	}

	@Override
	public String toString() {
		return "Likelihood [eachLikelihood=" + Arrays.toString(eachLikelihood)
				+ "\n glikelihood=" + glikelihood + "\n paramLikelihood="
				+ paramLikelihood + "\n priorProb=" + priorProb
				+ "\n upperLim=" + upperLim + ", lowerLim=" + lowerLim
				+ "\n getGlikelihood()=" + getGlikelihood() + "]";
	}
	@Deprecated
	public String getParamLikelihoodString() {

		StringBuilder sb = new StringBuilder();
		for (String s : fixLists) {
			sb.append(s).append(": ").append(paramLikelihood.get(s)).append(
					"\t");
		}

		return sb.toString();
	}
	@Deprecated
	public String getPriorProbString() {
		StringBuilder sb = new StringBuilder();
		for (String s : fixLists) {
			sb.append(s).append(": ").append(priorProb.get(s)).append("\t");
		}

		return sb.toString();

	}

	@Deprecated
	public void calGlobalLogLikelihood(ParSpot[] sp) {
	
		for (int i = 0; i < sp.length; i++) {
			setEachLikelihood(i, calLogLikeli(sp[i]));
		}
	}

	@Deprecated
	public void calGlobalLogLikelihood(ParSpot[] sp, ParGlobal gp) {
	
		for (int i = 0; i < sp.length; i++) {
			sp[i].setSd(gp);
			setEachLikelihood(i, calLogLikeli(sp[i]));
		}
	}

}
