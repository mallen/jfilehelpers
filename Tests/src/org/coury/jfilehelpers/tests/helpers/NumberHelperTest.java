/*
 * NumberHelperTest.java
 *
 * Copyright (C) 2007 Felipe Gonçalves Coury <felipe.coury@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.coury.jfilehelpers.tests.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.coury.jfilehelpers.helpers.NumberHelper;
import org.junit.Test;

public class NumberHelperTest {

	@Test
	public void isSupportedReturnsTrueForInts(){
		assertTrue(NumberHelper.isSupportedType(Integer.TYPE));
		assertTrue(NumberHelper.isSupportedType(Integer.class));
	}
	
	@Test
	public void isSupportedReturnsTrueForLongs(){
		assertTrue(NumberHelper.isSupportedType(Long.TYPE));
		assertTrue(NumberHelper.isSupportedType(Long.class));
	}
	
	@Test
	public void isSupportedReturnsTrueForDoubles(){
		assertTrue(NumberHelper.isSupportedType(Double.TYPE));
		assertTrue(NumberHelper.isSupportedType(Double.class));
	}
	
	@Test
	public void isSupportedReturnsTrueForFloats(){
		assertTrue(NumberHelper.isSupportedType(Float.TYPE));
		assertTrue(NumberHelper.isSupportedType(Float.class));
	}
	
	@Test
	public void isSupportedReturnsTrueForBigDecimal(){
		assertTrue(NumberHelper.isSupportedType(BigDecimal.class));
	}
	
	@Test
	public void isSupportedReturnsFalseForObject(){
		assertTrue(!NumberHelper.isSupportedType(Object.class));
	}
	
	@Test
	public void isSupportedReturnsFalseForString(){
		assertTrue(!NumberHelper.isSupportedType(String.class));
	}
	
	@Test 
	public void impliedDecimalPlacesInteger(){
		int i = 123456;
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 3);
		assertEquals(123, implied);
	}
	
	@Test 
	public void impliedDecimalPlacesInteger2(){
		int i = 123456;
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 2);
		//will be rounded up
		assertEquals(1235, implied);
	}
	
	@Test 
	public void impliedDecimalPlacesLong(){
		long i = 123456;
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 3);
		assertEquals(123L, implied);
	}
	
	@Test 
	public void impliedDecimalPlacesLong2(){
		long i = 123456;
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 2);
		//will be rounded up
		assertEquals(1235L, implied);
	}
	
	@Test 
	public void impliedDecimalPlacesFloat(){
		float i = 123456;
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 3);
		assertEquals(123.456F, implied);
	}
	
	@Test 
	public void impliedDecimalPlacesDouble(){
		double i = 123456;
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 3);
		assertEquals(123.456, implied);
	}
	
	@Test 
	public void impliedDecimalPlacesBigDecimal(){
		BigDecimal i = new BigDecimal(123456);
		Number implied = NumberHelper.applyImpliedDecimalPlaces(i, 3);
		assertEquals(new BigDecimal("123.456"), implied);
	}
	
}
