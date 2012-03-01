/*
 * ImpliedDecimalPlacesTest.java
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
package org.coury.jfilehelpers.tests.implieddecimal;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.junit.Before;
import org.junit.Test;

public class ImpliedDecimalPlacesTest {

	private String file;

	@Before
	public void before(){
		file = "foos_for_implieddecimalplaces.csv";
	}
	
	@Test
	public void roundTrip() {
		
		FileHelperEngine<Foo> engine = new FileHelperEngine<Foo>(Foo.class);
		
		List<Foo> foos = new ArrayList<Foo>();
		Foo foo = new Foo();
		foo.i = 123;
		foo.l = 1234L;
		foo.f = 123.456F;
		foo.d = 1234.5678;
		foo.decimal=new BigDecimal("123.45678");
		foos.add(foo);
		
		try {
			engine.writeFile(file, foos);
			foos = engine.readFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		foo = foos.get(0);
		assertEquals(123, foo.i);
		assertEquals(1234L, foo.l);
		assertEquals(123.456F, foo.f, 0.00001);
		assertEquals(1234.5678, foo.d, 0.00001);
		assertEquals(new BigDecimal("123.45678"), foo.decimal);
		
	}
	
//	@After
//	protected void tearDown() {
//		new File(file).delete();
//	}
	
}
