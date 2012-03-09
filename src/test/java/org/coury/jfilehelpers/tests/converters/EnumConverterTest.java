/*
 * EnumConverterTest.java
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
import java.util.List;

import junit.framework.TestCase;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.coury.jfilehelpers.tests.converters.testobjects.Enum2;
import org.coury.jfilehelpers.tests.converters.testobjects.EnumType2;

public class EnumConverterTest extends TestCase {
	@SuppressWarnings("unchecked")
	FileHelperEngine engine;
	
	public static void main(final String[] args) {
		System.out.println(Enum2.One.toString());
	}
    
	@SuppressWarnings("unchecked")
	public void testEnumSingleCase() throws IOException {
        engine = new FileHelperEngine<EnumType2>(EnumType2.class);

		List<EnumType2> res = engine.readResource("/Good/EnumConverter2.txt");

		assertEquals(5, res.size());

		assertEquals(Enum2.One, res.get(0).enumValue);
		assertEquals(Enum2.One, res.get(1).enumValue);
		assertEquals(Enum2.Two, res.get(2).enumValue);
		assertEquals(Enum2.Three, res.get(3).enumValue);
		assertEquals(Enum2.Three, res.get(4).enumValue);
	}
	
	@SuppressWarnings("unchecked")
	public void testUnknownEnumValueThrows() throws IOException {
        engine = new FileHelperEngine<EnumType2>(EnumType2.class);

        try{
        	List<EnumType2> res = engine.readResource("/Bad/BadEnum1.txt");
        	fail("expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
			// expected exception
		} catch(Exception e){
			fail("Unexpected exception: " + e);
		}

	}

}