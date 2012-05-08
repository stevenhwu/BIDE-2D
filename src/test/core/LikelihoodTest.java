/*******************************************************************************
 * LikelihoodTest.java
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
package test.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bide.core.Likelihood;
import bide.core.par.ParSpot;
import bide.core.par.Parameter;
import bide.core.par.Spot;
import bide.math.NormalDistribution;
import bide.math.Transformation;

public class LikelihoodTest {

	private static Likelihood li;
	private double expected;

	// private static NormalDistribution nd = new NormalDistribution();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		li = new Likelihood(100, -10);

	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetEachLikelihood() {

		for (int i = 0; i < 10; i++) {
			li.setEachLikelihood(i, 10);
		}
		expected = 100;
		assertEquals(expected, li.getGlikelihood(), 0);
		
		for (int i = 0; i < 10; i++) {
			li.setEachLikelihood(i, i + 1);

		}
		expected = li.getGlikelihood() - 1 + 10;
		li.setEachLikelihood(0, 10);
		assertEquals(expected, li.getGlikelihood(), 0);
		
		
		expected = li.getGlikelihood() - 5 + 2;
//		li.replaceLikelihood(4, 2);
		li.setEachLikelihood(4, 2);
		assertEquals(expected, li.getGlikelihood(), 0);

	}

	@Test
	public void testCalLogLikeliParSpot() throws Exception {

		Spot sp = new Spot("1	1	2	NA	3	NA	NA	");
		ParSpot spar = new ParSpot(0, 1, 1, -2, 1);
		spar.setSpot(sp);
		// double[] s = {1,2};
		// double expected = li.calLogLikeli(s, 3, 0,
		// Transformation.invLogit(1), 1);
		// s = new double[] {3};
		// expected += li.calLogLikeli(s, 3, 1, Transformation.invLogit(-1),
		// 1);;
		//			
		assertEquals(li.calLogLikeli(spar), li.calLogLikeli( spar), 0);
		double[] tempPar = { spar.getMu1(), spar.getProb1(), spar.getSd(),
				spar.getMu2(), spar.getProb2(), spar.getSd() };
		assertEquals(li.calLogLikeli(spar), li.calLogLikeli(sp, tempPar), 0);
	}

	@Test
	public void testCalLogLikeliSpotParSpot() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Spot sp = new Spot("1	1	2	NA	3	NA	NA	");
		ParSpot spar = new ParSpot(0, 1, 1, -2, 1);
		spar.setSpot(sp);
		double[] s = { 1, 2 };
		
		Method m = Likelihood.class.getDeclaredMethod("calLogLikeli", double[].class, int.class, double.class,  double.class, double.class);
		m.setAccessible(true);

		expected = (Double) m.invoke(li, s, 3, 0, Transformation.invLogit(1),	1);
		s = new double[] { 3 };
		expected += (Double) m.invoke(li, s, 3, 1, Transformation.invLogit(-1), 1);

		assertEquals(expected, li.calLogLikeli( spar), 0);

//				 targetClass.getDeclaredMethod(methodName, argClasses);
//		method.setAccessible(true);
//		return method.invoke(targetObject, argObjects);
		
//		double expected = li.calLogLikeli(s, 3, 0, Transformation.invLogit(1), 1);
//		s = new double[] { 3 };
//		expected += li.calLogLikeli(, s, 3, 1, Transformation.invLogit(-1), 1);

	}

	@Test
	public void testCalLogLikeliDoubleArrayIntDoubleDoubleDouble() throws ReflectiveOperationException  {

		
		Method m = Likelihood.class.getDeclaredMethod("calLogLikeli", double[].class, int.class, double.class,  double.class, double.class);
		m.setAccessible(true);
		
		double[] s = { 1 };
		expected = -0.918938524890523;
		assertEquals(expected, (Double) m.invoke(li, s, 1, 1, 1, 1), 10E-10);

		expected = -1.612085705450469;
		assertEquals(expected, (Double) m.invoke(li, s, 1, 1, 0.5, 1), 10E-10);

		s = new double[] { 1, 1 };
		expected = -3.224171410900937;
		assertEquals(expected, (Double) m.invoke(li, s, 2, 1, 0.5, 1), 10E-10);

		s = new double[] { 0, 2 };
		expected = -4.224171410900937;
		assertEquals(expected, (Double) m.invoke(li, s, 2, 1, 0.5, 1), 10E-10);

		expected = -2.837877049781047;
		assertEquals(expected, (Double) m.invoke(li, s, 2, 1, 0, 1), 10E-10);
		assertEquals(expected, (Double) m.invoke(li, s, 2, 1, 1, 1), 10E-10);

		s = new double[] { 0, 2 };
		expected = -5.61046577202083;
		assertEquals("0 2 NA NA", expected, (Double) m.invoke(li, s, 4, 1, 0.5, 1),
				10E-10);

		expected = -7.65376826708479;
		assertEquals("0 2 NA NA", expected, (Double) m.invoke(li, s, 4, 1, 0.1, 1),
				10E-10);
		assertEquals("0 2 NA NA", expected, (Double) m.invoke(li, s, 4, 1, 0.9, 1),
				10E-10);

	}

	@Test
	public void testCalGlobalLogLikelihood() {

		Spot sp = new Spot("1	1	2	NA	3	NA	NA	");
		ParSpot spar = new ParSpot(0, 1, 1, -2, 1);
		spar.setSpot(sp);
		
		ArrayList<Spot> allSp = new ArrayList<Spot>();
		ParSpot[] allspar = new ParSpot[10];
		for (int i = 0; i < 10; i++) {
			allSp.add(sp);
			allspar[i] = spar;
			
		}
		double expected = 10 * li.calLogLikeli( spar);
		li.calGlobalLogLikelihood( allspar);
		assertEquals(expected, li.getGlikelihood(), 10E-10);
		
	}



	@Test
	public void testCondLikeliMu() {
		ParSpot spar = new ParSpot(0, 1, 1, -2, 1);
		ParSpot[] allspar = new ParSpot[10];
		for (int i = 0; i < 10; i++) {
			allspar[i] = spar;
		}
		assertEquals("0, 1", NormalDistribution.logPdf(0, 0, 1) * 10, li
				.condLikeliMu(allspar, 0, 1), 10E-10);
		assertEquals("0, 1", NormalDistribution.logPdf(0, 1, 2) * 10, li
				.condLikeliMu(allspar, 1, 2), 10E-10);
		allspar[0] = new ParSpot(1, 1, 1, -2, 1);
		double expected = NormalDistribution.logPdf(0, 1, 2) * 9
				+ NormalDistribution.logPdf(1, 1, 2);
		assertEquals("0, 1", expected, li.condLikeliMu(allspar, 1, 2), 10E-10);
	}

	@Test
	public void testCondLikeliDelta() {
		Parameter spar = new ParSpot(0, 1, 1, -2, 1);
		Parameter[] allspar = new Parameter[10];
		for (int i = 0; i < 10; i++) {
			allspar[i] = spar;
		}
//		assertEquals("0, 1",
//				ExpUniExpDistribution.logPdf(1, 1, 2, 0.5, 2) * 10, li
//						.condLikeliDeltaOld(allspar, 1, 2, 0.5), 10E-10);
//		assertEquals("0, 1",
//				ExpUniExpDistribution.logPdf(1, 0.5, 3, 0.3, 2) * 10, li
//						.condLikeliDeltaOld(allspar, 0.5, 3, 0.3), 10E-10);
//		allspar[0] = new ParSpot(0, 3, -1, -2, 1);
//		double expected = ExpUniExpDistribution.logPdf(1, 1, 2, 0.5, 2) * 9
//				+ ExpUniExpDistribution.logPdf(3, 1, 2, 0.5, 2);
//		assertEquals("0, 1", expected, li.condLikeliDeltaOld(allspar, 1, 2, 0.5),
//				10E-10);
	}

	@Test
	public void testCondLikeliProb1() {
		ParSpot spar = new ParSpot(0, 1, 1, -2, 1);
		ParSpot[] allspar = new ParSpot[10];
		for (int i = 0; i < 10; i++) {
			allspar[i] = spar;
		}
		assertEquals("0, 1", NormalDistribution.logPdf(1, 0, 1) * 10, li
				.condLikeliPi(allspar, 0, 1), 10E-10);
		assertEquals("0, 1", NormalDistribution.logPdf(1, 1, 2) * 10, li
				.condLikeliPi(allspar, 1, 2), 10E-10);
		allspar[0] = new ParSpot(0, 1, -1, -2, 1);
		double expected = NormalDistribution.logPdf(1, 1, 2) * 9
				+ NormalDistribution.logPdf(-1, 1, 2);
		assertEquals("0, 1", expected, li.condLikeliPi(allspar, 1, 2), 10E-10);
	}

	@Test
	public void testCondLikeliProb2() {
		ParSpot spar = new ParSpot(0, 1, 1, -2, 1);
		ParSpot[] allspar = new ParSpot[10];
		for (int i = 0; i < 10; i++) {
			allspar[i] = spar;
		}
		assertEquals("0, 1", NormalDistribution.logPdf(-2, 0, 1) * 10, li
				.condLikeliRho(allspar, 0, 1), 10E-10);
		assertEquals("0, 1", NormalDistribution.logPdf(-2, 1, 2) * 10, li
				.condLikeliRho(allspar, 1, 2), 10E-10);
		allspar[0] = new ParSpot(0, 1, -1, 2, 1);
		double expected = NormalDistribution.logPdf(-2, 1, 2) * 9
				+ NormalDistribution.logPdf(2, 1, 2);
		assertEquals("0, 1", expected, li.condLikeliRho(allspar, 1, 2), 10E-10);
	}

	@Test
	public void testCondLikeliSd() {
		Parameter spar = new ParSpot(0, 1, 1, -2, 1);
		Parameter[] allspar = new Parameter[10];
		for (int i = 0; i < 10; i++) {
			allspar[i] = spar;
		}
//		assertEquals("T1", InverseGammaDistribution.logPdf(1, 0.01, 0.01) * 10,
//				li.condLikeliSd(allspar, 0.01, 0.01), 10E-10);
//		assertEquals("T2", InverseGammaDistribution.logPdf(1, 1, 2) * 10, li
//				.condLikeliSd(allspar, 1, 2), 10E-10);
//		allspar[0] = new ParSpot(0, 1, -1, 2, 2);
//		double expected = InverseGammaDistribution.logPdf(1, 1, 2) * 9
//				+ InverseGammaDistribution.logPdf(2, 1, 2);
//		assertEquals("T3", expected, li.condLikeliSd(allspar, 1, 2), 10E-10);

	}

	@Test
	public void testGetPriorProb() {
		li.init();
		li.putPriorProb("GMUSD", 10);
		li.putPriorProb("GDELTA", 5);
		li.putPriorProb("GPI", -20);
		assertEquals("GMUSDA", 10, li.getPriorProb("GMUSD"), 0);
		assertEquals("GDELTA", 5, li.getPriorProb("GDELTA"), 0);
		assertEquals("GPI", -20, li.getPriorProb("GPI"), 0);

		assertEquals("glikely", -5, li.getPosterior(), 0);
		li.putPriorProb("GPI", 10);
		assertEquals("glikely", 25, li.getPosterior(), 0);

	}

	@Test
	public void testGetParamLikelihood() {
		li.init();
		li.putParamLikelihood("GMUSD", 10);
		li.putParamLikelihood("GDELTA", 5);
		li.putParamLikelihood("GPI", -20);
		assertEquals("GMUSD", 10, li.getParamLikelihood("GMUSD"), 0);
		assertEquals("GDELTA", 5, li.getParamLikelihood("GDELTA"), 0);
		assertEquals("GPI", -20, li.getParamLikelihood("GPI"), 0);

		li.putParamLikelihood("GMUSD", -20);
		assertEquals("GMUSD", -20, li.getParamLikelihood("GMUSD"), 0);
		assertEquals("glikely", -35, li.getGlikelihood(), 0);
		li.putParamLikelihood("GPI", 10);
		assertEquals("glikely", -5, li.getGlikelihood(), 0);
	}

	@Test
	public void testGetEachLikelihood() {

		double[] d = new double[100];
		li = new Likelihood(100, -10);
		assertArrayEquals(d, li.getEachLikelihood(), 0);

	}

	@Test
	public void testGetEachLikelihoodInt() {

		li.setEachLikelihood(1, 10);
		li.setEachLikelihood(9, -10);
		assertEquals("eachLikelihood", 10, li.getEachLikelihood(1), 0);
		assertEquals("eachLikelihood", -10, li.getEachLikelihood(9), 0);
	}

}
