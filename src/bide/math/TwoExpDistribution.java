/*******************************************************************************
 * TwoExpDistribution.java
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
package bide.math;



public class TwoExpDistribution implements Distribution {


	private double lambda;
	private double phi;
	private double oneMinusPhi;

	public TwoExpDistribution(double lambda, double phi) {

		updateDist(lambda, phi);
		// setLimit(gap);
	}

	//
	 public TwoExpDistribution() {
		 updateDist(1,0.5);
	 }

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getPhi() {

		return phi;
	}

	public void setPhi(double newPhi) {
		updateDist(lambda, newPhi);
		// proportionDE = (1 - phi) / 2;

	}


	public void updateDist(double lambda, double phi) {

		this.lambda = lambda;
		this.phi = phi;
		oneMinusPhi = 1 - phi;
		// proportionDE = (1 - phi) / 2;
	}

	@Override
	public double pdf(double x) {

		double p = 0;
		if (x > 0) {
			p = phi * ExponentialDistribution.pdf(x, lambda);
		} else {

			p = oneMinusPhi * ExponentialDistribution.pdf(-x, lambda);
		}
		return p;
	}

	@Override
	public double logPdf(double x) {
		return logPdf(x, lambda, phi);
	}

	@Override
	public double mean() {
		
		return Double.NaN;
	}

	@Override
	public double variance() {
	
		return Double.NaN;
	}


	public static double logPdf(double x, double lambda, double phi) {

		if (phi < 0 | phi > 1) {
			return Double.NEGATIVE_INFINITY;
		} else {

			double p;
			if (x > 0) {
				p = Math.log(phi) + ExponentialDistribution.logPdf(x, lambda);
			} else {
				p = Math.log(1 - phi)
						+ ExponentialDistribution.logPdf(-x, lambda);
			}
			
			return p;
		}
	}

}
