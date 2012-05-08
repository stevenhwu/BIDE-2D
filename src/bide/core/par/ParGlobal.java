/*******************************************************************************
 * ParGlobal.java
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

import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.stat.StatUtils;

import bide.core.Likelihood;
import bide.core.MHRatio;
import bide.core.Setting;
import bide.math.Constant;
import bide.math.Transformation;
import bide.prior.PriorBeta;
import bide.prior.PriorDist;
import bide.prior.PriorExp;
import bide.prior.PriorInvGamma;
import bide.prior.PriorNormal;
import bide.proposal.ProposalNormal;

/**
 * @author steven
 * 
 */
public class ParGlobal implements Parameter {

	
	private static RandomDataImpl r = new RandomDataImpl();
	
	private double meanMu;
	private double meanSd;
	private double sdShape;
	private double sdScale;

	// private double lambdaUp;
	private double lambda;
	private double phi;

	private double piMu;
	private double piSd;
	private double rhoMu;
	private double rhoSd;

	private double alphaPi = 1;
	private double alphaRho = 1;
	private double alphaMu = 1;
	
	private double spotScale;

	private double spotSd;
	private double limDet;

	private PriorDist priorMeanMu;
	private PriorDist priorMeanVar;

	private PriorDist priorLambda;
	private PriorDist priorPhi;

	private PriorDist priorProbMu;
	private PriorDist priorProbVar;


	private double tuneSdDelta = 1;
	private double tuneSdMean = 1;
	private double tuneSdProb = 1;

	private double temperature = 1;

	

	public ParGlobal(double limDet) {
		reset(limDet);
		setDefaultPrior();
		calculateSpotSd();

	}

	private void calculateSpotSd() {

		spotSd = meanSd *  alphaMu * spotScale;
	}

	/**
	 * 
	 * @param limDet
	 */
	public void reset(double limDet) {

		this.limDet = limDet;
		meanMu = r.nextUniform(limDet / 2, limDet / 4);
		setMeanSd(r.nextUniform(Constant.MIN_SD, Constant.MAX_INIT));

		lambda = r.nextUniform(Constant.ZEROPLUS, Constant.TWO);
		phi = r.nextUniform(Constant.PHI_MIN, Constant.PHI_MAX);

		piMu = r.nextUniform(Constant.MIN_INIT, Constant.MAX_INIT);
		piSd = r.nextUniform(Constant.MIN_SD, Constant.MAX_INIT);
		rhoMu = r.nextUniform(Constant.MIN_INIT, Constant.MAX_INIT);
		rhoSd = r.nextUniform(Constant.MIN_SD, Constant.MAX_INIT);

		spotScale = r.nextUniform(Constant.ZEROPLUS, Constant.ONE);;
		
		alphaPi = 1;//r.nextUniform(Constant.ALPHA_MIN, Constant.MAX_INIT);
		alphaRho = 1;//r.nextUniform(Constant.ALPHA_MIN, Constant.MAX_INIT);
		alphaMu = 1;//r.nextUniform(Constant.ALPHA_MIN, Constant.MAX_INIT);
			
		
		
	}

	public void setDefaultPrior() {

		setPriorMu(-3, 5);
		setPriorSd(0.001, 0.001);
		setPriorExp(1);
		setPriorPhi(2,2);
		setPriorProbMu(0, 3);
		setPriorProbSd(0.001, 0.001);
// Winbug invGamma = 1/dgamma (shape, rate)
	}

	public void calcParamLikelihood(ParSpot[] sp, Likelihood li) {
		li.putParamLikelihood(GMUSD, li.condLikeliMu(sp, meanMu * alphaMu,
				meanSd * alphaMu));
		li.putParamLikelihood(GDELTA, li.condLikeliDelta(sp, lambda, phi));
		li.putParamLikelihood(GPI, li.condLikeliPi(sp, piMu * alphaPi,
				piSd * alphaPi));
		li.putParamLikelihood(GRHO, li.condLikeliRho(sp, rhoMu * alphaRho,
				rhoSd * alphaRho));

	}

