/*******************************************************************************
 * MCMC.java
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


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import bide.core.par.CurrentPar;
import bide.core.par.ParGlobal;
import bide.core.par.ParSpot;
import bide.core.par.SavePar;
import bide.core.par.SaveParGlobal;
import bide.core.par.SaveParLocal;
import bide.core.par.Spot;
import bide.core.par.TunePar;
import bide.logger.NumberColumn;
import bide.logger.PerformanceLogger;
import bide.logger.TabFormatter;
import bide.math.Constant;

//import classes.org.jdom.

/**
 * @author steven
 * 
 */
public class MCMC {

	private final static String FILE_SEP = System.getProperty("file.separator");
	
	private static final String[] SPOT_LABELS = ParSpot.SPOT_LABELS;
	private static final String[] GLOBAL_LABELS = ParGlobal.GLOBAL_LABELS;

	private static Random rand = new Random();
	private int totalIte;
	private int thinning;
	private double limDet;
	
	private ArrayList<Spot> allSpots = new ArrayList<Spot>();
	private CurrentPar cp;

	private String globalOutFile;
	private String localOutFile;
	private String path;

	public MCMC(String configFile) {

		readConfig(configFile);

		limDet = findMin(allSpots);
		System.out.println("LimDet: "+limDet);

		cp = new CurrentPar(allSpots, limDet);


	}



