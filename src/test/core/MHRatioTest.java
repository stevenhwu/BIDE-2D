/*******************************************************************************
 * MHRatioTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bide.core.MHRatio;

public class MHRatioTest {

	@Test
	public void testAcceptDoubleDoubleDoubleDouble() {
		for (int i = 0; i < 1000; i++) {
			assertTrue(MHRatio.accept(0, 0, 0, 1));
		}

		for (int i = 0; i < 1000; i++) {
			assertFalse(MHRatio.accept(0, 0, 0, -1000));
		}
		boolean[] count = new boolean[1000];
		double half = Math.log(0.5);
		for (int i = 0; i < 1000; i++) {
			count[i]=MHRatio.accept(0, 0, 0, half);
		}
		int T = 0;
		int F = 0;
		for (int i = 0; i < count.length; i++) {
			int z = count[i] ? T++: F++;
		}
		assertEquals("T1: "+T+" and "+F, T, F, 50);
		
		double halfhalf = half/2;
		double expected = Math.log(0.5);
			
//		assertEquals("T2: ", MHRatio.calculateRatio( halfhalf, 0, 0, halfhalf), expected, 0);

		
		
	}

}
