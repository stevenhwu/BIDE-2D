/*******************************************************************************
 * MHRatio.java
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
package bide.core;

import java.util.Random;



/**
 * @author steven calculate the Metropolis-Hastings accepting ratio
 */
public class MHRatio {

	/**
	 * @param theta0
	 *            P(theta | theta_proposed)
	 * @param theta1
	 *            p(theta_proposed | theta)
	 * @param prob0
	 *            P(theta | data)
	 * @param prob1
	 *            p(theta_proposed | data)
	 * @return
	 */


	public static boolean acceptTemp(double theta0, double theta1, double post0,
			double post1, double temperature) {

		double alpha = post1 - post0 + theta0 - theta1;
		alpha = alpha * temperature;

		if (alpha > 0)
			alpha = 0.0;
		boolean accept = nextLogDouble() < alpha;
	
		return accept;

	}
	
	public static boolean accept(double theta0, double theta1, double post0,
			double post1) {

		double alpha = post1 - post0 + theta0 - theta1;
		if (alpha > 0)
			alpha = 0.0;
		boolean accept = nextLogDouble() < alpha;
	
		return accept;

	}
	public static boolean accept(double post0,	double post1) {

	
		return accept(0, 0 ,post0, post1);

	}
	
	static Random rand = new Random();
	public static double nextLogDouble(){
		
		return ( Math.log( rand.nextDouble() ) );
	}
}
