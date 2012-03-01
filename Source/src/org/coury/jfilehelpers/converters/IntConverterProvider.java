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

import org.coury.jfilehelpers.enums.ConverterKind;

public class IntConverterProvider extends ConverterProvider {

	private static final IntConverter INT_CONVERTER = new IntConverter();
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType == Integer.class || fieldType == Integer.TYPE;
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return converterKind.equals(ConverterKind.Int);
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, final String format) {
		return INT_CONVERTER;
	}

	public static class IntConverter extends ConverterBase {

		@Override
		public Object stringToField(String from) {
			if (from != null) {
				from = from.trim();
			}
			return Integer.parseInt(from);
		}

		@Override
		public String fieldToString(final Object from) {
			return from.toString();
		}
	}
}