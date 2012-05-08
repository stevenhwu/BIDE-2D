/*******************************************************************************
 * Setting.java
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
@SuppressWarnings("unused")
public class Setting {

	public static final String LOCAL = "LOCAL";
	public static final String GLOBAL = "GLOBAL";
	
	private static double priorMuMu;
	private static double priorMuSd;
	private static double priorMuShape;
	private static double priorMuScale;
	
	private static double priorLambdaShape; 
	private static double priorLambdaScale;
	private static double phiLower;
	private static double phiUpper;

	private static double gapLower = -2;
	private static double gapUpper = 2;
	
	
	private static double priorProbMu;
	private static double priorProbSd;
	private static double priorProbShape;
	private static double priorProbScale;
	
	private static double normalSd;
	private static double normalMean;
	


//	public static void useDefault(){
//		
//		
//		setupPriorMu(0, 5);
//		setupPriorSd(0.01, 0.01, 5);
//
//		setGap(2);
//		setupPriorExp(0.01, 100);
//		setupPhiRange(0.01, 0.99);
//		
//		setupProbMu(0, 5);
//		setupProbSd(0.01, 0.01);
//		
//	}
	
	public static void setGap(double gap) {
		gapLower = -gap;
		gapUpper = gap;
	}
		
	public static void setupNormal(double m, double sd) {
		normalMean = m;
		normalSd = sd;
		
	}

	public static void setupPriorMu(double m, double sd) {
		priorMuMu = m;
		priorMuSd = sd;
		
	}
	
	public static void setupPriorSd(double shape, double scale){
		priorMuShape = shape;
		priorMuScale = scale;
		
	}

	public static void setupPriorExp(double shape, double scale) {
		priorLambdaShape = shape;
		priorLambdaScale = scale;
		
	}

	
	public static void setupPhiRange(double lower, double upper){
		if(lower <=0 ){
			lower = 1E-2;
		}
		if(upper >=1 ){
			upper = 1-1E-2;
		}
		phiLower = lower;
		phiUpper = upper;
		
	}

	public static void setupProbMu(double m, double sd) {
		priorProbMu = m;
		priorProbSd = sd;
		
	}
	
	public static void setupProbSd(double shape, double scale) {
		priorProbShape = shape;
		priorProbScale = scale;
		
	}


	public static double getPriorMuMu() {
		return priorMuMu;
	}

	public static double getPriorMuSd() {
		return priorMuSd;
	}

	public static double getPriorMuShape() {
		return priorMuShape;
	}

	public static double getPriorMuScale() {
		return priorMuScale;
	}
	
	public static double getPriorLambdaShape() {
		return priorLambdaShape;
	}

	public static double getPriorLambdaScale() {
		return priorLambdaScale;
	}

	public static double getPhiLower() {
		return phiLower;
	}

	public static double getPhiUpper() {
		return phiUpper;
	}

	public static double getGap() {
		return gapUpper;
	}
	public static double getGapUpper() {
		return gapUpper;
	}

	public static double getGapLower() {
		return gapLower;
	}
	

	public static double getPriorProbMu() {
		return priorProbMu;
	}

	public static double getPriorProbSd() {
		return priorProbSd;
	}

	public static double getPriorProbShape() {
		return priorProbShape;
	}

	public static double getPriorProbScale() {
		return priorProbScale;
	}
	





	public static double[] getPriorSummary() {
		double[] priorSummary = {
		Setting.getPriorMuMu(),
		Setting.getPriorMuSd(),
		Setting.getPriorMuShape(),
		Setting.getPriorMuScale(),
		Setting.getPriorLambdaShape(),
		Setting.getPriorLambdaScale(),
		Setting.getPhiLower(),
		Setting.getPhiUpper(),
		Setting.getPriorProbMu(),
		Setting.getPriorProbSd(),
		Setting.getPriorProbShape(),
		Setting.getPriorProbScale() };
		return priorSummary;
	}



	

}
