/*******************************************************************************
 * SaveParGlobal.java
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


/**
 * @author steven
 * 
 */
public class SaveParGlobal extends AbstractSavePar  {

//
//
//	private static final int INDEX_MU = GlobalPar.INDEX_MU;
//	private static final int INDEX_LAMBDA = GlobalPar.INDEX_LAMBDA;
//	private static final int INDEX_PI = GlobalPar.INDEX_PI;
//	private static final int INDEX_RHO = GlobalPar.INDEX_RHO;
//
//	private static final int INDEX_PI_SD = GlobalPar.INDEX_PI_SD;
//	private static final int INDEX_RHO_SD = GlobalPar.INDEX_RHO_SD;
//
//	private static final int INDEX_PI_ALPHA = GlobalPar.INDEX_PI_ALPHA;
//	private static final int INDEX_RHO_ALPHA = GlobalPar.INDEX_RHO_ALPHA;
//	private static final int INDEX_MU_ALPHA = GlobalPar.INDEX_MU_ALPHA;
//	private static final int INDEX_SPOT_SCALE = GlobalPar.INDEX_SPOT_SCALE;

//	private static final int[] INDEX_PARAM = { 
//			ParGlobal.INDEX_MU,	ParGlobal.INDEX_LAMBDA, 
//			ParGlobal.INDEX_PI, ParGlobal.INDEX_RHO,
//			ParGlobal.INDEX_PI_SD, ParGlobal.INDEX_RHO_SD,
//			ParGlobal.INDEX_PI_ALPHA, ParGlobal.INDEX_RHO_ALPHA, ParGlobal.INDEX_MU_ALPHA,
//			ParGlobal.INDEX_SPOT_SCALE
//		};
	protected static final int NO_TUNE = NO_GLOBAL_TUNE;
//	private static final int NO_PAR = Parameter.NO_GLOBAL_PAR;


	

	public SaveParGlobal(int n) {

		tunesize = ++n;
		state = new int[tunesize];

		init(NO_TUNE);
		

	}

	@Override
	public double[] calAccRate(int size) {

		double[] accRate = new double[NO_TUNE];
		size -= 1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < accRate.length; j++) {
				if (allPar[i][j] != allPar[i + 1][j]) {
					accRate[j]++;
				}
			}
//			if (allPar[i][INDEX_MU] != allPar[i + 1][INDEX_MU]) {
//				accRate[0]++;
//			}
//			if (allPar[i][INDEX_LAMBDA] != allPar[i + 1][INDEX_LAMBDA]) {
//				accRate[1]++;
//			}
//			if (allPar[i][INDEX_PI] != allPar[i + 1][INDEX_PI]) {
//				accRate[2]++;
//			}
//			if (allPar[i][INDEX_RHO] != allPar[i + 1][INDEX_RHO]) {
//				accRate[3]++;
//			}
//			if (allPar[i][INDEX_PI_SD] != allPar[i + 1][INDEX_PI_SD]) {
//				accRate[4]++;
//			}
//			if (allPar[i][INDEX_RHO_SD] != allPar[i + 1][INDEX_RHO_SD]) {
//				accRate[5]++;
//			}
//			if (allPar[i][INDEX_PI_ALPHA] != allPar[i + 1][INDEX_PI_ALPHA]) {
//				accRate[6]++;
//			}
//			if (allPar[i][INDEX_RHO_ALPHA] != allPar[i + 1][INDEX_RHO_ALPHA]) {
//				accRate[7]++;
//			}
//			if (allPar[i][INDEX_MU_ALPHA] != allPar[i + 1][INDEX_MU_ALPHA]) {
//				accRate[8]++;
//			}
//			if (allPar[i][INDEX_SPOT_SCALE] != allPar[i + 1][INDEX_SPOT_SCALE]) {
//				accRate[9]++;
//			}
						
		}

		for (int i = 0; i < accRate.length; i++) {
			accRate[i] /= size;
		}
		resetCount();
		return accRate;
	}




}
