/*******************************************************************************
 * Spot.java
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
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.stat.StatUtils;

import com.google.common.primitives.Doubles;

public class Spot {

	private static JDKRandomGenerator rand = new JDKRandomGenerator();
//	private static NormalDistribution nd = new NormalDistribution(0, 1);
	private String names;
	private double[] controlSpot;
	private double[] caseSpot;
	private double[] expressControlSpot;
	private double[] expressCaseSpot;
	private int numberOfSpot;
	private int numberOfCase;
	private int numberOfControl;
	
	public Spot(){}
	
	
	/**
	 * numberOfSpot = totalNumberOfCase = totalNumberOfControl
	 * @param s
	 */
	public Spot(String s){
		//str format  spotNo\tCO1\tCO2\t...\tCA1\tCA2
		//String[] oneSpot = s.replaceAll("NA","NaN").replaceAll("\t0.0","\tNaN").split("\t");
		String[] oneSpot = s.replaceAll("NA","NaN").split("\t");

		numberOfSpot = oneSpot.length/2;
		controlSpot = new double[numberOfSpot];
		caseSpot = new double[numberOfSpot];
		int n1 = numberOfSpot+1;
		names = oneSpot[0];
		for (int i = 0; i < numberOfSpot; i++) {
			controlSpot[i] = Double.parseDouble(oneSpot[i+1]);
			caseSpot[i] = Double.parseDouble(oneSpot[i+n1]);
			
		}
		expressControlSpot = formatSpot(controlSpot);
		expressCaseSpot = formatSpot(caseSpot);
		numberOfControl = expressControlSpot.length;
		numberOfCase = expressCaseSpot.length;

		
	}
	
	
	private double[] formatSpot(double[] spot){
		
		int express = 0;
		ArrayList<Double> expressSpot = new ArrayList<Double>(); 
		for (int i = 0; i < spot.length; i++) {
			if(!Double.isNaN(spot[i])){
				express++;
				expressSpot.add(spot[i]);				
			}
		}		
		double[] expSpot = ArrayUtils.toPrimitive(expressSpot.toArray(new Double[express]));
		return expSpot;
	}
	
	public void randomInit(int n, String string){
		
		numberOfSpot = n;
		
		controlSpot = new double[numberOfSpot];
		caseSpot = new double[numberOfSpot];
//		int n1 = numberOfSpot+1;
		names = string;
		for (int i = 0; i < numberOfSpot; i++) {
			controlSpot[i] = generateRandomValue();
			caseSpot[i] = generateRandomValue(); //nd.randomDist();
		}
		expressControlSpot = formatSpot(controlSpot);
		expressCaseSpot = formatSpot(caseSpot);
		numberOfControl = expressControlSpot.length;
		numberOfCase = expressCaseSpot.length;
		
	}

	private double generateRandomValue() {
		
		
		boolean isNa = rand.nextBoolean();
		if(isNa){
			return Double.NaN;
		}
		else{
			return rand.nextGaussian();
		}
	}


	public double[] getCase() {
		return caseSpot;
	}

	public double[] getControl() {
		return controlSpot;
	}

	public int getNumberOfSpot() {
		return numberOfSpot;
	}
	
	public String getName(){
		return names;
	}
	
	public double findMin(){
		return StatUtils.min(Doubles.concat(controlSpot, caseSpot));
//		return StatUtils.min(ArrayUtils.addAll(controlSpot,caseSpot));
		

	}
	@Override
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		sb.append( "SpotNumber: ").append(names).append("\n").
			append("Control: ").append(Arrays.toString(controlSpot)).append("\n").
			append("Case:    ").append(Arrays.toString(caseSpot)).append("\n");
		return sb.toString();
	}

	/**
	 * @return Returns the expressCaseSpot.
	 */
	public double[] getExpressCaseSpot() {
		return expressCaseSpot;
	}

	/**
	 * @return Returns the expressControlSpot.
	 */
	public double[] getExpressControlSpot() {
		return expressControlSpot;
	}

	/**
	 * @return Returns the numberOfCase.
	 */
	public int getNumberOfCase() {
		return numberOfCase;
	}

	/**
	 * @return Returns the numberOfControl.
	 */
	public int getNumberOfControl() {
		return numberOfControl;
	}


	public static ArrayList<Spot> generateList(int totalSpot, int spotPerGel) {
		ArrayList<Spot> allSpots = new ArrayList<Spot>();
		for (int i = 0; i < totalSpot; i++) {
			Spot s = new Spot();
			s.randomInit(spotPerGel, ""+i );
			s.randomInit(spotPerGel, Integer.toString(i) );
			allSpots.add(s);
		}
		return allSpots;
	}
}

