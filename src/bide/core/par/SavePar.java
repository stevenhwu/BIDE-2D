/*******************************************************************************
 * SavePar.java
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

public interface SavePar {


	public final int NO_LOCAL_TUNE = 2;
	public final int NO_GLOBAL_TUNE = 8;

	@Override
	public abstract String toString();

	public abstract int getCount();

	public abstract double[][] getAllPar();

	public abstract double[] calAccRate(int size);

	

	public abstract void addPar(Parameter gp, int ite);

	public abstract int[] getState();

	public abstract void resetCount();

	void init(int noTune);;



}
