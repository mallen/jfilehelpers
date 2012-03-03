/*
 * PropertyUtilsTest.java
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
package org.coury.jfilehelpers.tests.helpers;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.coury.jfilehelpers.helpers.PropertyHelper;
import org.coury.jfilehelpers.helpers.PropertyHelper.PropertyNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class PropertyHelperTest {
	
	@Test
	public void canFindPropertyByName(){
		PropertyDescriptor found = PropertyHelper.getPropertyDescriptorByName(PropertyHelperTestObject.class, "wibble");
		assertNotNull(found);
	}
	
	@Test(expected=PropertyNotFoundException.class)
	public void throwsWhenPropertyNotFound(){
		PropertyHelper.getPropertyDescriptorByName(PropertyHelperTestObject.class, "foo");
		fail("should have thrown error");
	}
	
	@Test
	public void canReadPropertyValue(){
		PropertyHelperTestObject obj = new PropertyHelperTestObject();
		obj.setWibble("123456");
		PropertyDescriptor found = PropertyHelper.getPropertyDescriptorByName(PropertyHelperTestObject.class, "wibble");
		String read = (String) PropertyHelper.getPropertyValue(found, obj);
		assertEquals("123456", read);
	}
	
	@Test
	public void canReadPropertyValue2(){
		PropertyHelperTestObject obj = new PropertyHelperTestObject();
		obj.setWobble(123456);
		PropertyDescriptor found = PropertyHelper.getPropertyDescriptorByName(PropertyHelperTestObject.class, "wobble");
		int read =  (Integer) PropertyHelper.getPropertyValue(found, obj);
		assertEquals(123456, read);
	}
	
	@Test
	public void canWritePropertyValue(){
		PropertyHelperTestObject obj = new PropertyHelperTestObject();
		PropertyDescriptor found = PropertyHelper.getPropertyDescriptorByName(PropertyHelperTestObject.class, "wibble");
		PropertyHelper.setPropertyValue(found, obj, "123456");
		assertEquals("123456", obj.getWibble());
	}
	
	@Test
	public void canWritePropertyValue2(){
		PropertyHelperTestObject obj = new PropertyHelperTestObject();
		PropertyDescriptor found = PropertyHelper.getPropertyDescriptorByName(PropertyHelperTestObject.class, "wobble");
		PropertyHelper.setPropertyValue(found, obj, 123456);
		assertEquals(123456, obj.getWobble());
	}
	
	@Test
	public void canReadFieldValue() throws SecurityException, NoSuchFieldException{
		PropertyHelperTestObject obj = new PropertyHelperTestObject();
		obj.foo = "123456";
		Field field = obj.getClass().getDeclaredField("foo");
		String read = (String) PropertyHelper.getFieldValue(field, obj);
		assertEquals("123456", read);
	}
	
	@Test
	public void canSetFieldValue() throws SecurityException, NoSuchFieldException{
		PropertyHelperTestObject obj = new PropertyHelperTestObject();
		Field field = obj.getClass().getDeclaredField("foo");
		PropertyHelper.setFieldValue(field, obj, "123456");
		assertEquals("123456", obj.foo);
	}

}
