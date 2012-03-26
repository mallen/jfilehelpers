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

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.coury.jfilehelpers.annotations.FieldDateFormat;

public class DateConverterProvider extends ConverterProvider {

	private String defaultFormat = "ddMMyyyy";
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType.equals(Date.class);
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType) {
		return new DateConverter(defaultFormat);
	}
	
	public String getDefaultFormat() {
		return defaultFormat;
	}

	public void setDefaultFormat(final String defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	public static class DateConverter extends ConverterBase {
		
		private String format;
		private SimpleDateFormat sdf;
		private String timeZone;

		public DateConverter(final String format) {
			this.format = format;
		}

		private void checkFormat() {
			if(sdf != null){
				return;
			}
			
			if (format == null || format.length() < 1) {
				throw new IllegalArgumentException("The format of the DateTime Converter cannot be null or empty.");
			}
			
			TimeZone tz = null;
			if(StringUtils.isNotBlank(timeZone)){
				tz = TimeZone.getTimeZone(timeZone);
			}

			try {
				sdf = new SimpleDateFormat(format, Locale.ENGLISH);
				sdf.setLenient(false);
				sdf.format(new Date());
				if(tz != null){
					sdf.setTimeZone(tz);
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("The format: '" + format + " is invalid for the DateTime Converter.");
			}
		}
		
		@Override
		public Class<? extends Annotation> getOptionsAnnotationType() {
			return FieldDateFormat.class;
		}
		
		@Override
		public void setOptionsFromAnnotation(final Annotation annotation) {
			FieldDateFormat options = (FieldDateFormat) annotation;
			this.format = options.value();
			this.timeZone = options.timeZone();
		}

		@Override
		public Object stringToField(final String from) {
			if (StringUtils.isBlank(from)) {
				return null;
			}
			
			checkFormat();

			Date val;
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
				throw new RuntimeException(extra + " - from:" + from, e);
			}

			return val;
		}

		@Override
		public String fieldToString(final Object from) {
			if(from == null){
				return "";
			}
			checkFormat();
			return sdf.format(from);
		}
	}
}