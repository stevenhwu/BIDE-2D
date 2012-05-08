/*******************************************************************************
 * SaveParGlobalTest.java
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

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bide.core.par.ParGlobal;
import bide.core.par.SavePar;
import bide.core.par.SaveParGlobal;




public class SaveParGlobalTest {


	private SaveParGlobal savePG;
	
	private ParGlobal gp;
//	private Likelihood li;
//	private ArrayList<Spot> allSpot;
//	private ParSpot[] spArray;
//	private ParSpot spTest;
	private double[] expecteds;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {

		savePG = new SaveParGlobal(10);		
	}

	@Test
	public void testSaveParGlobal() {

		double[][] temp = savePG.getAllPar();
		assertEquals(11, temp.length, 0);

		for (int i = 0; i < temp.length; i++) {
			assertEquals(SavePar.NO_GLOBAL_TUNE, temp[i].length);
		}

	}

	@Test
	public void testAdd() {
	
		ParGlobal spTest = new ParGlobal(-10);
		savePG.addPar(spTest, 10);
		spTest = new ParGlobal(-10);
		savePG.addPar(spTest, 20);
		
		double[][] temp = savePG.getAllPar();
		assertNotSame(temp[0], temp[1]);
		
//		expecteds = new double[] {1, 2, 0.3, 0.2};
//		assertArrayEquals(expecteds, temp[0], 0);
//		expecteds = new double[] {2, 3, 1.3, 1.2};
//		assertArrayEquals(expecteds, temp[1], 0);
		
		int[] count = savePG.getState();
		int[] expectedInt = new int[] {10, 20, 0,0,0,0,0,0,0,0,0};
		assertArrayEquals(expectedInt, count);
		
	}



	
	@Test
	public void testCalAccRate() throws Exception {
		 
		savePG.addPar(new ParGlobal(-10), 0);
		savePG.addPar(new ParGlobal(-10), 0);
		savePG.addPar(new ParGlobal(-10), 0);
		savePG.addPar(new ParGlobal(-10), 0);
		savePG.addPar(new ParGlobal(-10), 0);
		
		
		double[][] t = savePG.getAllPar();

		double[] expecteds = {1,1,1,1,0,0,0,1};
		
		assertArrayEquals(expecteds, savePG.calAccRate(2), 0);
		
//		expecteds = new double[]{0.4, 0.6};
		assertArrayEquals(expecteds, savePG.calAccRate(5), 0);

		expecteds = new double[]{0.5,0.5,0.5,0.5,0.1,0.1,0.1,0.5};
		assertArrayEquals(expecteds, savePG.calAccRate(11), 0);
	}

}
