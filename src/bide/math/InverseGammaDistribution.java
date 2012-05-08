/*******************************************************************************
 * InverseGammaDistribution.java
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
 * inverse gamma function
 * 
 */
@Deprecated
public class InverseGammaDistribution implements Distribution{

	private double shape;
	private double scale;


	
	public InverseGammaDistribution() {

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
	public void setParam(double shape2, double scale2) {
		this.shape = shape2;
		this.scale = scale2;
		
	}

	/*
	 * ###################################################################### #
	 * Method pinvgamma # lower.tail=T pinvgamma<- function(x,shape,scale){
	 * 
	 * return(1 - pgamma(1/x, shape, scale)) }
	 * 
	 * ###################################################################### #
	 * Method rinvgamma # random generation from the inverse gamma distribution
	 * 
	 * rinvgamma<-function (n, shape, scale){ return(1/rgamma(n, shape, scale)) }
	 * 
	 * 
	 * ###################################################################### #
	 * Method dinvgamma # density function from the inverse gamma distribution #
	 * require(MCMCpack) dinvgamma<-function (x, shape, scale, log=T){
	 * 
	 * density <- shape * log(scale) - lgamma( shape) - ( shape + 1) * log(x) -
	 * scale/x if(log==F){ density=exp(density) } return(density) }
	 */
	public double mean(double shape, double scale) {
		return scale / (shape - 1);
	}

	@Override
	public double mean() {
		return mean(shape, scale);
	}

	public double variance(double shape, double scale) {
		double shape1 = shape - 1;
		double shape2 = shape1 - 1;
		return scale * scale / (shape1 * shape1 * shape2);
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
	 * @param scale
	 *            scale parameter
	 * 
	 * @return pdf value
	 */

	@Override
	public double pdf(double x) {

		return pdf(x, shape, scale);
	}


	public static double pdf(double x, double shape, double scale) {

		if (x <= 0) {
			return Double.NEGATIVE_INFINITY;
		}

		return Math.exp(logPdf(x,shape,scale));
	}
	/**
	 * the natural log of the probability density function of the distribution
	 * 
	 * @param x
	 *            argument
	 * @param shape
	 *            shape parameter
	 * @param scale
	 *            scale parameter
	 * 
	 * @return log pdf value
	 */
	@Override
	public double logPdf(double x) {

		return logPdf(x, shape, scale);
	}

	public static double logPdf(double x, double shape, double scale) {

		// AR - changed this to return -ve inf instead of throwing an
		// exception... This makes things
		// much easier when using this to calculate log likelihoods.
		if (x <= 0) {
			return Double.NEGATIVE_INFINITY;
		}

		return shape * Math.log(scale) - GammaFunction.lnGamma(shape)
				- (shape + 1) * Math.log(x) - scale / x;
	}


	//@Override
	//public double cdf(double x) {
		
	//	return 0;
	//}

}
