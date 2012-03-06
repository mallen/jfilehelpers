/*
 * BigDecimalTest.java
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

import java.math.BigDecimal;

import org.coury.jfilehelpers.converters.BigDecimalConverterProvider;
import org.coury.jfilehelpers.converters.BigDecimalConverterProvider.BigDecimalConverter;

public class BigDecimalTest extends ConverterTestBase<BigDecimalConverterProvider, BigDecimalConverter, BigDecimal> {

	public BigDecimalTest() {
		super(new Class<?>[]{BigDecimal.class}, new BigDecimal("12.345"), "12.345",BigDecimal.ZERO, "0");
	}
	
	@Override
	BigDecimalConverter createConverter() {
		return new BigDecimalConverter();
	}
	
	@Override
	BigDecimalConverterProvider createConverterProvider() {
		return new BigDecimalConverterProvider();
	}
	
}
