/*******************************************************************************
 * CurrentParTest.java
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
package test.core.par;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bide.core.Likelihood;
import bide.core.par.CurrentPar;
import bide.core.par.ParGlobal;
import bide.core.par.Parameter;
import bide.core.par.Spot;
import bide.core.par.TunePar;

public class CurrentParTest {

	private static ParGlobal gp;
	private static Parameter[] sp;
	private static Likelihood li;
	static CurrentPar cp;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		int n = 10;
		double limDet = -10;
		ArrayList<Spot> allSpots = Spot.generateList(n, 6);

		cp = new CurrentPar(allSpots, limDet);
		
	}

	@Before
	public void setUp() throws Exception {
	}

//	@Test
	public void testCurrentParIntDouble() {
		fail("Not yet implemented");
	}


	@Test
	public void testInit() {
		String lString = cp.getLikelihood().toString();
		cp.init();
		assertNotSame(lString,cp.getLikelihood().toString());
	}

	@Test
	public void testUpdateGlobal() {

		int ite = 1000;
		String lString = cp.getLikelihood().toString();
		TunePar gtp = new TunePar("", 100, 10, 10);
		double[] tp = new double[10];
		Arrays.fill(tp, 1);
		for (int i = 0; i < ite; i++) {
			cp.updateGlobal(gtp);		
		}
		assertFalse(  lString.equals(cp.getLikelihood().toString()) );

	}
	

}