	public void calcPriorProb(Likelihood li) {

//		li.putPriorProb(GMUSD, priorMeanMu.getLogPrior(meanMu)
//				+ priorMeanSd.getLogPrior(meanSd * meanSd));
//		li.putPriorProb(GDELTA, priorLambda.getLogPrior(lambda)
//				+ priorPhi.getLogPrior(phi));
//		li.putPriorProb(GPI, priorProbMu.getLogPrior(piMu)
//				+ priorProbSd.getLogPrior(piSd * piSd));
//		li.putPriorProb(GRHO, priorProbMu.getLogPrior(rhoMu)
//				+ priorProbSd.getLogPrior(rhoSd * rhoSd));
		li.putPriorProb(GMUSD, priorMeanMu.getLogPrior(meanMu * alphaMu)
				+ priorMeanVar.getLogPrior(meanSd * meanSd * alphaMu * alphaMu));
		li.putPriorProb(GDELTA, priorLambda.getLogPrior(lambda)
				+ priorPhi.getLogPrior(phi));
		li.putPriorProb(GPI, priorProbMu.getLogPrior(piMu * alphaPi)
				+ priorProbVar.getLogPrior(piSd * piSd * alphaPi * alphaPi));
		li.putPriorProb(GRHO, priorProbMu.getLogPrior(rhoMu * alphaRho)
				+ priorProbVar.getLogPrior(rhoSd * rhoSd * alphaRho * alphaRho));
	}

	public void initCalcLikeli(ParSpot[] sp, Likelihood li) {

		calcParamLikelihood(sp, li);
		calcPriorProb(li);

	}

	public void setPriorMu(double m, double sd) {

		priorMeanMu = new PriorNormal(m, sd);
	}

	public void setPriorSd(double shape, double rate) {

		priorMeanVar = new PriorInvGamma(shape, rate);


	}
	public void setPriorExp(double lambda) {

		priorLambda = new PriorExp(lambda);

	}

	public void setPriorPhi(double alpha, double beta) {

		 priorPhi = new PriorBeta(alpha, beta);
	}

	public void setPriorProbMu(double m, double sd) {

		priorProbMu = new PriorNormal(m, sd);
	}

	public void setPriorProbSd(double shape, double rate) {
		 priorProbVar = new PriorInvGamma(shape, rate);
	}
	

	public void updateMean(ParSpot[] sp, Likelihood li, double tune) {

		double[] newMu = ProposalNormal.nextTruncatedValue(meanMu, tune,
				limDet, Constant.GEL_MAX);
		double[] newSd = ProposalNormal.nextTruncatedValue( Math.pow(meanSd, 2),
				tune * tuneSdMean, Constant.MIN_SD, Constant.MAX_VAR);
		newSd[0] = Math.sqrt(newSd[0]);

		double[] newSpotScale = new double[] {spotScale, 0, 0};
		double[] newAlphaMu = new double[] { alphaMu, 0, 0 };

		double xGivenNewX = newMu[1] + newSd[1];
		double newXGivenX = newMu[2] + newSd[2];

		double oldPosterior = li.getParamLikelihood(GMUSD)
				+ li.getPriorProb(GMUSD);

		oldPosterior += StatUtils.sum(li.getEachLikelihood());


		double realMu = newMu[0] * newAlphaMu[0];
		double realSd = newSd[0] * newAlphaMu[0];
		
//		double newPrior = priorMeanMu.getLogPrior(newMu[0])
//				+ priorMeanSd.getLogPrior(newSd[0] * newSd[0]);
		double newPrior = priorMeanMu.getLogPrior(realMu)
				+ priorMeanVar.getLogPrior(realSd * realSd);
		double newLikeli = li.condLikeliMu(sp, realMu, realSd);
		double newPosterior = newLikeli + newPrior;
		// newPosterior += li.returnGlobalLogLikelihood(sp, newSd[0]);

		double[] allNewEachLikeli = li.returnGlobalLogLikelihood(sp, realSd * newSpotScale[0]);
		newPosterior += StatUtils.sum(allNewEachLikeli);

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				oldPosterior, newPosterior, temperature);

