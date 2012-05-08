/*******************************************************************************
 * CurrentPar.java
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

import com.google.common.primitives.Doubles;

public class CurrentPar {

	private static int[] globalOrder = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
	private static RandomDataImpl rgen = new RandomDataImpl();

	private double limDet;

	private ParGlobal gp;
	private ParSpot[] spArray;
	private ArrayList<Spot> allSpots;
	private Likelihood li;

	private int noSpot;


	/**
	 * @param totalIte
	 * @param limDet
	 */
	public CurrentPar(ArrayList<Spot> allSpots, double limDet) {

		this.allSpots = allSpots;
		this.noSpot = allSpots.size();
		this.limDet = limDet;

		gp = new ParGlobal(limDet);
		li = new Likelihood(noSpot, limDet);
		init();

	}

	public void init() {

		spArray = ParSpot.init(limDet, allSpots, gp, li);

		gp.initCalcLikeli(spArray, li);

	}

	public void updateParamLikelihood() {
		
		gp.calcParamLikelihood(spArray, li);

	}
	
	
	public void updateGlobal(TunePar tpGlobal) {

		double[] tune = tpGlobal.getTunePar();
		globalOrder = shuffle(globalOrder);
		 for (int i = 0; i < globalOrder.length; i++) {

			// switch (globalOrder[i]) {
			switch (i) {
			case 0:
				gp.updateMean(spArray, li, tune[0]);
				break;
			case 1:
				gp.updateDelta(spArray, li, tune[1]);

				break;
			case 2:
				gp.updateProb1(spArray, li, tune[2]);
				break;
			case 3:
				gp.updateProb2(spArray, li, tune[3]);
				break;
			case 4:
				gp.updateAlphaMu(spArray, li, tune[4]);
				break;
			case 5:
				gp.updateAlphaPi(spArray, li, tune[5]);
				break;
			case 6:
				gp.updateAlphaRho(spArray, li, tune[6]);
				break;
			case 7:
				gp.updateSpotScale(spArray, li, tune[7]);
				break;
			default:
				break;
			}

		}
	

	}

	
	public void updateLocalLikeli() {
		
		for (int i = 0; i < spArray.length; i++) {
			spArray[i].setupLikeli(gp, li) ;
		}
	}

	public void updateLocal(ArrayList<TunePar> tune) {

		double[] eachTune;
		for (int i = 0; i < spArray.length; i++) {
			eachTune = tune.get(i).getTunePar();
			spArray[i].updateLocalPar(gp, li, eachTune);
		}

	}

	public double[] getGlobalOutput() {

		double[] out = {
				getPosterior(),
				li.getSumPriorProb(),
				li.getSumParamLikelihood(),
				getGlobalLikelihood(),
				};
		out = Doubles.concat( out, gp.getAllParLogOutput()  );

		return out;
		
	}
	public double[] getLocalOutputAll() {

		double[] outMu = new double[noSpot];
		double[] outD = new double[noSpot];
		double[] outPi = new double[noSpot];
		double[] outRho = new double[noSpot];
		double[] outLikeli = new double[noSpot];

		for (int i = 0; i < noSpot; i++) {
			outMu[i] = spArray[i].getMu1();
			outD[i] = spArray[i].getD();
			outPi[i] = spArray[i].getPi();
			outRho[i] = spArray[i].getRho();
			outLikeli[i] = li.getEachLikelihood(i);
		}

		double[] outAll2 = Doubles.concat(outMu, outD, outPi, outRho, outLikeli);
		return outAll2;
	}

	public double[] getLocalOutput(int j) {
		
		
		double[] out = Doubles.concat(spArray[j].getAllParLogOutput(), new double[]{li.getEachLikelihood(j)} );	
		
		return out;
	}

	private int[] shuffle(int[] srcArray) {
	
		for (int i = 0; i < srcArray.length; i++) {
	
			int randomPosition = rgen.nextInt(0, srcArray.length - 1);
			int temp = srcArray[i];
			srcArray[i] = srcArray[randomPosition];
			srcArray[randomPosition] = temp;
		}
			return srcArray;
	}

	public Parameter getParGlobal() {
		return gp;
	}

	public Parameter[] getParSpot() {
		return spArray;
	}

	public Parameter getParSpotEach(int i) {
	
		return spArray[i];
	}

	public Likelihood getLikelihood() {
		return li;
	}

	public double getPosterior() {
		return li.getPosterior();
	}

	public double getGlobalLikelihood() {
		return li.getGlikelihood();
	}

	public double getLimDet() {
		return limDet;
	}

	public int getNoSpot() {
		return noSpot;
	}

	public double[] getParamOutput(int j) {

		double[] out = new double[noSpot];

		if (j == 0) {

			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getMu1();
			}

		} else if (j == 1) {

			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getD();
			}
		} else if (j == 2) {
			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getPi();
			}
		} else if (j == 3) {
			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getRho();
			}
		} else if (j == 4) {
			for (int i = 0; i < noSpot; i++) {
				out[i] = spArray[i].getLikelihood();
			}
		}
		return out;
	}

	@Deprecated
	public void setPrior(double... config) {
		int i = 0;
	
		gp.setPriorMu(config[i++], config[i++]);
		gp.setPriorSd(config[i++], config[i++]);
	
		gp.setPriorExp(config[i++], config[i++]);
		gp.setPriorPhi(config[i++], config[i++]);
	
		gp.setPriorProbMu(config[i++], config[i++]);
		gp.setPriorProbSd(config[i++], config[i++]);
	
		init();
	
	}

	@Deprecated
	public double[] getLocalOutput() {
	
		double[] outD = new double[noSpot];
		double[] outRho = new double[noSpot];
		double[] outLikeli = new double[noSpot];
	
		for (int i = 0; i < noSpot; i++) {
			outD[i] = spArray[i].getD();
			outRho[i] = spArray[i].getRho();
			outLikeli[i] = li.getEachLikelihood(i);
		}
	
		double[] outAll = new double[outD.length + outRho.length
				+ outLikeli.length];
		System.arraycopy(outD, 0, outAll, 0, outD.length);
		System.arraycopy(outRho, 0, outAll, outD.length, outRho.length);
		System.arraycopy(outLikeli, 0, outAll, outD.length + outRho.length,
				outLikeli.length);
	
		return outAll;
	}


}
