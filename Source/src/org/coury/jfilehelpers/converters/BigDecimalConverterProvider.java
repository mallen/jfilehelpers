/*
 * IntConverter.java
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
package org.coury.jfilehelpers.converters;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.coury.jfilehelpers.enums.ConverterKind;

public class BigDecimalConverterProvider extends ConverterProvider {

	private static final BigDecimalConverter BIG_DECIMAL_CONVERTER = new BigDecimalConverter();
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType == BigDecimal.class;
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return converterKind.equals(ConverterKind.BigDecimal);
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, final String format) {
		
		return BIG_DECIMAL_CONVERTER;
	}

	public static class BigDecimalConverter extends ConverterBase {

		@Override
		public BigDecimal stringToField(final String from) {
			if(StringUtils.isBlank(from)){
				return BigDecimal.ZERO;
			}
			return new BigDecimal(from.trim());
		}

		@Override
		public String fieldToString(final Object from) {
			if(from == null){
				return "0";
			}
			BigDecimal decimal = (BigDecimal) from;
			return decimal.toPlainString();
		}

	}
}