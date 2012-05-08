/*******************************************************************************
 * Simulation.java
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
package bide.simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Hashtable;


import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.util.Precision;


import bide.math.Transformation;

public class Simulation {

	private static final String FILE_SEP = System.getProperty("file.separator");
	private static final double EMPIRICAL_MEAN = -3.58;
	private static RandomDataImpl rd = new RandomDataImpl();
	
//	private static String simdata = "zzz";
	private static double globalSd = 1;
	private static int totalSpot = 100;
	private static int noSpotPerGroup = 12;
	private static double simMean = -5;

	// setupMean[i] = rd.nextUniform(-6, -2);
	// setupDelta[i] = rd.nextUniform(-2, 2);
	//
	// setupPi[i] = rd.nextUniform(-0.5, 0.5);
	// setupRho[i] = rd.nextUniform(-2, 2);

	public static void createSim(String simDir, int totalSpot) {
	
//		simdata = simDir;
		Simulation.totalSpot = totalSpot;
		noSpotPerGroup = 12;
		globalSd = 1;
		
		double limDet = -8.67;
		double spotScale = 0.7; 
		double spotSd = globalSd *  spotScale; 
		Hashtable<String, double[]> setupTable;
		
		setupTable = simRealistic();
		generateGelFile("simReal", simDir, limDet, spotSd, setupTable);
		
		setupTable = simUnifProb();
		generateGelFile("simUnifProb", simDir, limDet, spotSd, setupTable);
		
		setupTable = simTwoDiffProb();
		generateGelFile("simTwoProb", simDir, limDet, spotSd, setupTable);
		
	}

	private static void createSimFalsePositive() {

		String fName = "simFalsePos12";
		totalSpot = 100;
		noSpotPerGroup = 12;
		globalSd = 1;
		
		double limDet = -999;
		double spotScale = 0.7; 
		double spotSd = globalSd *  spotScale; 
		
		Hashtable<String, double[]> setupTable = simFalsePositive();
		generateGelFile(fName, null, limDet, spotSd, setupTable);
		
	}

	private static Hashtable<String, double[]> simRealistic() {
		
		double oneOverLambda = 1/0.5;
		double[] setupMean = new double[totalSpot];
		double[] setupDelta = new double[totalSpot];
		double[] setupPi = new double[totalSpot];
		double[] setupRho = new double[totalSpot];
		
		for (int i = 0; i < totalSpot; i++) {
			setupMean[i] = rd.nextGaussian(simMean, globalSd);
			if (i < 50) {
				setupDelta[i] = rd.nextExponential(oneOverLambda);
			} else {
				setupDelta[i] = -rd.nextExponential(oneOverLambda);
			}
			setupPi[i] = rd.nextGaussian(1, 1);
			setupRho[i] = rd.nextGaussian(0, 2);
		}
		
		Hashtable<String, double[]> table = new Hashtable<String, double[]>();
		table.put("setupMean", setupMean);
		table.put("setupDelta", setupDelta);
		table.put("setupPi", setupPi);
		table.put("setupRho", setupRho);
		return table;
	}
	
	private static Hashtable<String, double[]> simUnifProb() {
		
		double oneOverLambda = 1/0.5;
		double[] setupMean = new double[totalSpot];
		double[] setupDelta = new double[totalSpot];
		double[] setupPi = new double[totalSpot];
		double[] setupRho = new double[totalSpot];
		
		for (int i = 0; i < totalSpot; i++) {
			setupMean[i] = rd.nextGaussian(simMean, globalSd);
			if (i < 50) {
				setupDelta[i] = rd.nextExponential(oneOverLambda);
			} else {
				setupDelta[i] = -rd.nextExponential(oneOverLambda);
			}
			setupPi[i] = rd.nextUniform(-1, 3);
			setupRho[i] = rd.nextUniform(-2, 2);
		}
		
		Hashtable<String, double[]> table = new Hashtable<String, double[]>();
		table.put("setupMean", setupMean);
		table.put("setupDelta", setupDelta);
		table.put("setupPi", setupPi);
		table.put("setupRho", setupRho);
		return table;
	}
	
	private static Hashtable<String, double[]> simTwoDiffProb() {
		
		double oneOverLambda = 1.0/(1.0/1.5);
		double[] setupMean = new double[totalSpot];
		double[] setupDelta = new double[totalSpot];
		double[] setupPi = new double[totalSpot];
		double[] setupRho = new double[totalSpot];
		
		for (int i = 0; i < totalSpot; i++) {
			setupMean[i] = rd.nextGaussian(simMean, globalSd);
			if (i < 50) {
				setupDelta[i] = rd.nextExponential(oneOverLambda);
				setupRho[i] = rd.nextGaussian(-3, 0.25);
			} else {
				setupDelta[i] = -rd.nextExponential(oneOverLambda);
				setupRho[i] = rd.nextGaussian(2, 0.25);
			}
			setupPi[i] = rd.nextGaussian(1, 0.25);
			
		}
		
		Hashtable<String, double[]> table = new Hashtable<String, double[]>();
		table.put("setupMean", setupMean);
		table.put("setupDelta", setupDelta);
		table.put("setupPi", setupPi);
		table.put("setupRho", setupRho);
		return table;
	}
	private static Hashtable<String, double[]> simFalsePositive() {
		
		double[] setupMean = new double[totalSpot];
		double[] setupDelta = new double[totalSpot];
		double[] setupPi = new double[totalSpot];
		double[] setupRho = new double[totalSpot];
		
		for (int i = 0; i < totalSpot; i++) {
			setupMean[i] = rd.nextGaussian(simMean, globalSd);
			setupPi[i] = rd.nextGaussian(1, 1);
		}
		Arrays.fill(setupDelta, 0);
		Arrays.fill(setupRho, 0);
		
		Hashtable<String, double[]> table = new Hashtable<String, double[]>();
		table.put("setupMean", setupMean);
		table.put("setupDelta", setupDelta);
		table.put("setupPi", setupPi);
		table.put("setupRho", setupRho);
		
		return table;
	}

	private static void generateGelFile(String fName, String simDir, double limDet, double spotSd, Hashtable<String, double[]> setupTable) {
		
		double[] setupMean = setupTable.get("setupMean");
		double[] setupDelta = setupTable.get("setupDelta");
		double[] setupPi = setupTable.get("setupPi");
		double[] setupRho = setupTable.get("setupRho");
		
		try {
			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simDir).append(FILE_SEP).append(fName).append(FILE_SEP);

			System.out.println(sb.toString());
			checkDir(sb, false);
			
			
			sb.append(fName);
			String simLog = sb.toString() + ".log";
			PrintWriter outLog = new PrintWriter(new BufferedWriter(
					new FileWriter(simLog)));


			String simFile = sb.toString() + ".csv";
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(simFile)));

			GelSetting g = new GelSetting(noSpotPerGroup);
			out.println(g.createLabel());

			for (int i = 0; i < totalSpot; i++) {
				String info = i + "\t" + setupMean[i] + "\t" + setupDelta[i]
						+ "\t" + Transformation.invLogit(setupPi[i]) + "\t"
						+ Transformation.invLogit(setupPi[i] + setupRho[i]);
				System.out.println(info);
				outLog.println(info);
				g = new GelSetting(noSpotPerGroup, setupMean[i], setupDelta[i],
						setupPi[i], setupRho[i], spotSd);

				g.setLimDet(limDet);
				out.println(GelSetting.printGel(i, g.generateSpot() ));

			}
			out.close();
			outLog.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("END");
	}
	
	

	private static void checkDir(StringBuilder sb, boolean overWrite) {

		File f = new File(sb.toString());
		if (f.exists() & overWrite) {
			sb.append("_new");
			checkDir(sb, overWrite);
		} else {
			sb.append(FILE_SEP);
			f.mkdirs();
		}

	}

	public static void diffMean(int noOfData, String simdata) {

		for (int i = 0; i < 12; i++) {
			double meanDiff = Precision.round(globalSd * i / 4, 3);
			double halfMeanDiff = Precision.round(meanDiff / 2, 3);
			double smallMean = EMPIRICAL_MEAN - halfMeanDiff;

			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							"DiffMean_").append(i).append(".csv");

			String simFile = sb.toString();
			System.out.println(meanDiff + "\t" + smallMean + "\t"
					+ sb.toString());

			double limDet = -999;
			GelSetting g = new GelSetting(noSpotPerGroup, smallMean, meanDiff,
					50, 0, globalSd);
			g.gelSetP(1, 1);
			g.setLimDet(limDet);
			
			generateSimDataFile(simFile, g, noOfData);
		
			

		}

	}

	public static void diffProb(int noOfData, String simdata) {

		double limDet = -999;
		for (int p = 1; p < 11; p++) {
			double p1 = p / 10.0;
			for (int pp = 1; pp <= p; pp++) {
				double p2 = pp / 10.0;
				StringBuilder sb = new StringBuilder(System
						.getProperty("user.dir"));
				sb.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
						"MeanProb_").append(p1).append("_").append(p2).append(".csv");

				String simFile = sb.toString();
				System.out.println(p1 + "\t" + p2 + "\t" + simFile);

				GelSetting g = new GelSetting(noSpotPerGroup,
						EMPIRICAL_MEAN, 0, 50, 0, globalSd);
				g.gelSetP(p1, p2);
				g.setLimDet(limDet);

				generateSimDataFile(simFile, g, noOfData);

			}

		}
	}

	public static void diffLim(double gap, int noOfData, String simdata) {

		double limDet = -8.67;
		double meanDiff = Precision.round(globalSd * gap, 3);
		double halfMeanDiff = Precision.round(meanDiff / 2, 3);
		double smallMean = EMPIRICAL_MEAN - halfMeanDiff;
		System.out.println(meanDiff + "\t" + smallMean + "\t");
		
		for (int i = 1; i < 12; i++) {

			StringBuilder sb = new StringBuilder(System.getProperty("user.dir"))
					.append(FILE_SEP).append(simdata).append(FILE_SEP).append(
							"DiffLim_").append(i).append(".csv");

			String simFile = sb.toString();
			GelSetting g = new GelSetting(noSpotPerGroup, smallMean, meanDiff,
					50, 0, globalSd);
			g.gelSetP(1, 1);
			NormalDistribution nd = new NormalDistribution(smallMean,
					globalSd);

			System.out.println(sb.toString());
			
			try {
				
				limDet = nd.inverseCumulativeProbability(0.05 * (i - 1));
				System.out.println(simFile + "\t" + limDet);
				g.setLimDet(limDet);
				generateSimDataFile(simFile, g, totalSpot);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void generateSimDataFile(String simFile, GelSetting g, int noOfSpot) {
		try {
	
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(simFile)));
			out.println(g.createLabel());
			for (int j = 0; j < noOfSpot; j++) {
				out.println(j + "\t" + GelSetting.printGel(g.generateSpot()));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}



}
