/*******************************************************************************
 * SaveParLocal.java
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
package bide.core.par;

import java.util.Formatter;

/**
 * @author steven
 * 
 */
public class SaveParLocal extends AbstractSavePar {

	protected static final int NO_TUNE = NO_LOCAL_TUNE;
//	private static final int NO_PAR = Parameter.NO_LOCAL_PAR;

	public SaveParLocal(int n) {

		tunesize = ++n;
		state = new int[tunesize];

		init(NO_TUNE);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("state\tmu\td\tpi\trho\tsd\tlikelihood\n");
		Formatter f = new Formatter(sb);

		for (int i = 0; i < allPar.length; i++) {
			f.format("%d\t", state[i]);
			for (int j = 0; j < allPar[i].length; j++) {
				f.format("%.4f\t", allPar[i][j]);
			}
			f.format("\n");

		}
		return f.toString();

	}

	/**
	 * calculate the acceptance rate
	 * 
	 * @return
	 */
	@Override
	public double[] calAccRate(int size) {

		double[] accRate = new double[NO_TUNE];
		size -= 1;
		for (int i = 0; i < size; i++) {
			if (allPar[i][0] != allPar[i + 1][0]) {
				accRate[0]++;
			}
			if (allPar[i][1] != allPar[i + 1][1]) {
				accRate[1]++;
			}

		}
		for (int i = 0; i < accRate.length; i++) {
			accRate[i] /= size;
		}
		resetCount();
		return accRate;
	}



}
