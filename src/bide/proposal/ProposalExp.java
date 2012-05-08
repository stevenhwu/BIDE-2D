/*******************************************************************************
 * ProposalExp.java
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
/**
 * 
 */
package bide.proposal;

import bide.math.ExponentialDistribution;

/**
 * @author steven
 * 
 */
public class ProposalExp implements ProposalDist {

	// private static ExponentialDistribution dist = new
	// ExponentialDistribution();

	/**
	 * @param x
	 * @param tune
	 * @return [0] new X [1] p(x | new X) at the top of the M-H ratio [2] p(new
	 *         X | x) at the bottom of the M-H ratio
	 * 
	 */
	public static double[] nextValue(double x) {

		double[] newX = new double[3];
		newX[0] = rNumber.nextExponential(1 / x);

		// p(x | newX)
		newX[1] = ExponentialDistribution.logPdf(x, newX[0]);

		// p(newX | x)
		newX[2] = ExponentialDistribution.logPdf(newX[0], x);

		return newX;

	}

}
