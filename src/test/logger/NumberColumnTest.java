/*******************************************************************************
 * NumberColumnTest.java
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
package test.logger;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bide.logger.NumberColumn;

public class NumberColumnTest {

	private NumberColumn nc;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		nc = new NumberColumn("test", 5);
	}

	@Test
	public void testNumberColumn() {
		
		assertEquals("Label", "test", nc.getLabel());
		assertEquals("dp", -1, nc.getDecimalPlaces());
		assertEquals("sf", 5, nc.getSignificantFigures());
		
	}

	@Test
	public void testSetLabel() {
		nc.setLabel("xyz");
		assertEquals("lable", "xyz", nc.getLabel());
	}


	@Test
	public void testSetSignificantFigures() {
		nc.setSignificantFigures(10);
		assertEquals("sf", 10, nc.getSignificantFigures());
		nc.setSignificantFigures(100);
		assertEquals("sf", 100, nc.getSignificantFigures());
	}

	@Test
	public void testSetDecimalPlaces() {
		nc.setDecimalPlaces(5);
		assertEquals("dp", 5, nc.getDecimalPlaces());
	}

//	@Test
//	public void testGetDecimalPlaces() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSignificantFigures() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testGetFormattedValue() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testFormatValue() {
		
		nc.setSignificantFigures(4);
		assertEquals("1.000",nc.formatValue(1));
		assertEquals("10.00",nc.formatValue(10));
		assertEquals("100.0",nc.formatValue(100));
		assertEquals("1000",nc.formatValue(1000));
		assertEquals("1E4",nc.formatValue(10000));
		assertEquals("1E5",nc.formatValue(100000));
		assertEquals("1.100",nc.formatValue(1.1));
		assertEquals("1.010",nc.formatValue(1.01));
		assertEquals("1.001",nc.formatValue(1.001));
		assertEquals("1.000",nc.formatValue(1.0001));
		assertEquals("1.000",nc.formatValue(1.00001));
		assertEquals("100.1",nc.formatValue(100.1));
		assertEquals("100.0",nc.formatValue(100.01));
		assertEquals("100.0",nc.formatValue(100.001));
		assertEquals("1E4",nc.formatValue(10000.1));
		assertEquals("1E3",nc.formatValue(1000.01));
		
		nc.setDecimalPlaces(6);
		assertEquals("1.100000",nc.formatValue(1.1));
		assertEquals("1.010000",nc.formatValue(1.01));
		assertEquals("1.001000",nc.formatValue(1.001));
		assertEquals("100.100000",nc.formatValue(100.1));
		assertEquals("100.010000",nc.formatValue(100.01));
		assertEquals("100.001000",nc.formatValue(100.001));
		assertEquals("100.000100",nc.formatValue(100.0001));
		
		nc.setDecimalPlaces(3);
		assertEquals("1.100",nc.formatValue(1.1));
		assertEquals("1.010",nc.formatValue(1.01));
		assertEquals("1.001",nc.formatValue(1.001));
		assertEquals("1.000",nc.formatValue(1.0001));
		assertEquals("1.000",nc.formatValue(1.00001));
		assertEquals("10.000",nc.formatValue(10.00001));
	}


}

