/*******************************************************************************
 * ParSpotTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bide.core.Likelihood;
import bide.core.par.ParGlobal;
import bide.core.par.ParSpot;
import bide.core.par.Spot;
import bide.math.Transformation;

public class ParSpotTest {

	private ParSpot sp;
	private double expected;

	private static ParGlobal gp;
	private static ParSpot spTest;
	private static Spot thisSpot;
	private static ParSpot[] spArray;
	private static Likelihood li;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		

		spTest = new ParSpot(1, 2, 0.3, 0.2, 0.5);
		thisSpot = new Spot("1	1	2	NA	3	NA	NA	");
	}

	@Before
	public void setUp() throws Exception {
		int n = 10;
		gp = new ParGlobal(-10);
		li = new Likelihood(n, -10);
		ArrayList<Spot> allSpot = Spot.generateList(n, 6);
		spArray = ParSpot.init(-10, allSpot, gp, li);

		gp.initCalcLikeli(spArray, li);
		sp = spArray[0];

	}

	@Test
	public void testParSpotDoubleDoubleDoubleDoubleDouble() {
		ParSpot sp = new ParSpot(1, 2, 0.3, 0.2, 0.5);
		assertEquals("GetMu1", 1, sp.getMu1(), 0);
		assertEquals("GetD", 2, sp.getD(), 0);
		assertEquals("GetMu2", 3, sp.getMu2(), 0);
		assertEquals("GetPi", 0.3, sp.getPi(), 0);
		assertEquals("GetRho", 0.2, sp.getRho(), 0);

		expected = Transformation.invLogit(sp.getPi());
		assertEquals("GetP1", expected, sp.getProb1(), 0);

		expected = Transformation.invLogit(sp.getPi() + sp.getRho());
		assertEquals("GetP2", expected, sp.getProb2(), 0);
	}

	@Test
	public void testSetD() {
		sp = new ParSpot(1, 2, 0.3, 0.2, 0.5);
		assertEquals("GetD", 2, sp.getD(), 0);
		sp.setD(3);
		assertEquals("GetMu1", 1, sp.getMu1(), 0);
		assertEquals("GetD", 3, sp.getD(), 0);
		assertEquals("GetMu2", 4, sp.getMu2(), 0);
	}

	@Test
	public void testSetMu1() {
		assertEquals("getMu1", 1, spTest.getMu1(), 0);
		spTest.setMu1(2);
		assertEquals("getMu1", 2, spTest.getMu1(), 0);
	}

	@Test
	public void testSetPi() {

		assertEquals("getPi", 0.3, spTest.getPi(), 0);

		spTest.setPi(0.6);
		assertEquals("getPi", 0.6, spTest.getPi(), 0);

		expected = Transformation.invLogit(0.6);
		assertEquals("getProb1", expected, spTest.getProb1(), 0);

		expected = Transformation.invLogit(0.8);
		assertEquals("getProb2", expected, spTest.getProb2(), 0);
	}

	@Test
	public void testSetRho() {
		spTest.setRho(0.1);
		assertEquals("getPi", 0.6, spTest.getPi(), 0);
		assertEquals("getRho", 0.1, spTest.getRho(), 0);

		expected = Transformation.invLogit(0.6);
		assertEquals("getProb1", expected, spTest.getProb1(), 0);

		expected = Transformation.invLogit(0.7);
		assertEquals("getProb2", expected, spTest.getProb2(), 0);
	}

	@Test
	public void testInit() throws Exception {
		gp = new ParGlobal(-5);
		ParSpot[] allSp = ParSpot.init(10, -5);
		assertEquals("D", allSp[0].getD(), allSp[0].getMu2()
				- allSp[0].getMu1(), 1E-10);

	}

	@Test
	public void testCalculatePrior() throws Exception {

		
		double[] tempPar = sp.tempParMuDLikeli(sp.getMu1(), sp.getD());
		expected = li.getEachLikelihood(0);
//		assertEquals("defaultLikeli", expected, sp.getLikelihood(),0);
		assertEquals("calLikeli", expected, li.calLogLikeli(sp.getSpot(),
				tempPar), 0);
		
		tempPar = sp.tempParPiRhoLikeli(sp.getPi(), sp.getRho() );
		assertEquals(expected, li.calLogLikeli(sp.getSpot(),
				tempPar), 0);
		tempPar = sp.tempParPiRhoPrior(sp.getPi(), sp.getRho() );
		assertEquals(sp.calculatePrior(gp), sp.calculatePrior(tempPar, gp), 0);
//		assertEquals(sp.getPriorLi(), sp.getPosterior() - sp.getLikelihood(), 10E-10);
//		assertEquals(sp.getPriorLi(), sp.calculatePrior( gp), 0);


	}

	@Test
	public void testTempPar() throws Exception {
		double[] oldPar = sp.getAllParLogOutput();
		double[] prob = ParSpot.calcProb(oldPar[2], oldPar[3]);
		double[][] tempPar = new double[2][];
		tempPar[0] = sp.tempParMuDLikeli(1, 2);
		tempPar[1] = sp.tempParMuDPrior(1, 2);
		assertEquals("mu1", 1, tempPar[0][0], 0);
		assertEquals("Prob1", prob[0], tempPar[0][1], 0);
		assertEquals("sd", sp.getSd(), tempPar[0][2], 0);
		assertEquals("mu2", 1 + 2, tempPar[0][3], 0);
		assertEquals("Prob2", prob[1], tempPar[0][4], 0);

		assertEquals("mu1", 1, tempPar[1][0], 0);
		assertEquals("d", 2, tempPar[1][1], 0);
		assertEquals("pi", oldPar[2], tempPar[1][2], 0);
		assertEquals("rho", oldPar[3], tempPar[1][3], 0);

		tempPar = new double[2][];
		tempPar[0] = sp.tempParPiRhoLikeli(5, -10);
		tempPar[1] = sp.tempParPiRhoPrior(5, -10);
		prob = ParSpot.calcProb(5, -10);
		assertEquals("mu1", oldPar[0], tempPar[0][0], 0);
		assertEquals("Prob1", prob[0], tempPar[0][1], 0);
		assertEquals("sd", sp.getSd(), tempPar[0][2], 0);
		assertEquals("mu2", oldPar[0] + oldPar[1], tempPar[0][3], 0);
		assertEquals("Prob2", prob[1], tempPar[0][4], 0);

		assertEquals("mu1", oldPar[0], tempPar[1][0], 0);
		assertEquals("d", oldPar[1], tempPar[1][1], 0);
		assertEquals("pi", 5, tempPar[1][2], 0);
		assertEquals("rho", -10, tempPar[1][3], 0);

	}

	@Test
	public void testUpdate() throws Exception {
		
		gp.setDefaultPrior();
		gp.initCalcLikeli(spArray, li);
		


		double[] allPar = spArray[0].getAllParLogOutput();
		double oldLikeli = li.getEachLikelihood(0);

		for (int i = 0; i < 500; i++) {

			spArray[0].updateMuD(gp, li, 1);
			spArray[0].updatePiRho(gp, li, 1);
			gp.updateDelta(spArray, li, 1);
			gp.updateMean(spArray, li, 1);
			gp.updateProb1(spArray, li, 1);
			gp.updateProb2(spArray, li, 1);

		}
		double[] allPar2 = spArray[0].getAllParLogOutput();
		for (int i = 0; i < allPar2.length; i++) {
			assertFalse(allPar[i] == allPar2[i]);
		}
		assertNotSame(allPar, allPar2);
		assertTrue(li.getEachLikelihood(0) +"\t"+ oldLikeli, li.getEachLikelihood(0) > oldLikeli);



	}

	

}
