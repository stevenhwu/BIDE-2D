/*******************************************************************************
 * MainAnalysisResultHPD.java
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
package bide.hpd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.common.primitives.Doubles;

public class MainAnalysisResultHPD {
	

	public static void main(String[] args) {
	
		
		String cwd = System.getProperty("user.dir")+File.separator;
		
		analysisOne(cwd+"example"+File.separator);

	}

	private static void anasysisThreeSimulation3(String string) {
		String cwd = string;
		String logFileName;

		String[] sims = {  
				"simUnifProb/",
				"simReal/",      
				"simTwoProb/"};
		for (int i = 0; i < sims.length; i++) {
			String s = sims[i];
			logFileName = "Spot_d.log";
			readLogFile(cwd, s+logFileName);
			logFileName = "Spot_rho.log";
			readLogFile(cwd, s+logFileName);
		}
		
		
	}

	private static void analysisSingle(String string) {
		String cwd = string;
		String logFileName;

		String[] sims = {
				"simProbDown/",
				"simDiffProb/",
				"simMix2/"};
		for (int i = 0; i < sims.length; i++) {
			String s = sims[i];
			logFileName = "Spot_pi.log";
			readLogFile(cwd, s+logFileName);
			logFileName = "Spot_likleihood.log";
					       
			readLogFile(cwd, s+logFileName);
		}
		
	}


	private static void analysisOne(String string) {
		String cwd = string;
		String logFileName;
		logFileName = "Spot_d.log";
		readLogFile(cwd, logFileName);
		logFileName = "Spot_rho.log";
		readLogFile(cwd, logFileName);

		
	}

	private static void readLogFile(String cwd, String logFileName) {

		String logFile = cwd+logFileName;
		System.out.println("Gel File:\t" + logFile);
		try {

			BufferedReader in = new BufferedReader(new FileReader(logFile));
			FileWriter fout = new FileWriter(logFile+"_result.tab");
			String input = in.readLine();
			
			String name = in.readLine();//.split("\t");
			String[] names = name.split("\t");
			
			StringTokenizer token = new StringTokenizer(name);

			int noSpot = token.countTokens();
			ArrayList<Double>[] dists = new ArrayList[noSpot];
			for (int i = 0; i < dists.length; i++) {
				dists[i] = new ArrayList<Double>();
			}
			
			System.out.println("No. of spots:\t"+dists.length);
			
			while ((input = in.readLine()) != null) {
				token = new StringTokenizer(input);
				token.nextToken(); //skip noIte
				for (int i = 0; i < noSpot; i++) {
					dists[i].add(Double.parseDouble(token.nextToken()))	;
				}

			}
			
			StringBuilder sb = new StringBuilder();
			int noSignificant = 0;
			for (int i = 0; i < dists.length; i++) {
				double[] values = Doubles.toArray( dists[i]);
				TraceDistribution td = new TraceDistribution(values, 0.95, 0.1);
				double hpdlower = td.getLowerHPD();
				double hpdupper = td.getUpperHPD();
				boolean isGood = hpdlower > 0 || hpdupper < 0; 
				if(isGood){
					noSignificant++;
				}
				sb.append(names[i]).append("\t").append(hpdlower).append("\t").append(hpdupper).
					append("\t").append(isGood).append("\n");
			
			}
			System.out.println("No. of differentially expressed spots:\t"+noSignificant);
			
			fout.write(sb.toString());
			
			in.close();
			fout.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
