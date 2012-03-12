/*
 * AfterReadRecordTest.java
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
package org.coury.jfilehelpers.tests.postread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.junit.Test;

public class PostReadRecordTest {

	@Test
	public void postReadHandlerWorks() throws IOException{
		
		FileHelperEngine<Foo> fileEngine = new FileHelperEngine<Foo>(Foo.class);
		List<Foo> foos = fileEngine.readResource("/foo.csv");
		
		assertNotNull(foos);
		assertEquals(2, foos.size());
		for (Foo foo : foos) {
			assertTrue(foo.getNumber() % 2 == 1);
		}
		
	}
	
}
