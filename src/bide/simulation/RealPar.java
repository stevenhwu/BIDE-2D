/*******************************************************************************
 * RealPar.java
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
package bide.simulation;

import bide.core.par.ParSpot;
import bide.math.Transformation;


public class RealPar {

	private double u1;
	private double u2;
	private double p1;
	private double p2;
	private double sd;
	
	
	
	public RealPar(double mu, double d, double pi, double rho, double sd) {
		
		u1 = mu;
		u2 = mu+d;
		p1 = Transformation.invLogit(pi);
		p2 = Transformation.invLogit(pi+rho);
		this.sd = sd;
	}
	
	public RealPar(ParSpot sp) {
		
		u1 = sp.getMu1();
		u2 = sp.getMu2();
		p1 = Transformation.invLogit(sp.getPi());
		p2 = Transformation.invLogit(sp.getPi()+sp.getRho());
		sd = sp.getSd();
	}
	
	/**
	 * @return Returns the p1.
	 */
	public double getP1() {
		return p1;
	}


	/**
	 * @return Returns the p2.
	 */
	public double getP2() {
		return p2;
	}


	/**
	 * @return Returns the sd.
	 */
	public double getSd() {
		return sd;
	}


	/**
	 * @return Returns the u1.
	 */
	public double getU1() {
		return u1;
	}


	/**
	 * @return Returns the u2.
	 */
	public double getU2() {
		return u2;
	}

	public void setP(double p1, double p2){
		
		this.p1=p1;
		this.p2=p2;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("u1: ").append(u1).append("\tu2: ").append(u2)
			.append("\np1: ").append(p1).append("\tp2: ").append(p2)
			.append("\nsd: ").append(sd);
		
		return sb.toString();
	}
}
	
