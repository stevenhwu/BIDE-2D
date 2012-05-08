/*******************************************************************************
 * AbstractSavePar.java
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
package bide.core.par;

import java.util.Arrays;

public abstract class AbstractSavePar implements SavePar {

	protected int[] state;
	protected int count;
	protected double[][] allPar;
	protected int tunesize;
	
	
	
	@Override
	public void addPar(Parameter par, int ite) {

		state[count] = ite;
		allPar[count] = par.getTunePar();
		count++;

	}

	@Override
	public void resetCount() {
		count = 0;
	}

	@Override
	public int[] getState() {
		return state;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public double[][] getAllPar() {
		return allPar;
	}

	@Override
	public void init(int noTune) {
		allPar = new double[tunesize][noTune];
		resetCount();
	}
}
