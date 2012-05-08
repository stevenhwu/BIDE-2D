/*******************************************************************************
 * ExpUniExpDistributionTest.java
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
package test.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bide.math.ExpUniExpDistribution;
import bide.math.ExponentialDistribution;
import bide.math.UniformDistribution;

public class ExpUniExpDistributionTest {

	ExpUniExpDistribution t = new ExpUniExpDistribution();

	@Test
	public void testGetPhi() {
//		fail("Not yet implemented");
		t.setPhi(10);
		assertEquals(10,t.getPhi(),0);
	}
	
	
	@Test
	public void testExpUniExpDistributionDoubleDoubleDoubleDouble() {
//		fail("Not yet implemented");
		ExpUniExpDistribution testClass = new ExpUniExpDistribution(1,2,3,4);
		assertEquals("Down", 1, testClass.getlambdaDown(), 0);
		assertEquals("Up", 2, testClass.getLambdaUp(), 0);
		assertEquals("Phi", 3, testClass.getPhi(), 0);
//		assertEquals("Phi", 3, testClass.phi, 0);
//		assertEquals("Gap", 4, testClass.gapUpper, 0);
//		assertEquals("Gap", -4, testClass.gapLower, 0);
		
	}

	
	
	@Test
	public void testPdf() {
		
		ExpUniExpDistribution testClass = new ExpUniExpDistribution(2,2,1.0/3,2);
		
		double proportionDE = (1 - 1.0/3)/2;
		double expected = proportionDE*ExponentialDistribution.pdf(1, 2);
		assertEquals("Up",expected, testClass.pdf(3), 0);
		assertEquals("Down", expected, testClass.pdf(-3), 0);
		expected = 1.0/3*UniformDistribution.pdf(1,-2,2);
		assertEquals("Same", expected, testClass.pdf(1), 0);
	}

	@Test
	public void testLogPdf() {
		ExpUniExpDistribution testClass = new ExpUniExpDistribution(2,2,1.0/3,2);
		
		double proportionDE = (1 - 1.0/3)/2;
		double expected = Math.log( proportionDE*ExponentialDistribution.pdf(1, 2) );
		assertEquals("Up",expected, testClass.logPdf(3), 0);
		assertEquals("Down", expected, testClass.logPdf(-3), 0);
		expected = Math.log( 1.0/3*UniformDistribution.pdf(1,-2,2) );
		assertEquals("Same", expected, testClass.logPdf(1), 0);
		assertEquals("Static", expected, ExpUniExpDistribution.logPdf(1, 2,2,1.0/3,2),0);
	}

}
