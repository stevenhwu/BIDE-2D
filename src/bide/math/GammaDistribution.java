/*******************************************************************************
 * GammaDistribution.java
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

/**
 * gamma distribution.
 * 
 * (Parameters: shape, scale; mean: scale*shape; variance: scale^2*shape)
 * 
 * @version $Id: GammaDistribution.java,v 1.9 2006/03/30 11:12:47 rambaut Exp $
 * 
 * @author Korbinian Strimmer
 */
public class GammaDistribution implements Distribution{

	private double shape;
	private double scale;
	private RandomNumberGenerator rg = new RandomNumberGenerator();

	public GammaDistribution() {
	}

	public GammaDistribution(double shape, double scale) {
		this.shape = shape;
		this.scale = scale;
	}

	public double getShape() {
		return shape;
	}

	public void setShape(double value) {
		shape = value;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double value) {
		scale = value;
	}

	public void setParam(double mean, double sd) {

		double t = mean / sd;
		shape = t * t;
		scale = mean / shape;
	}

	public double mean(double shape, double scale) {
		return scale * shape;
	}

	@Override
	public double mean() {
		return mean(shape, scale);
	}

	public double variance(double shape, double scale) {
		return scale * scale * shape;
	}

	@Override
	public double variance() {
		return variance(shape, scale);
	}

	/**
	 * probability density function of the Gamma distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param rate
	 *            scale parameter
	 * 
	 * @return pdf value
	 */

	@Override
	public double pdf(double x) {

		return pdf(x, shape, scale);
	}

	public static double pdf(double x, double shape, double scale) {
		// return Math.pow(scale,-shape)*Math.pow(x, shape-1.0)/
		// Math.exp(x/scale + GammaFunction.lnGamma(shape));
		if (x < 0)
			throw new IllegalArgumentException();
		if (x == 0) {
			if (shape == 1.0)
				return 1.0 / scale;
			else
				return 0.0;
		}
		if (shape == 1.0) {
			return Math.exp(-x / scale) / scale;
		}

		double a = Math.exp((shape - 1.0) * Math.log(x / scale) - x / scale
				- GammaFunction.lnGamma(shape));

		return a / scale;
	}

	/**
	 * the natural log of the probability density function of the distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param rate
	 *            scale parameter
	 * 
	 * @return log pdf value
	 */
	@Override
	public double logPdf(double x) {

		return logPdf(x, shape, scale);
	}

	public static double logPdf(double x, double shape, double scale) {
		// double a = Math.pow(scale,-shape) * Math.pow(x, shape-1.0);
		// double b = x/scale + GammaFunction.lnGamma(shape);
		// return Math.log(a) - b;

		// AR - changed this to return -ve inf instead of throwing an
		// exception... This makes things
		// much easier when using this to calculate log likelihoods.
		// if (x < 0) throw new IllegalArgumentException();
		if (x < 0)
			return Double.NEGATIVE_INFINITY;

		if (x == 0) {
			if (shape == 1.0)
				return Math.log(1.0 / scale);
			else
				return Double.NEGATIVE_INFINITY;
		}
		if (shape == 1.0) {
			return (-x / scale) - Math.log(scale);
		}

		return ((shape - 1.0) * Math.log(x / scale) - x / scale - GammaFunction
				.lnGamma(shape))
				- Math.log(scale);
	}

	/**
	 * cumulative density function of the Gamma distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param rate
	 *            scale parameter
	 * 
	 * @return cdf value
	 */
	public double cdf(double x) {

		return cdf(x, shape, scale);
	}

	public static double cdf(double x, double shape, double scale) {
		return GammaFunction.incompleteGammaP(shape, x / scale);
	}

	/**
	 * random gamma distribution
	 * 
	 * @return
	 */
	public double randomDist() {

		return rg.nextGamma(shape, scale);
	}




}
