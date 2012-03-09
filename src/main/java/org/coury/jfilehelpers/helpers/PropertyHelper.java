/*
 * PropertyHelper.java
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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropertyHelper {

	public static PropertyDescriptor getPropertyDescriptorByName(final Class<?> clazz, final String name){
		PropertyDescriptor[] propertyDescriptors;
		try {
			propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new RuntimeException("Error introspecting class",e);
		}
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if(propertyDescriptor.getName().equals(name)){
				return propertyDescriptor;
			}
		}
		throw new PropertyNotFoundException("property: " + name + " not found on class: " + clazz.getName());
	}
	
	public static class PropertyNotFoundException extends RuntimeException {
		
		public PropertyNotFoundException(final String message) {
			super(message);
		}
	}
	
	public static void setFieldValue(final Field field, final Object obj, final Object value){
		if(field == null){
			throw new NullPointerException("field must not be null");
		}
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			throw new CannotSetPropertyException("Cannot set value for field: " + field.getName() + " on class: " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new CannotSetPropertyException("Cannot set value for field: " + field.getName() + " on class: " + obj.getClass().getName(), e);
		}
	}
	
	public static void setPropertyValue(final PropertyDescriptor propertyDescriptor, final Object obj, final Object value){
		if(propertyDescriptor == null){
			throw new NullPointerException("propertyDescriptor must not be null");
		}
		setPropertyValue(propertyDescriptor.getWriteMethod(), obj, value);
	}
	
	public static void setPropertyValue(final Method writeMethod, final Object obj, final Object value){
		
		if(writeMethod == null){
			throw new NullPointerException("writeMethod must not be null");
		}
		
		try {
			writeMethod.invoke(obj, value);
		} catch (IllegalArgumentException e) {
			throw new CannotSetPropertyException("Cannot set value using method: " + writeMethod.getName() + " on class: " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new CannotSetPropertyException("Cannot set value using method: " + writeMethod.getName() + " on class: " + obj.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new CannotSetPropertyException("Cannot set value using method: " + writeMethod.getName() + " on class: " + obj.getClass().getName(), e);
		}
	}
	
	public static class CannotSetPropertyException extends RuntimeException {
		public CannotSetPropertyException(final String message, final Throwable e) {
			super(message, e);
		}
	}
	
	public static Object getFieldValue(final Field field, final Object obj) {
		if(field == null){
			throw new NullPointerException("field must not be null");
		}
		try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new CannotGetPropertyException("Cannot get value for field: " + field.getName() + " on class: " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new CannotGetPropertyException("Cannot get value for field: " + field.getName() + " on class: " + obj.getClass().getName(), e);
		}
	}
	
	public static Object getPropertyValue(final PropertyDescriptor propertyDescriptor, final Object obj) {
		if(propertyDescriptor == null){
			throw new NullPointerException("propertyDescriptor must not be null");
		}
		return getPropertyValue(propertyDescriptor.getReadMethod(), obj);
	}
	
	public static Object getPropertyValue(final Method getMethod, final Object obj) {
		if(getMethod == null){
			throw new NullPointerException("getMethod must not be null");
		}
		try {
			return getMethod.invoke(obj);
		} catch (IllegalArgumentException e) {
			throw new CannotGetPropertyException("Cannot get value using method: " + getMethod.getName() + " on class: " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new CannotGetPropertyException("Cannot get value using method: " + getMethod.getName() + " on class: " + obj.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new CannotGetPropertyException("Cannot get value using method: " + getMethod.getName() + " on class: " + obj.getClass().getName(), e);
		}
	}
	
	public static class CannotGetPropertyException extends RuntimeException {
		public CannotGetPropertyException(final String message, final Throwable e) {
			super(message, e);
		}
	}
	
	
}
