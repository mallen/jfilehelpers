/*
 * DateTest.java
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

import static org.junit.Assert.fail;

import java.util.Date;

import org.coury.jfilehelpers.converters.DateConverterProvider;
import org.coury.jfilehelpers.converters.DateConverterProvider.DateConverter;
import org.junit.Test;

public class DateTest extends ConverterTestBase<DateConverterProvider, DateConverter, Date> {

	public DateTest() {
		super(new Class<?>[]{Date.class},new Date(112, 1, 14), "20120214", null, "");
	}

	@Override
	DateConverterProvider createConverterProvider() {
		return new DateConverterProvider();
	}

	@Override
	DateConverter createConverter() {
		return new DateConverter("yyyyMMdd");
	}
	
	@Override()
	@Test(expected=RuntimeException.class)
	public void converterReturnsCorrectObjectForValidStringWithWhitespace() {
		DateConverter converter = createConverter();
		converter.stringToField("  20120214  ");
		fail("should have thrown");
		
	}
	

}
