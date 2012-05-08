/*******************************************************************************
 * GlobalMLMain.java
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

import bide.simulation.Simulation;

/**
 * @author Steven Wu
 * Thanks beast-mcmc project for some very useful source code
 * 
 * Required library:
 * Apache commons-lang-3.*
 * Apache commons-math-3.*
 * 
 * Usage: java -jar bide.jar config.conf
 * Note: with large file -Xmx switch might be required
 * 
 */

public class GlobalMLMain {


	public static void main(String[] args) {

		/*		
		 * config file format
		 * 
		 * dataFileName
		 * TotalChainLength LogInterval
		 * GlobalParameterOutputFile LocalParameterOutputFile
		 * 
		 * e.g.
		 * example.csv
		 * 10000000 1000
		 * SpotGlobal.log SpotLocal.log
		 */

		String configFile = args[0];

		MCMC mcmcChain = new MCMC(configFile);
		mcmcChain.run();

	}


}