		if (accept) {
			meanMu = newMu[0];
			spotScale = newSpotScale[0];
			alphaMu = newAlphaMu[0];
			setMeanSd(newSd[0]);
			li.putPriorProb(GMUSD, newPrior);
			li.putParamLikelihood(GMUSD, newLikeli);
			li.updateAllEachLikeli(allNewEachLikeli);

		}
	}

	public void updateAlphaMu(ParSpot[] sp, Likelihood li, double tune) {

		double[] newAlphaMu = ProposalNormal.nextTruncatedValue(alphaMu, tune,
				Constant.ALPHA_MIN, Constant.ALPHA_MAX);

		double[] newMu = new double[] { meanMu, 0, 0 };
		double[] newSd = new double[] { meanSd, 0, 0 };
		double[] newSpotScale = new double[] {spotScale, 0, 0};
		
		
		
		double oldPosterior = li.getParamLikelihood(GMUSD)
				+ li.getPriorProb(GMUSD);
		oldPosterior += StatUtils.sum(li.getEachLikelihood());

		double realMu = newMu[0] * newAlphaMu[0];
		double realSd = newSd[0] * newAlphaMu[0];
		
		double xGivenNewX = newAlphaMu[1];
		double newXGivenX = newAlphaMu[2];

//		double newPrior = priorMeanMu.getLogPrior(newMu[0])
//				+ priorMeanSd.getLogPrior(newSd[0] * newSd[0]);
		
		double newPrior = priorMeanMu.getLogPrior(realMu)
				+ priorMeanVar.getLogPrior(realSd * realSd);
		double newLikeli = li.condLikeliMu(sp, realMu, realSd);
		double newPosterior = newLikeli + newPrior;
		// newPosterior += li.returnGlobalLogLikelihood(sp, newSd[0]);

		double[] allNewEachLikeli = li.returnGlobalLogLikelihood(sp, realSd* newSpotScale[0]);
		newPosterior += StatUtils.sum(allNewEachLikeli);

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX, oldPosterior, newPosterior,
				temperature);

		if (accept) {
			meanMu = newMu[0];
			spotScale = newSpotScale[0];
			alphaMu = newAlphaMu[0];
			setMeanSd(newSd[0]);
			li.putPriorProb(GMUSD, newPrior);
			li.putParamLikelihood(GMUSD, newLikeli);
			li.updateAllEachLikeli(allNewEachLikeli);

		}

	}


	public void updateSpotScale(ParSpot[] sp, Likelihood li, double tune) {

		double[] newSpotScale = ProposalNormal.nextTruncatedValue(spotScale, tune,
				0.0001, Constant.MAX_VAR);
		
		double[] newMu = new double[] { meanMu, 0, 0 };
		double[] newSd = new double[] { meanSd, 0, 0 };
		double[] newAlphaMu = new double[] { alphaMu, 0, 0 };
		
		//Uniform prior
		double oldPriorSS = 0;//priorSpotScale.getLogPrior(spotScale);
		double newPriorSS = 0;//priorSpotScale.getLogPrior(newSpotScale[0]);
		
		double xGivenNewX = newSpotScale[1];
		double newXGivenX = newSpotScale[2];
		
		double oldPosterior = li.getParamLikelihood(GMUSD)
				+ li.getPriorProb(GMUSD) + oldPriorSS;
		oldPosterior += StatUtils.sum(li.getEachLikelihood());

		double realMu = newMu[0] * newAlphaMu[0];
		double realSd = newSd[0] * newAlphaMu[0];
		
		
		double newPrior = li.getPriorProb(GMUSD) +newPriorSS;
		double newLikeli = li.condLikeliMu(sp, realMu, realSd);
		double newPosterior = newLikeli + newPrior;
		// newPosterior += li.returnGlobalLogLikelihood(sp, newSd[0]);

		double[] allNewEachLikeli = li.returnGlobalLogLikelihood(sp, realSd * newSpotScale[0]);
		newPosterior += StatUtils.sum(allNewEachLikeli);

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX, oldPosterior, newPosterior,
				temperature);

		if (accept) {
//			meanMu = newMu[0];
//			alphaMu = newAlphaMu[0];
			spotScale  = newSpotScale[0];
//			setMeanSd(newSd[0]);
			
			li.putPriorProb(GMUSD, newPrior);
			li.putParamLikelihood(GMUSD, newLikeli);
			li.updateAllEachLikeli(allNewEachLikeli);

		}

	}
	public void updateDelta(ParSpot[] sp, Likelihood li, double tune) {

		double[] newLambda = ProposalNormal.nextTruncatedValue(lambda, tune,
				Constant.MIN_SD, 10);

		double[] newPhi = ProposalNormal.nextTruncatedValue(Transformation.logit(phi),
				tune * tuneSdDelta, Constant.LOGIT_PHI_MIN, Constant.LOGIT_PHI_MAX);

		newPhi[0] = Transformation.invLogit(newPhi[0]);
		double xGivenNewX = newLambda[1] + newPhi[1];
		double newXGivenX = newLambda[2] + newPhi[2];

		double oldPosterior = li.getParamLikelihood(GDELTA)
				+ li.getPriorProb(GDELTA);

		double newPrior = priorLambda.getLogPrior(newLambda[0])
				+ priorPhi.getLogPrior(newPhi[0]);
		double newLikeli = li.condLikeliDelta(sp, newLambda[0], newPhi[0]);
		double newPosterior = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				oldPosterior, newPosterior, temperature);

		if (accept) {

			lambda = newLambda[0];
			phi = newPhi[0];
			li.putPriorProb(GDELTA, newPrior);
			li.putParamLikelihood(GDELTA, newLikeli);

		}
	}


	public void updateProb1(ParSpot[] sp, Likelihood li, double tune) {

		double[] newPiMu = ProposalNormal.nextValue(piMu, tune);

		double[] newPiSd = ProposalNormal.nextTruncatedValue(Math.pow(piSd, 2),
				tune * tuneSdProb, Constant.MIN_SD, Constant.MAX_VAR);
		newPiSd[0] = Math.sqrt(newPiSd[0]);

		double[] newAlphaPi = new double[] { alphaPi, 0, 0 };

		double realMu = newPiMu[0] * newAlphaPi[0];
		double realSd = newPiSd[0] * newAlphaPi[0];

		double xGivenNewX = newPiMu[1] + newPiSd[1];
		double newXGivenX = newPiMu[2] + newPiSd[2];

		double oldPosterior = li.getParamLikelihood(GPI) + li.getPriorProb(GPI);

//		double newPrior = priorProbMu.getLogPrior(newPiMu[0])
//		+ priorProbSd.getLogPrior(newPiSd[0] * newPiSd[0]);
		double newPrior = priorProbMu.getLogPrior(realMu)
				+ priorProbVar.getLogPrior(realSd * realSd);
		double newLikeli = li.condLikeliPi(sp, realMu, realSd);

		double newPosterior = newLikeli + newPrior;
		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				oldPosterior, newPosterior, temperature);

		if (accept) {

			piMu = newPiMu[0];
			piSd = newPiSd[0];
			alphaPi = newAlphaPi[0];
			li.putPriorProb(GPI, newPrior);
			li.putParamLikelihood(GPI, newLikeli);

		}

	}

	public void updateAlphaPi(ParSpot[] sp, Likelihood li, double tune) {

		double[] newAlphaPi = ProposalNormal.nextTruncatedValue(alphaPi, tune,
				Constant.ALPHA_MIN, Constant.ALPHA_MAX);

		double[] newPiMu = new double[] { piMu, 0, 0 };
		double[] newPiSd = new double[] { piSd, 0, 0 };
		// newAlpha = new double[] {alphaProb1, 0, 0};

		double realMu = newPiMu[0] * newAlphaPi[0];
		double realSd = newPiSd[0] * newAlphaPi[0];

		 double xGivenNewX = newAlphaPi[1];
		 double newXGivenX = newAlphaPi[2];

		double oldPosterior = li.getParamLikelihood(GPI) + li.getPriorProb(GPI);

//		double newPrior = priorProbMu.getLogPrior(newPiMu[0])
//				+ priorProbSd.getLogPrior(newPiSd[0] * newPiSd[0]);
		double newPrior = priorProbMu.getLogPrior(realMu)
				+ priorProbVar.getLogPrior(realSd * realSd);
		double newLikeli = li.condLikeliPi(sp, realMu, realSd);

		double newPosterior = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX, oldPosterior, newPosterior,
				temperature);

		if (accept) {

			piMu = newPiMu[0];
			piSd = newPiSd[0];
			alphaPi = newAlphaPi[0];
			li.putPriorProb(GPI, newPrior);
			li.putParamLikelihood(GPI, newLikeli);

		}

	}

	public void updateProb2(ParSpot[] sp, Likelihood li, double tune) {

		double[] newRhoMu = ProposalNormal.nextValue(rhoMu, tune);
		double[] newRhoSd = ProposalNormal.nextTruncatedValue(Math
				.pow(rhoSd, 2), tune * tuneSdProb, Constant.MIN_SD, Constant.MAX_VAR);
		newRhoSd[0] = Math.sqrt(newRhoSd[0]);

		double[] newAlphaRho = new double[] { alphaRho, 0, 0 };

		double realMu = newRhoMu[0] * newAlphaRho[0];
		double realSd = newRhoSd[0] * newAlphaRho[0];

		double xGivenNewX = newRhoMu[1] + newRhoSd[1];
		double newXGivenX = newRhoMu[2] + newRhoSd[2];

		double oldPosterior = li.getParamLikelihood(GRHO)
				+ li.getPriorProb(GRHO);

//		double newPrior = priorProbMu.getLogPrior(newRhoMu[0])
//				+ priorProbSd.getLogPrior(newRhoSd[0] * newRhoSd[0]);
		double newPrior = priorProbMu.getLogPrior(realMu)
				+ priorProbVar.getLogPrior(realSd * realSd);
		double newLikeli = li.condLikeliRho(sp, realMu, realSd);

		double newPosterior = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				oldPosterior, newPosterior, temperature);
		// accept = MHRatio.accept(0, 0, oldPosterior, newPosterior);
		if (accept) {
			rhoMu = newRhoMu[0];
			rhoSd = newRhoSd[0];
			alphaRho = newAlphaRho[0];
			li.putPriorProb(GRHO, newPrior);
			li.putParamLikelihood(GRHO, newLikeli);

		}
	}

	public void updateAlphaRho(ParSpot[] sp, Likelihood li, double tune) {


		double[] newRhoMu = new double[] { rhoMu, 0, 0 };
		double[] newRhoSd = new double[] { rhoSd, 0, 0 };

		double[] newAlphaRho = ProposalNormal.nextTruncatedValue(alphaRho,
				tune, Constant.ALPHA_MIN, Constant.ALPHA_MAX);

		double realMu = newRhoMu[0] * newAlphaRho[0];
		double realSd = newRhoSd[0] * newAlphaRho[0];

		double xGivenNewX = newAlphaRho[1];
		double newXGivenX = newAlphaRho[2];

		double oldPosterior = li.getParamLikelihood(GRHO)
				+ li.getPriorProb(GRHO);
//
//		double newPrior = priorProbMu.getLogPrior(newRhoMu[0])
//				+ priorProbSd.getLogPrior(newRhoSd[0] * newRhoSd[0]);
		double newPrior = priorProbMu.getLogPrior(realMu)
				+ priorProbVar.getLogPrior(realSd * realSd);
		double newLikeli = li.condLikeliRho(sp, realMu, realSd);

		double newPosterior = newLikeli + newPrior;

		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
				oldPosterior, newPosterior, temperature);

		if (accept) {
			rhoMu = newRhoMu[0];
			rhoSd = newRhoSd[0];
			alphaRho = newAlphaRho[0];
			li.putPriorProb(GRHO, newPrior);
			li.putParamLikelihood(GRHO, newLikeli);

		}

	}
