/*******************************************************************************
 * PriorDist.java
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




public interface PriorDist{

	public final static String PRIOR = "prior";
	public final static String UNIFORM = "uniform";
	public final static String TRUE = "true";
 
		/**
		 * @param 
		 * @return the log prior of some aspect of the given value
		 */
		public double getLogPrior(double x);

		/**
		 * Returns the logical name of this prior. 
		 * @return the logical name of this prior.
		 */
		public String getPriorName();
		
		public double logPdf(double x);
	
	
}
