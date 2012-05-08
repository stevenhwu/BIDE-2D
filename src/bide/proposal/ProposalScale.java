/*******************************************************************************
 * ProposalScale.java
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
package bide.proposal;

public class ProposalScale implements ProposalDist {

	public static double[] nextValue(double x, double tune) {

		double scale = (tune + (rNumber.nextUniform(0, 1) * ((1.0 / tune) - tune)));
		double[] newX = { x * scale, 0, 0 };
		return newX;

	}

	public static double[] nextTruncatedValue(double x, double tune,
			double lower, double upper) {

		double[] newX = new double[3];

		do {

			double scale = (tune + (rNumber.nextUniform(0, 1) * ((1.0 / tune) - tune)));
			newX[0] = x * scale;

		} while (newX[0] > upper || newX[0] < lower);

		newX[1] = 0;
		newX[2] = 0;

		return newX;

	}

}