//
//	public void updateMeanAndAlpahMu(ParSpot[] sp, Likelihood li, double tune) {
//	
//		double[] newMu = ProposalNormal.nextTruncatedValue(meanMu, tune,
//				limDet, Constant.GEL_MAX);
//		double[] newSd = ProposalNormal.nextTruncatedValue( Math.pow(meanSd, 2),
//				tune * tuneSdMean, Constant.MIN_SD, Constant.MAX_SD);
//		double[] newAlphaMu = ProposalNormal.nextTruncatedValue(alphaMu, tune,
//				Constant.ALPHA_MIN, Constant.ALPHA_MAX);
//		newSd[0] = Math.sqrt(newSd[0]);
//	
//		double[] newSpotScale = new double[] {spotScale, 0, 0};
//		
//		double xGivenNewX = newMu[1] + newSd[1] + newAlphaMu[1];
//		double newXGivenX = newMu[2] + newSd[2] + newAlphaMu[2];
//	
//		double oldPosterior = li.getParamLikelihood(GMUSD)
//				+ li.getPriorProb(GMUSD);
//	
//		oldPosterior += StatUtils.sum(li.getEachLikelihood());
//	
//	
//		double realMu = newMu[0] * newAlphaMu[0];
//		double realSd = newSd[0] * newAlphaMu[0];
//	
////		double newPrior = priorMeanMu.getLogPrior(newMu[0])
////				+ priorMeanSd.getLogPrior(newSd[0] * newSd[0]);
//		double newPrior = priorMeanMu.getLogPrior(realMu)
//				+ priorMeanVar.getLogPrior(realSd * realSd);
//		double newLikeli = li.condLikeliMu(sp, realMu, realSd);
//		double newPosterior = newLikeli + newPrior;
//		// newPosterior += li.returnGlobalLogLikelihood(sp, newSd[0]);
//	
//		double[] allNewEachLikeli = li.returnGlobalLogLikelihood(sp, realSd * newSpotScale[0]);
//		newPosterior += StatUtils.sum(allNewEachLikeli);
//	
//		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
//				oldPosterior, newPosterior, temperature);
//	
//		if (accept) {
//			meanMu = newMu[0];
//			spotScale = newSpotScale[0];
//			alphaMu = newAlphaMu[0];
//			setMeanSd(newSd[0]);
//			li.putPriorProb(GMUSD, newPrior);
//			li.putParamLikelihood(GMUSD, newLikeli);
//			li.updateAllEachLikeli(allNewEachLikeli);
//	
//		}
//	}
//
//	public void updateProb1AndAlphaPi(ParSpot[] sp, Likelihood li, double tune) {
//	
//		double[] newAlphaPi = ProposalNormal.nextTruncatedValue(alphaPi, tune,
//				Constant.ALPHA_MIN, Constant.ALPHA_MAX);
//	
//		double[] newPiMu = ProposalNormal.nextValue(piMu, tune);
//		double[] newPiSd = ProposalNormal.nextTruncatedValue(Math.pow(piSd, 2),
//				tune * tuneSdProb, Constant.MIN_SD, Constant.MAX_SD);
//		newPiSd[0] = Math.sqrt(newPiSd[0]);
//	
//		double realMu = newPiMu[0] * newAlphaPi[0];
//		double realSd = newPiSd[0] * newAlphaPi[0];
//	
//		 double xGivenNewX = newPiMu[1] + newPiSd[1] + newAlphaPi[1];
//		 double newXGivenX = newPiMu[2] + newPiSd[2] + newAlphaPi[2];
//	
//		double oldPosterior = li.getParamLikelihood(GPI) + li.getPriorProb(GPI);
//	
////		double newPrior = priorProbMu.getLogPrior(newPiMu[0])
////				+ priorProbSd.getLogPrior(newPiSd[0] * newPiSd[0]);
//		double newPrior = priorProbMu.getLogPrior(realMu)
//				+ priorProbVar.getLogPrior(realSd * realSd);
//		double newLikeli = li.condLikeliPi(sp, realMu, realSd);
//	
//		double newPosterior = newLikeli + newPrior;
//	
//		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX, oldPosterior, newPosterior,
//				temperature);
//	
//		if (accept) {
//	
//			piMu = newPiMu[0];
//			piSd = newPiSd[0];
//			alphaPi = newAlphaPi[0];
//			li.putPriorProb(GPI, newPrior);
//			li.putParamLikelihood(GPI, newLikeli);
//	
//		}
//	
//	}
//
//	public void updateProb2AndAlphaRho(ParSpot[] sp, Likelihood li, double tune) {
//	
//	
//		double[] newRhoMu = ProposalNormal.nextValue(rhoMu, tune);
//		double[] newRhoSd = ProposalNormal.nextTruncatedValue(
//				Math.pow(rhoSd, 2), tune * tuneSdProb, Constant.MIN_SD,
//				Constant.MAX_SD);
//		newRhoSd[0] = Math.sqrt(newRhoSd[0]);
//		
//		double[] newAlphaRho = ProposalNormal.nextTruncatedValue(alphaRho,
//				tune, Constant.ALPHA_MIN, Constant.ALPHA_MAX);
//	
//		double realMu = newRhoMu[0] * newAlphaRho[0];
//		double realSd = newRhoSd[0] * newAlphaRho[0];
//	
//		double xGivenNewX = newRhoMu[1] + newRhoSd[1] + newAlphaRho[1];
//		double newXGivenX = newRhoMu[2] + newRhoSd[2] + newAlphaRho[2];
//	
//		double oldPosterior = li.getParamLikelihood(GRHO)
//				+ li.getPriorProb(GRHO);
//	
////		double newPrior = priorProbMu.getLogPrior(newRhoMu[0])
////				+ priorProbSd.getLogPrior(newRhoSd[0] * newRhoSd[0]);
//		double newPrior = priorProbMu.getLogPrior(realMu)
//				+ priorProbVar.getLogPrior(realSd * realSd);
//		double newLikeli = li.condLikeliRho(sp, realMu, realSd);
//	
//		double newPosterior = newLikeli + newPrior;
//	
//		boolean accept = MHRatio.acceptTemp(xGivenNewX, newXGivenX,
//				oldPosterior, newPosterior, temperature);
//	
//		if (accept) {
//			rhoMu = newRhoMu[0];
//			rhoSd = newRhoSd[0];
//			alphaRho = newAlphaRho[0];
//			li.putPriorProb(GRHO, newPrior);
//			li.putParamLikelihood(GRHO, newLikeli);
//	
//		}
//	
//	}

	public double getLimDet() {
		return limDet;
	}

	public double getSpotSd() {

		return spotSd;
	}

	public double getPhi() {
		return phi;
	}

	public double getLambda() {
		return lambda;
	}



	
	public void setMeanSd(double sd) {
		this.meanSd = sd;
		calculateSpotSd();
	}


	@Deprecated
	public void setAllGlobalPar(double... par) {
		setAllGlobalPar(par[0], par[1], par[2], par[3], par[4], par[5], par[6], par[7], par[8] );
		
	}
	@Deprecated
	private void setAllGlobalPar(double mu, double sd, double lambdaDown,
			double lambdaUp, double phi, double p1v1, double p1v2, double p2v1,
			double p2v2) {
	
		this.meanMu = mu;
		setMeanSd(sd);
	
		this.lambda = lambdaDown;
		// this.lambdaUp = lambdaUp;
		this.phi = phi;
	
		this.piMu = p1v1;
		this.piSd = p1v2;
	
		this.rhoMu = p2v1;
		this.rhoSd = p2v2;
	
		this.alphaMu = 1;
		this.alphaPi = 1;
		this.alphaRho = 1;
		// this.n = noSpot;
	}

	@Deprecated
	public void setPriorExp(double a, double b) {
	
		priorLambda = new PriorExp(1);
	
	}


	@Deprecated
	public double getGap() {
		return Setting.getGap();
	}
	
	@Deprecated
	public double getGapLower() {
		return Setting.getGapLower();
	}
	
	@Deprecated
	public double getGapUpper() {
		return Setting.getGapUpper();
	}

	public static final String GMUSD = "GMUSD";
	public static final String GDELTA = "GDELTA";
	public static final String GPI = "GPI";
	public static final String GRHO = "GRHO";
	public static final String GSD = "GSD";

	public static final String[] GLOBAL_LABELS = { "Ite",
		"posterior","prior", "globalParamLikelihood", "globalLikelihood", 
		"muAlpha","sdAlpha", 
		"spotScale",
		"lambda", "phi",
		"piMuAlpha", "piSdAlpha", 
		"rhoMuAlpha", "rhoSdAlpha", 
		"spotSd",
		"alphaMu", "alphaPi", "alphaRho",
		};
	
	@Override
	public double[] getAllParLogOutput() {

		double[] out = {
				getMeanMuAlpha(), getMeanSdAlpha(),
				spotScale,
				lambda, phi, 
				getPiMuAlpha(), getPiSdAlpha(), 
				getRhoMuAlpha(), getRhoSdAlpha(),
				spotSd,
				alphaMu, alphaPi, alphaRho,
		};
		return out;
	}
