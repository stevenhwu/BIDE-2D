/*******************************************************************************
 * PriorUniform.java
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

import bide.math.UniformDistribution;


public class PriorUniform implements PriorDist {

	private double lower;
	private double upper;
	
	public PriorUniform(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	public PriorUniform(){
		lower = 0;
		upper = 1;
	}
	public double getLogPrior(double x, double y){
		return getLogPrior(x)+getLogPrior(y);
	}
	@Override
	public double getLogPrior(double x) {
		return UniformDistribution.logPdf(x, lower, upper);
	}

	@Override
	public String getPriorName() {
		return "Uniform";
	}
	public double getLower() {
		return lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	public double getUpper() {
		return upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}

	@Override
	public double logPdf(double x) {
		return getLogPrior(x);
	}



}


