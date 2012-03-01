/*
 * NumberHelper.java
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
package org.coury.jfilehelpers.helpers;

import java.math.BigDecimal;

public class NumberHelper {

	public static boolean isSupportedType(final Class<?> numberType) {

		String typeName = numberType.getName();
		if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
			return true;
		} else if (typeName.equals("long") || typeName.equals("java.lang.Long")) {
			return true;
		} else if (typeName.equals("double") || typeName.equals("java.lang.Double")) {
			return true;
		} else if (typeName.equals("float") || typeName.equals("java.lang.Float")) {
			return true;
		} else if (typeName.equals("java.math.BigDecimal")) {
			return true;
		}
		return false;
	}
	
	public static int safeLongToInt(final long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	public static float safeDoubleToFloat(final double d) {
	    if (d < Float.MIN_VALUE || d > Float.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (d + " cannot be cast to float without changing its value.");
	    }
	    return (float) d;
	}
	
	public static boolean isFloatingPoint(final Number number){
		
		String typeName = number.getClass().getName();
		if (typeName.equals("double") || typeName.equals("java.lang.Double") 
				|| typeName.equals("float") || typeName.equals("java.lang.Float")){
			return true;
		}
		return false;
	}

	public static Number applyImpliedDecimalPlaces(final Number number, final int impliedDecimalPlaces){
		
		String typeName = number.getClass().getName();
		if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
			int num = number.intValue();
			double decimal = num / Math.pow(10, impliedDecimalPlaces);
			long rounded = Math.round(decimal);
			return safeLongToInt(rounded);
		} else if (typeName.equals("long") || typeName.equals("java.lang.Long")) {
			long num = number.longValue();
			double decimal = num / Math.pow(10, impliedDecimalPlaces);
			return Math.round(decimal);
		} else if (typeName.equals("double") || typeName.equals("java.lang.Double")) {
			double num = number.doubleValue();
			return num / Math.pow(10, impliedDecimalPlaces);
		} else if (typeName.equals("float") || typeName.equals("java.lang.Float")) {
			float num = number.floatValue();
			double decimal = num / Math.pow(10, impliedDecimalPlaces);
			return safeDoubleToFloat(decimal);
		} else if (typeName.equals("java.math.BigDecimal")) {
			BigDecimal dec = (BigDecimal) number;
			return dec.movePointLeft(impliedDecimalPlaces);
		}
		throw new IllegalArgumentException("Unsupported type: " + typeName);
	}
}