//	public static final int INDEX_MU = 0;
//	public static final int INDEX_LAMBDA = 3;
//	public static final int INDEX_PI = 4;
//	public static final int INDEX_PI_SD = 5;
//	
//	public static final int INDEX_RHO = 6;
//	public static final int INDEX_RHO_SD = 7;
//	
//	public static final int INDEX_PI_ALPHA = 8;
//	public static final int INDEX_RHO_ALPHA = 9;
//	public static final int INDEX_MU_ALPHA = 10;
//	public static final int INDEX_SPOT_SCALE = 11;
	


	@Override
	public double[] getTunePar() {
	
		double[] allTunePar = { 
				meanMu, lambda, piMu, rhoMu, 
				alphaMu, alphaPi, alphaRho, spotScale};
		
		return allTunePar;
	}

	public double getMeanMuAlpha() {
		
		return meanMu * alphaMu;
	}
	public double getMeanSdAlpha() {
		
		return meanSd * alphaMu;
	}
	public double getPiMuAlpha() {
		
		return piMu * alphaPi;
	}
	public double getPiSdAlpha() {
		
		return piSd * alphaPi;
	}
	public double getRhoMuAlpha() {
		
		return rhoMu * alphaRho;
	}
	public double getRhoSdAlpha() {
		
		return rhoSd * alphaRho;
	}
}
