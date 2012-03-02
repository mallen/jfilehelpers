/*
 * TimeConverterProvider.java
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
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LocalTimeConverterProvider extends ConverterProvider {

	private String defaultFormat = "HH:mm";
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType.equals(LocalTime.class);
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return converterKind.equals(ConverterKind.LocalTime);
	}
	
	public String getDefaultFormat() {
		return defaultFormat;
	}

	public void setDefaultFormat(final String defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, String format) {
		if(format == null || format.length() == 0){
			format = defaultFormat;
		}
		return new LocalTimeConverter(format);
	}
	
	public static class LocalTimeConverter extends ConverterBase {
		
		private final DateTimeFormatter formatter;

		public LocalTimeConverter(final String format) {
			if (format == null || format.length() < 1) {
				throw new IllegalArgumentException("The format of the DateTime Converter can be null or empty.");
			}
			
			formatter = DateTimeFormat.forPattern(format);
		}

		@Override
		public Object stringToField(final String from) {
			return LocalTime.parse(from, formatter);
		}
		
		@Override
		public String fieldToString(final Object from) {
			if(from == null){
				return "";
			}
			return ((LocalTime) from).toString(formatter);
		}
		
	}

}
