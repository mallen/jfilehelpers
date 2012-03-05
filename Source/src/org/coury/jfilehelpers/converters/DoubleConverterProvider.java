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

import org.apache.commons.lang3.StringUtils;
import org.coury.jfilehelpers.enums.ConverterKind;

public class DoubleConverterProvider extends ConverterProvider {

	private static final DoubleConverter DOUBLE_CONVERTER = new DoubleConverter();
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType == Double.class || fieldType == Double.TYPE;
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return converterKind.equals(ConverterKind.Double);
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, final String format) {
		return DOUBLE_CONVERTER;
	}

	public static class DoubleConverter extends ConverterBase {

		@Override
		public Double stringToField(final String from) {
			if(StringUtils.isBlank(from)){
				return 0d;
			}
			return Double.parseDouble(from.trim());
		}

		@Override
		public String fieldToString(final Object from) {
			if(from == null){
				return "0";
			}
			return from.toString();
		}

	}
}