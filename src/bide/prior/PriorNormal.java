/*******************************************************************************
 * PriorNormal.java
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

import bide.math.NormalDistribution;

public class PriorNormal implements PriorDist{

	private double mean;
	private double sd;

	
	public PriorNormal(double mean, double sd){
		this.mean=mean;
		this.sd = sd;
		
	}
	
	public PriorNormal() {
		
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}

	@Override
	public double getLogPrior(double x) {

		return NormalDistribution.logPdf(x, mean, sd);
	}


	public double pdf(double x) {
		return NormalDistribution.pdf(x, mean, sd);
	}



	@Override
	public String getPriorName() {
		
		return "Normal";
	}

	public double getMean() {
		return mean;
	}

	public double getSd() {
		return sd;
	}

	public static double getLogPrior(double x, double mean, double sd) {
		return NormalDistribution.logPdf(x, mean, sd);
	}

	@Override
	public double logPdf(double x) {
		return NormalDistribution.logPdf(x, mean, sd);
	}



	
	
}

