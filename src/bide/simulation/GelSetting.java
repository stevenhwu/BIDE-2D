/*******************************************************************************
 * GelSetting.java
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

import bide.math.Constant;
import bide.math.RandomNumberGenerator;

public class GelSetting {

	static RandomNumberGenerator rd = new RandomNumberGenerator(System.currentTimeMillis());

	RealPar rp;
	int n;
	double limDet;

	public GelSetting(int n) {
		this.n = n;

	}

	public GelSetting(int n, double mu, double d, double pi, double rho, double sd) {
		this.n = n;
		rp = new RealPar(mu, d, pi, rho, sd);
	}

	public GelSetting(int n, double mu, double d, double pi, double rho,
			double sd, boolean isGlobal) {
		this.n = n;
		rp = new RealPar(mu, d, pi, rho, sd);
		if (isGlobal) {
			rp.setP(pi, rho);
		}

	}

	public void gelSetP(double p1, double p2) {

		rp.setP(p1, p2);
	}

	public double[][] generateSpot() {

		double[][] spots = new double[2][n];
		double s2 = Math.pow(rp.getSd(), 2);
		spots[0] = generateVol(n, rp.getU1(), rp.getP1(), s2);
		spots[1] = generateVol(n, rp.getU2(), rp.getP2(), s2);
		
		return spots;
	}

	public double[][] generateSpotSameMissing() {

		double[][] spots = new double[2][n];
		double s2 = Math.pow(rp.getSd(), 2);
		if(rp.getP1() != rp.getP2()){
			System.out.println("!!!! different Prob:\t"+rp.getP1() +"\t"+ rp.getP2());
		}
		spots[0] = generateVol(n, rp.getU1(), rp.getP1(), s2);
		spots[1] = generateVol(rp.getU2(), spots[0], s2);

		return spots;
	}
	
	public static String printGel(int index, double[][] spots) {

		int n = spots[0].length;
		double[] spot = new double[n * 2];
		for (int i = 0; i < n; i++) {
			spot[i] = spots[0][i];
			spot[i + n] = spots[1][i];

		}
		StringBuilder sb = new StringBuilder();
		sb.append(index).append("\t");
		for (int i = 0; i < spot.length; i++) {
			sb.append(spot[i]).append("\t");
		}

		return sb.toString().trim();
	}

	public static String printGel(double[][] spots) {

		int n = spots[0].length;
		double[] spot = new double[n * 2];
		for (int i = 0; i < n; i++) {
			spot[i] = spots[0][i];
			spot[i + n] = spots[1][i];

		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < spot.length; i++) {
			sb.append(spot[i]).append("\t");
		}

		return sb.toString().trim();
	}

	private double[] generateVol(int n, double u, double p, double s2) {

		double s[] = new double[n];
		for (int i = 0; i < n; i++) {
			if (rd.nextBoolean(p)) {
				s[i] = simValue(u, s2);
			} else {
				s[i] = Double.NaN;
			}
		}

		return s;
	}

	private double[] generateVol(double u, double[] p, double s2) {

		double s[] = new double[n];
		for (int i = 0; i < p.length; i++) {
			
			if (!Double.isNaN( p[i] ) ){
				
				s[i] = simValue(u, s2);
			} else {
				s[i] = Double.NaN;
			}
		}

		return s;
	}
	private double simValue(double u, double s2) {
		double v = rd.nextGaussian(u, s2);
		while (v > Constant.GEL_MAX) {
			v = rd.nextGaussian(u, s2);
		}
		if (v < limDet) {
			v = Double.NaN;
		}
		return v;
	}

	public void setLimDet(double lim) {
		limDet = lim;
	}

	public String createLabel() {

		StringBuilder sb = new StringBuilder("SpotNo");
		for (int i = 1; i < n + 1; i++) {
			sb.append("\t").append("Co").append(i);
		}
		for (int i = 1; i < n + 1; i++) {
			sb.append("\t").append("Ca").append(i);
		}
		return sb.toString();
	}

}
