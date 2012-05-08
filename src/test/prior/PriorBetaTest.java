/*******************************************************************************
 * PriorBetaTest.java
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
package test.prior;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bide.prior.PriorBeta;

public class PriorBetaTest {

	BetaDistribution bdist;
	double expected;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLogPdfDoubleDoubleDouble()  {
		double alpha = 1;
		double beta = 2;
		bdist = new BetaDistribution(alpha, beta);
		expected = Math.log( bdist.density(0.7) );
		assertEquals("",expected, PriorBeta.logPdf(0.7, alpha, beta),1E-10);
		PriorBeta pb = new PriorBeta(alpha, beta);
		assertEquals("",expected, PriorBeta.logPdf(0.7, alpha, beta),1E-10);
	}

	@Test
	public void testPdfDoubleDoubleDouble()  {
		double alpha = 1;
		double beta = 2;
		bdist = new BetaDistribution(alpha, beta);
		expected = bdist.density(0.4);
		assertEquals("",expected, PriorBeta.pdf(0.4, alpha, beta),1E-10);
		PriorBeta pb = new PriorBeta(alpha, beta);
		assertEquals("",expected, PriorBeta.pdf(0.4, alpha, beta),1E-10);
	}

}
