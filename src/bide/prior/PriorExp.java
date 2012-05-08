/*******************************************************************************
 * PriorExp.java
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

import bide.math.ExponentialDistribution;

public class PriorExp implements PriorDist {

	private double lambda;

	public PriorExp(double lambda) {

		this.lambda = lambda;
	}

	public PriorExp() {
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getPrior(double x) {
		return ExponentialDistribution.logPdf(x, lambda);
	}

	public double getLogPrior(double x, double y) {
		return getLogPrior(x) + getLogPrior(y);
	}

	@Override
	public double getLogPrior(double x) {
		return ExponentialDistribution.logPdf(x, lambda);
	}

	@Override
	public String getPriorName() {
		return "Exponential";
	}

	@Override
	public double logPdf(double x) {

		return logPdf(x);
	}

}