	public void run() {

		try {
			runMCMC();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void runMCMC() throws IOException {

		NumberColumn nc = new NumberColumn("", 1);
		nc.setDecimalPlaces(5);

		// global and local
		TabFormatter tabOutGlobal = createGlobalLabels(globalOutFile, nc);
		TabFormatter tabOutLocal = createLocalLabels(localOutFile, nc); 
		
		ArrayList<TabFormatter> logEachParam = createTabFormatterEachParam(nc); 
		ArrayList<TabFormatter> logEachLocal = createTabFormatterEachLocal(nc);


		int tuneSize = Constant.TUNESIZE;
		int tuneGroup = Constant.TUNEGROUP;
		double updateGlobalProb = Constant.UPDATE_GLOBAL_PROB;
		int saveInterval = thinning;
		int perfLogInterval = saveInterval * 1;

		int tuneLocalCount = tuneSize;
		int tuneGlobalCount = tuneSize;
		int noSpot = cp.getNoSpot();

		
		TunePar tpGlobal = new TunePar(Setting.GLOBAL, totalIte, tuneSize,tuneGroup, "normal");
		SavePar saveTuneGlobal = new SaveParGlobal(tuneSize);

		ArrayList<TunePar> tpLocal = new ArrayList<TunePar>();
		ArrayList<SavePar> saveTuneLocal = new ArrayList<SavePar>();
		for (int i = 0; i < noSpot; i++) {
			saveTuneLocal.add(new SaveParLocal(tuneSize));
			tpLocal.add(new TunePar(Setting.LOCAL, totalIte, tuneSize, tuneGroup, "normal"));
		}

		// performance log
		PerformanceLogger perfLog = new PerformanceLogger(path + "Performance.log");
		PerformanceLogger perfScreen = new PerformanceLogger();
		perfLog.startLogging();
		perfScreen.startLogging();


		int gCount = 0;
		int lCount = 0;

		for (int ite = 0; ite < totalIte; ite++) {

			if(rand.nextDouble() < updateGlobalProb){
				gCount++;
				cp.updateParamLikelihood();
				cp.updateGlobal(tpGlobal);
//				cp.updateGlobalAndAlpha(tpGlobal.getTunePar());
				cp.updateLocalLikeli();
				saveTuneGlobal.addPar(cp.getParGlobal(), gCount);

				if (gCount % tuneGlobalCount == 0) {
					tpGlobal.update(saveTuneGlobal, gCount);

				}
			}
			else{
				lCount++;
				cp.updateLocal(tpLocal);

				// Save par for tuning local
				for (int j = 0; j < noSpot; j++) {
					saveTuneLocal.get(j).addPar(cp.getParSpotEach(j), ite);
				}
				if (lCount % tuneLocalCount == 0) {
					// 	update tuning local
					for (int j = 0; j < noSpot; j++) {
						tpLocal.get(j).update(saveTuneLocal.get(j), lCount);
					}
				}
				
			}
			



			// output local
			if (ite % saveInterval == 0) {
				
				// tabOutLocal.logValues(ite, cp.getLocalOutput());
				tabOutLocal.logValues(ite, cp.getLocalOutputAll());
				tabOutGlobal.logValues(ite, cp.getGlobalOutput());
				
				for (int j = 0; j < cp.getNoSpot(); j++) {
					logEachLocal.get(j).logValues(ite, cp.getLocalOutput(j));

				}
				for (int j = 0; j < SPOT_LABELS.length-1; j++) {
					logEachParam.get(j).logValues(ite, cp.getParamOutput(j));
				}

				
				if (ite % perfLogInterval == 0) {
					perfScreen.log(ite);
					perfLog.log(ite);
					System.out.println(Arrays.toString(nc.formatValues(cp.getGlobalOutput())));
				}

			}
		}

		// tabOutLocal.logValues(ite, cp.getLocalOutput());
		tabOutLocal.logValues(totalIte, cp.getLocalOutputAll());
		tabOutGlobal.logValues(totalIte, cp.getGlobalOutput());
		for (int j = 0; j < cp.getNoSpot(); j++) {
			logEachLocal.get(j).logValues(totalIte, cp.getLocalOutput(j));

		}
		for (int j = 0; j < SPOT_LABELS.length-1; j++) {
			logEachParam.get(j).logValues(totalIte, cp.getParamOutput(j));
		}
		

	
		perfScreen.stopLogging();
		perfLog.stopLogging();

		// tabOutGlobal.logValues(tpGlobal.getTunePar());
		System.out.println(tpGlobal.toString());
		System.out.println(tpLocal.get(0).toString());
		System.out.println("\n=====END=====");

	}

	private TabFormatter createGlobalLabels(String globalOutFile, NumberColumn nc) throws IOException {
		TabFormatter tabOutGlobal = new TabFormatter(globalOutFile, nc);
		tabOutGlobal.logHeading("Global parameters");
		tabOutGlobal.logLabels(GLOBAL_LABELS);

		return tabOutGlobal;
	}



	private TabFormatter createLocalLabels(String localOutFile, NumberColumn nc) throws IOException {
		
		TabFormatter tabOutLocal = new TabFormatter(localOutFile, nc);
		tabOutLocal.logHeading("Local parameters");
	
		List<String> s = new ArrayList<String>();
		s.add("Ite");
		for (int j = 1; j < SPOT_LABELS.length; j++) {
			for (int i = 0; i < cp.getNoSpot(); i++) {
				s.add("Spot" + i + "_" + SPOT_LABELS[j]);
			}
		}
		tabOutLocal.logLabels(s.toArray(new String[s.size()]));
		
		return tabOutLocal;
	}



	private ArrayList<TabFormatter> createTabFormatterEachLocal(NumberColumn nc) throws IOException {
		ArrayList<TabFormatter> logEachLocal = new ArrayList<TabFormatter>();
		for (int i = 0; i < cp.getNoSpot(); i++) {
			TabFormatter tf = new TabFormatter(path + "Spot" + (i + 1) + ".log", nc);
			tf.logHeading("Spot" + (i + 1));
			tf.logLabels(SPOT_LABELS);
			logEachLocal.add(tf);
		}

		return logEachLocal;
	}



	private ArrayList<TabFormatter> createTabFormatterEachParam(NumberColumn nc) throws IOException {
		
	
		ArrayList<TabFormatter> logEachParam = new ArrayList<TabFormatter>();
		List<String> alString = new ArrayList<String>();

		for (int j = 1; j < SPOT_LABELS.length; j++) {
			TabFormatter tf = new TabFormatter(path + "Spot_" + SPOT_LABELS[j] + ".log", nc);
 
			tf.logHeading("Spot_" + SPOT_LABELS[j]);
			
			alString.clear();
			for (int i = 0; i < cp.getNoSpot(); i++) {
				alString.add("Spot" + i + "_" + SPOT_LABELS[j]);
			}
			tf.logLabels(alString.toArray(new String[alString.size()]));
			
			logEachParam.add(tf);
		}

		return logEachParam;
	}



	/**
	 * Read the configuration file, tab delimited 10 parameters at moment, all
	 * hyper prior
	 * 
	 * Gelfile total ite, thinning globalout localout prior
	 * 
	 * @param configFile
	 * @return
	 * 
	 * 
	 */
	private ArrayList<double[]> readConfig(String configFile) {

		path = configFile.substring(0, configFile.lastIndexOf(FILE_SEP) + 1);
		System.out.println("coinfig file:\t" + configFile);
		ArrayList<double[]> config = new ArrayList<double[]>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(configFile));

			readGelData(in.readLine());
			setIte(in.readLine());
			setOutfile(in.readLine());

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	private void setOutfile(String s) {

		String[] outfile = s.split("\\s");
		this.globalOutFile = path + outfile[0];
		this.localOutFile = path + outfile[1];
		System.out.println("Global outifle: "+globalOutFile +"\tLocal outfile: "+localOutFile);
	}

	private void setIte(String s) {

		int[] mcmcInfo = stringToInt(s.split("\\s"));
		this.totalIte = mcmcInfo[0];
		this.thinning = mcmcInfo[1];
		System.out.println("Total Ite: "+totalIte +"\tthinning: "+thinning);
	}

	/**
	 * Read input data
	 * 
	 * @param gelFile
	 * @return
	 */
	private void readGelData(String gelFileName) {

		String gelFile = path.concat(gelFileName);
		System.out.println("Gel File:\t" + gelFile);
		// ArrayList<Spot> allSpots = new ArrayList<Spot>();
		try {

			BufferedReader in = new BufferedReader(new FileReader(gelFile));

			String input = in.readLine();

			while ((input = in.readLine()) != null) {
				Spot s = new Spot(input);
				allSpots.add(s);
			}
			allSpots.size();

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return allSpots;
	}

	private static double[] stringToDouble(String[] fields) {

		double[] temp = new double[fields.length];
		for (int i = 0; i < fields.length; i++) {
			temp[i] = Double.parseDouble(fields[i]);
		}
		return temp;
	}

	private static int[] stringToInt(String[] fields) {

		int[] temp = new int[fields.length];
		for (int i = 0; i < fields.length; i++) {
			temp[i] = Integer.parseInt(fields[i]);
		}
		return temp;
	}

	/**
	 * find minimum value
	 * 
	 * @param allSpot
	 * @return
	 */
	private static double findMin(ArrayList<Spot> allSpot) {
		double min = 0;
		double newMin = 0;
		for (Spot spot : allSpot) {
			newMin = spot.findMin();
			min = (min < newMin) ? min : newMin;
		}
		return min;
	}

}
