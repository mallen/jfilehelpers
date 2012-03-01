/*
 * BooleanConverter.java
 *
 * Copyright (C) 2007 Felipe Gon�alves Coury <felipe.coury@gmail.com>
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

public class BooleanConverterProvider extends ConverterProvider {

	private static String trueString = null;
	private static String falseString = null; 
	
	@Override
	public boolean handles(final Class<?> fieldType) {
		return fieldType == Boolean.class || fieldType == Boolean.TYPE;
	}

	@Override
	public boolean handles(final ConverterKind converterKind) {
		return converterKind.equals(ConverterKind.Boolean);
	}

	@Override
	public ConverterBase createConverter(final Class<?> fieldType, final String format) {
		return new BooleanConverter(trueString, falseString);
	}
	
	public static String getTrueString() {
		return trueString;
	}

	public static void setTrueString(final String trueString) {
		BooleanConverterProvider.trueString = trueString;
	}

	public static String getFalseString() {
		return falseString;
	}

	public static void setFalseString(final String falseString) {
		BooleanConverterProvider.falseString = falseString;
	}

	public static class BooleanConverter extends ConverterBase {
		private String trueString = null;
		private String falseString = null;
		private String trueStringLower = null;
		private String falseStringLower = null;

		public BooleanConverter(final String trueString, final String falseString) {
			this.trueString = trueString;
			this.falseString = falseString;
			this.trueStringLower = trueString.toLowerCase();
			this.falseStringLower = falseString.toLowerCase();
		}

		@Override
		public Object stringToField(final String from) {
			Object val;
			try {
				String testTo = from.toLowerCase();
				if (trueString == null) {
					testTo = testTo.trim();
					if (testTo.equals("true") || testTo.equals("1")) {
						val = true;
					} else if (testTo.equals("false") || testTo.equals("0") || testTo.equals("")) {
						val = false;
					} else {
						throw new Exception();
					}
				}
				else {
					if (testTo.equals(trueStringLower) || testTo.trim().equals(trueStringLower)) {
						val = true;
					} else if (testTo.equals(falseStringLower) || testTo.trim().equals(falseStringLower)) {
						val = false;
					} else {
						// throw new ConvertException(from, typeof(bool),
						// "The string: " + from +
						// " cant be recognized as boolean using the true/false values: "
						// + mTrueString + "/" + mFalseString);
						throw new RuntimeException(
								"The string: " + from + " cant be recognized as boolean " +
										"using the true/false values: " + trueString + "/" + falseString);
					}
				}
			} catch (Exception e) {
				// throw new ConvertException(from, typeof (Boolean));
				throw new RuntimeException("Error converting: " + from + " to boolean");
			}

			return val;
		}

		@Override
		public String fieldToString(final Object from) {
			boolean b = Boolean.parseBoolean(from.toString());
			if (b) {
				if (trueString == null) {
					return "True";
				} else {
					return trueString;
				}
			} else if (falseString == null) {
				return "False";
			} else {
				return falseString;
			}

		}
	}
}