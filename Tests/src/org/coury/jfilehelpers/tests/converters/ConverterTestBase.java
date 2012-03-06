/*
 * NumberTestBase.java
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.coury.jfilehelpers.converters.ConverterBase;
import org.coury.jfilehelpers.converters.ConverterProvider;
import org.junit.Test;

public abstract class ConverterTestBase<TCP extends ConverterProvider, TC extends ConverterBase, T> {

	private final Collection<Class<?>> validClasses;
	private final String validString;
	private final Class<?>[] numberClasses = new Class<?>[]{Integer.class, Integer.TYPE, 
															Long.class, Long.TYPE, 
															Double.class, Double.TYPE, 
															Float.class, Float.TYPE, 
															BigDecimal.class, 
															Date.class, 
															Boolean.class, Boolean.TYPE};
	private final T validObject;
	private final T blankObject;
	private final String nullString;
	

	public ConverterTestBase(final Class<?>[] validClasses, 
							final T validObject, final String validString, 
							final T blankObject, final String nullString) {
		
		this.validClasses = Arrays.asList(validClasses);
		this.validString = validString;
		this.validObject = validObject;
		this.blankObject = blankObject;
		this.nullString = nullString; 
	}


	protected abstract TCP createConverterProvider();
	protected abstract TC createConverter();
	
	@Test
	public void providerReturnTrueForValidClass() {
		TCP provider = createConverterProvider();
		for (Class<?> validClass : validClasses) {
			assertTrue(provider.handles(validClass));
		}
		
	}

	@Test
	public void providerReturnFalseForInvalidClass() {
		TCP provider = createConverterProvider();
		for (Class<?> clazz : numberClasses) {
			if(validClasses.contains(clazz)){
				continue;
			}
			assertFalse(provider.handles(clazz));
		}
		
	}

	@Test
	public void converterReturnsCorrectObjectForValidString() {
		TC converter = createConverter();
		T converted = (T) converter.stringToField(validString);
		assertEquals(validObject, converted);
	}

	@Test
	public void converterReturnsCorrectObjectForValidStringWithWhitespace() {
		TC converter = createConverter();
		T converted = (T) converter.stringToField("  " + validString + "  ");
		assertEquals(validObject, converted);
	}

	@Test
	public void converterReturnsBlankObjectForNullString() {
		TC converter = createConverter();
		T converted = (T) converter.stringToField(null);
		assertEquals(blankObject, converted);
	}

	@Test
	public void converterReturnsBlankObjectForEmptyString() {
		TC converter = createConverter();
		T converted = (T) converter.stringToField("");
		assertEquals(blankObject, converted);
	}

	@Test
	public void converterReturnsBlankObjectForWhitespaceString() {
		TC converter = createConverter();
		T converted = (T) converter.stringToField("   ");
		assertEquals(blankObject, converted);
	}

	@Test
	public void converterReturnCorrectStringForNumber() {
		TC converter = createConverter();
		String str = converter.fieldToString(validObject);
		assertEquals(validString, str);
	}

	@Test
	public void converterReturnCorrectStringForNull() {
		TC converter = createConverter();
		String str = converter.fieldToString(null);
		assertEquals(nullString, str);
	}

}