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


public class StringConverterProvider extends ConverterProvider {

	private static final StringConverter STRING_CONVERTER = new StringConverter();
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType == String.class;
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType) {
		return STRING_CONVERTER;
	}

	public static class StringConverter extends ConverterBase {

		@Override
		public Object stringToField(final String from) {
			return from;
		}

		@Override
		public String fieldToString(final Object from) {
			if(from == null){
				return "";
			}
			return from.toString();
		}
		
		@Override
		public boolean isCustomNullHandling() {
			return true;
		}

	}
}