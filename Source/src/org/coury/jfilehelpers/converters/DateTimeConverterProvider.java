/*
 * DateTimeConverter.java
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.coury.jfilehelpers.enums.ConverterKind;

public class DateTimeConverterProvider extends ConverterProvider {

	private static String defaultFormat = "ddMMyyyy";
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType.equals(Date.class);
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return converterKind.equals(ConverterKind.Date);
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, String format) {
		if(format == null || format.length() == 0){
			format = defaultFormat;
		}
		return new DateTimeConverter(format);
	}
	
	public static String getDefaultFormat() {
		return defaultFormat;
	}

	public static void setDefaultFormat(final String defaultFormat) {
		DateTimeConverterProvider.defaultFormat = defaultFormat;
	}

	public static class DateTimeConverter extends ConverterBase {
		
		private final String format;
		private final SimpleDateFormat sdf;

		public DateTimeConverter(final String format) {
			this.format = format;
			if (format == null || format.length() < 1) {
				throw new IllegalArgumentException("The format of the DateTime Converter can be null or empty.");
			}

			try {
				sdf = new SimpleDateFormat(format);
				sdf.format(new Date());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("The format: '" + format + " is invalid for the DateTime Converter.");
			}
		}

		public static boolean canHandle(final Class<?> fieldType) {
			return fieldType == Date.class;
		}

		@Override
		public Object stringToField(String from) {
			if (from == null) {
				from = "";
			}

			Object val;

			try {
				val = sdf.parse(from);
			} catch (ParseException e) {
				String extra = "";
				if (from.length() > format.length()) {
					extra = " There are more chars than in the format string: '" + format + "'";
				} else if (from.length() < format.length()) {
					extra = " There are less chars than in the format string: '" + format + "'";
				} else {
					extra = " Using the format: '" + format + "'";
				}

				// throw new ConvertException(from, typeof (DateTime), extra);
				throw new RuntimeException(extra);
			}

			return val;
		}

		@Override
		public String fieldToString(final Object from) {
			return sdf.format(from);
		}
	}
}