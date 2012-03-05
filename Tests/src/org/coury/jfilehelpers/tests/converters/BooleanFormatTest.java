/*
 * BooleanTest.java
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
package org.coury.jfilehelpers.tests.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.coury.jfilehelpers.tests.common.Common;
import org.coury.jfilehelpers.tests.converters.testobjects.BooleanType;
import org.junit.Test;

public class BooleanFormatTest {

	@Test
	public void test1() throws IOException{
		
		FileHelperEngine<BooleanType> engine = new FileHelperEngine<BooleanType>(BooleanType.class);

		List<BooleanType> res = (List<BooleanType>) Common.readTest(engine, "Good/BooleanTest1.txt");
		
		assertEquals(2, res.size());
		
		assertTrue(res.get(0).b1);
		assertFalse(res.get(0).b2);
		
		assertFalse(res.get(1).b1);
		assertTrue(res.get(1).b2);
		
	}
	
	@Test
	public void test2() throws IOException{
		
		FileHelperEngine<BooleanType> engine = new FileHelperEngine<BooleanType>(BooleanType.class);
		engine.setDefaultTrueString("y");
		engine.setDefaultFalseString("n");

		List<BooleanType> res = (List<BooleanType>) Common.readTest(engine, "Good/BooleanTest2.txt");
		
		assertEquals(2, res.size());
		
		assertTrue(res.get(0).b1);
		assertFalse(res.get(0).b2);
		
		assertFalse(res.get(1).b1);
		assertTrue(res.get(1).b2);
		
	}
	
}
