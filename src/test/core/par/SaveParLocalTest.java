/*******************************************************************************
 * SaveParLocalTest.java
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
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Splitter;
import bide.core.Likelihood;
import bide.core.par.ParSpot;
import bide.core.par.Parameter;
import bide.core.par.SavePar;
import bide.core.par.SaveParLocal;
import bide.core.par.Spot;

public class SaveParLocalTest {

	private double expected;
	private SaveParLocal savePL;
	
//	private ParGlobal gp;
	private Likelihood li;
	private ArrayList<Spot> allSpot;
	private Parameter[] spArray;
	private Parameter spTest;
	private double[] expecteds;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		int n = 10;
		
		savePL = new SaveParLocal(n);		
//		gp = new ParGlobal(-10);
		li = new Likelihood(n, -10);
		ArrayList<Spot> allSpot = Spot.generateList(n, 6);
//		spArray = ParSpot.init(-10, allSpot, gp, li);
		spTest = new ParSpot(1, 2, 0.3, 0.2, 0.5);
	}

	@Test
	public void testSaveParLocal() {

		double[][] temp = savePL.getAllPar();
		assertEquals(11, temp.length, 0);
		for (int i = 0; i < temp.length; i++) {
			assertEquals(SavePar.NO_LOCAL_TUNE, temp[i].length, 0);
		}

	}

	@Test
	public void testAdd() {
	
		savePL.addPar(spTest, 10);
		spTest = new ParSpot(2, 3, 1.3, 1.2, 1.5);
		savePL.addPar(spTest, 20);
		double[][] temp = savePL.getAllPar();
		
		expecteds = new double[] {1, 0.3 };
		assertArrayEquals(expecteds, temp[0], 0);
		expecteds = new double[] {2, 1.3};
		assertArrayEquals(expecteds, temp[1], 0);
		
		int[] count = savePL.getState();
		int[] expectedInt = new int[] {10, 20, 0,0,0,0,0,0,0,0,0};
		assertArrayEquals(expectedInt, count);
		
	}


	@Test
	public void testToString() {

		Iterable<String> split = Splitter.onPattern("\r?\n").split(savePL.toString());
		Iterator<String> iterator = split.iterator();
		
		String expected = "state\tmu\td\tpi\trho\tsd\tlikelihood";
		assertEquals(expected, iterator.next());
		expected = "0\t0.0000\t0.0000\t";
		
		while (iterator.hasNext()) {
			String s = iterator.next();
			if (!s.isEmpty()) {
				assertEquals(expected, s);
			}

		}
		
	}
	
	@Test
	public void testCalAccRate() throws Exception {
		 
		savePL.addPar(new ParSpot(1, 2, 3, 4, 5), 1);
		savePL.addPar(new ParSpot(1, 2, 3, 4, 5), 2);
		savePL.addPar(new ParSpot(1, 2, 3, 4, 5), 3);
		savePL.addPar(new ParSpot(1.4, 2.4, 3.4, 4.4, 5), 4);
		savePL.addPar(new ParSpot(1.5, 2.5, 3.5, 4.5, 5), 5);
		savePL.addPar(new ParSpot(1.5, 2.5, 3.6, 4.6, 5), 6);		
		savePL.addPar(new ParSpot(1.6, 2.6, 3.7, 4.7, 5), 7);
		savePL.addPar(new ParSpot(1.6, 2.6, 3.8, 4.8, 5), 8);
		savePL.addPar(new ParSpot(1.0, 2.0, 3.9, 4.9, 5), 9);
		savePL.addPar(new ParSpot(1.0, 2.0, 3.10, 4.10, 5), 10);
		savePL.addPar(new ParSpot(1.0, 2.0, 3.11, 4.11, 5), 11);
		
		double[] expecteds = {0,0};
		assertArrayEquals(expecteds, savePL.calAccRate(2), 0);
		
		expecteds = new double[]{0.4, 0.6};
		assertArrayEquals(expecteds, savePL.calAccRate(6), 0);

		expecteds = new double[]{0.4, 0.8};
		assertArrayEquals(expecteds, savePL.calAccRate(11), 0);
	}

}
