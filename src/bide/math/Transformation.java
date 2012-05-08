/*******************************************************************************
 * Transformation.java
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

public class Transformation {

	/**
	 * transform probability to real number by log(p/(1-p))
	 * @param probability [0,1]
	 * @return real number
	 */
	public static double logit(double p) {
		
		double y = Math.log(p / (1.0-p) );
		return y;
		
	}
	
	/** 
	 * transform real number to probability [0, 1]
	 * @param real number
	 * @return probability [0,1]
	 */
	public static double invLogit(double y) {
		double t = Math.exp(y);
		double p = t/(1.0+t);
		return p;
	}
}
