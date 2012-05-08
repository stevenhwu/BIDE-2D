/*******************************************************************************
 * TransformationTest.java
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

import bide.math.Transformation;

public class TransformationTest {

	@Test
	public void testLogit() {
		
		assertEquals(0, Transformation.logit(0.5), 0);
		assertEquals(-2.1972246, Transformation.logit(0.1), 10E-7);
		assertEquals(-1.3862944, Transformation.logit(0.2), 10E-7);
		assertEquals(-0.8472979, Transformation.logit(0.3), 10E-7);
		assertEquals(-0.4054651, Transformation.logit(0.4), 10E-7);

		assertEquals(0.4054651, Transformation.logit(0.6), 10E-7);
		assertEquals(0.8472979, Transformation.logit(0.7), 10E-7);
		assertEquals(1.3862944, Transformation.logit(0.8), 10E-7);
		assertEquals(2.1972246, Transformation.logit(0.9), 10E-7);

	
		
	}

	@Test
	public void testInvLogit() {

		assertEquals(0.04742587, Transformation.invLogit(-3), 10E-7);
		assertEquals(0.11920292, Transformation.invLogit(-2), 10E-7);
		assertEquals(0.26894142, Transformation.invLogit(-1), 10E-7);
		assertEquals(0.5, 		 Transformation.invLogit(0), 10E-7);
		assertEquals(0.73105858, Transformation.invLogit(1), 10E-7);
		assertEquals(0.88079708, Transformation.invLogit(2), 10E-7);
		assertEquals(0.95257413, Transformation.invLogit(3), 10E-7);
		

	}

}
