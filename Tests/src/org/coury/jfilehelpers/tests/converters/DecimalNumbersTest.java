/*
 * DecimalNumbersTest.java
 *
 * Copyright (C) 2007 Felipe Gon�alves Coury <felipe.coury@gmail.com>
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

package org.coury.jfilehelpers.tests.converters;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.coury.jfilehelpers.tests.common.Common;
import org.coury.jfilehelpers.tests.converters.testobjects.DecimalType;

public class DecimalNumbersTest extends TestCase {
	@SuppressWarnings("unchecked")
	FileHelperEngine engine;

	@SuppressWarnings("unchecked")
	public void testDecimals1() throws IOException {
		engine = new FileHelperEngine<DecimalType>(DecimalType.class);

		List<DecimalType> res = 
			(ArrayList<DecimalType>) Common.readTest(engine, "Good/NumberFormat.txt");

		assertEquals(10, res.size());

		assertDecimal("32.38", res.get(0));
		assertDecimal("11.61", res.get(1));
		assertDecimal("65.83", res.get(2));
		assertDecimal("41.34", res.get(3));
		assertDecimal("51.3", res.get(4));
		assertDecimal("58.17", res.get(5));
		assertDecimal("22.98", res.get(6));
		assertDecimal("148.33", res.get(7));
		assertDecimal("13.97", res.get(8));
		assertDecimal("81.91", res.get(9));
	}

	private static void assertDecimal(String dec, DecimalType res) {
		assertEquals(Double.parseDouble(dec), res.doubleField);
		assertEquals(Float.parseFloat(dec), res.floatField);
		assertEquals(new BigDecimal(dec), res.decimalField);
	}
}
