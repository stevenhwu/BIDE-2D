/*******************************************************************************
 * PriorInvGamma.java
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

import bide.math.GammaDistribution;

public class PriorInvGamma implements PriorDist {

	private double shape;
	private double rate;
	private double scale;
	
	public PriorInvGamma(double shape, double rate){
		this.shape = shape;
//		this.rate = rate;
		this.scale = 1/rate;
	}
	


	@Override
	public double getLogPrior(double x) {
		return GammaDistribution.logPdf(1.0/x, shape, scale);
	}


	public double pdf(double x) {
		return GammaDistribution.pdf(1.0/x, shape, scale);
	}


	@Override
	public String getPriorName() {
		return "Inverse Gamma";
	}

	@Override
	public double logPdf(double x) {
		return logPdf(x);
	}

}
