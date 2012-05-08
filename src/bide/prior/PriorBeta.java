/*******************************************************************************
 * PriorBeta.java
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
package bide.prior;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.special.Gamma;

public class PriorBeta implements PriorDist {

	BetaDistribution bDist;

	/** First shape parameter. */
	private double alpha;

	/** Second shape parameter. */
	private double beta;

	/**
	 * Normalizing factor used in density computations. updated whenever alpha
	 * or beta are changed.
	 */
	private double z;

	/**
	 * Build a new instance.
	 * 
	 * @param alpha
	 *            first shape parameter (must be positive)
	 * @param beta
	 *            second shape parameter (must be positive)
	 */

	public PriorBeta(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
		z = Double.NaN;
		bDist = new BetaDistribution(alpha, beta);

	}

	@Override
	public double logPdf(double x) {
		return logPdf(x, alpha, beta);
	}

	public static double logPdf(double x, double alpha, double beta){
	
		double z = recomputeZ(alpha, beta);

		double logX = Math.log(x);
		double log1mX = Math.log1p(-x);
		return ((alpha - 1) * logX + (beta - 1) * log1mX - z);
	}

	/**
	 * Recompute the normalization factor.
	 */
	private void recomputeZ() {
		if (Double.isNaN(z)) {
			z = Gamma.logGamma(alpha) + Gamma.logGamma(beta)
					- Gamma.logGamma(alpha + beta);
		}
	}
	
	private static double recomputeZ(double alpha, double beta) {
        
        return ( Gamma.logGamma(alpha) + Gamma.logGamma(beta) - Gamma.logGamma(alpha + beta) );
        
    }

	public double pdf(double x) {
		return pdf(x,alpha,beta);

	}

	public static double pdf(double x, double alpha, double beta) {
		double z = recomputeZ(alpha, beta);

		double logX = Math.log(x);
		double log1mX = Math.log1p(-x);
		return Math.exp((alpha - 1) * logX + (beta - 1) * log1mX - z);

	}

	@Override
	public double getLogPrior(double x) {

		return logPdf(x);
	}

	@Override
	public String getPriorName() {

		return "Beta";
	}


}
