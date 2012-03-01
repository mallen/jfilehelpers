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

public class EnumConverterProvider extends ConverterProvider {

	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType.isEnum();
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return false;
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, final String format) {
		@SuppressWarnings("unchecked")
		Class<? extends Enum> enumType = (Class<Enum>) fieldType;
		return new EnumConverter(enumType);
	}

	public static class EnumConverter extends ConverterBase {

		private final Class<? extends Enum> fieldType;

		public EnumConverter(final Class<? extends Enum> fieldType) {
			this.fieldType = fieldType;
		}

		@Override
		public Object stringToField(final String from) {
			@SuppressWarnings("unchecked")
			Object ret = Enum.valueOf(fieldType, from);
			return ret;
		}

		@Override
		public String fieldToString(final Object from) {
			return from.toString();
		}
	}
}