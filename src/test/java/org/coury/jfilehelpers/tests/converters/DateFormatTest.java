/*
 * DateFormatTest.java
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

package org.coury.jfilehelpers.tests.converters;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.coury.jfilehelpers.tests.common.Common;
import org.coury.jfilehelpers.tests.converters.testobjects.DateFormatType1;
import org.coury.jfilehelpers.tests.converters.testobjects.DateFormatType2;
import org.coury.jfilehelpers.tests.converters.testobjects.DateFormatType3;


public class DateFormatTest extends TestCase {
	
	public void test() throws ParseException{
		//DateConverter converter = new DateConverter("dd/MM/yyyy HH:mm");
		//Date parsed = (Date) converter.stringToField("25/03/2012 01:45");
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		format.setLenient(false);
		format.parse("25/03/2012 01:45");
		
//		DateFormat format2 = DateFormat.getDateTimeInstance();
//		((SimpleDateFormat) format2).applyPattern("dd/MM/yyyy HH:mm");
//		format2.setLenient(false);
//		format2.parse("25/03/2012 01:45");
		
	}
	
	
	public void testDifferentSpanishFormat() throws IOException {
		FileHelperEngine<DateFormatType1> engine = new FileHelperEngine<DateFormatType1>(DateFormatType1.class);

		List<DateFormatType1> res = engine.readResource("/Good/DateFormat1.txt");
		assertEquals(6, res.size());
		
		Common.assertSameDate(getDate(1996, 7, 4), res.get(0).orderDate);
		Common.assertSameDate(getDate(1996, 7, 5), res.get(1).orderDate);
		Common.assertSameDate(getDate(1996, 7, 8), res.get(2).orderDate);
		Common.assertSameDate(getDate(1996, 7, 8), res.get(3).orderDate);

		Common.assertSameDate(getDate(1996, 8, 1), res.get(0).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 16), res.get(1).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 5), res.get(2).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 5), res.get(3).requiredDate);

		Common.assertSameDate(getDate(1996, 7, 16), res.get(0).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 10), res.get(1).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 12), res.get(2).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 15), res.get(3).shippedDate);
	}
	
	public void testDifferentEnglishFormat() throws IOException {
		FileHelperEngine<DateFormatType2> engine = new FileHelperEngine<DateFormatType2>(DateFormatType2.class);

		List<DateFormatType2> res = engine.readResource("/Good/DateFormat2.txt");
		assertEquals(6, res.size());

		Common.assertSameDate(getDate(1996, 7, 4), res.get(0).orderDate);
		Common.assertSameDate(getDate(1996, 7, 5), res.get(1).orderDate);
		Common.assertSameDate(getDate(1996, 7, 8), res.get(2).orderDate);
		Common.assertSameDate(getDate(1996, 7, 8), res.get(3).orderDate);

		Common.assertSameDate(getDate(1996, 8, 1), res.get(0).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 16), res.get(1).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 5), res.get(2).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 5), res.get(3).requiredDate);

		Common.assertSameDate(getDate(1996, 7, 16), res.get(0).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 10), res.get(1).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 12), res.get(2).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 15), res.get(3).shippedDate);		
	}
	

	public void testDifferentEnglishFormat_usingDefaultFormat() throws IOException {
		FileHelperEngine<DateFormatType3> engine = new FileHelperEngine<DateFormatType3>(DateFormatType3.class);
		
		engine.setDefaultDateFormat("M-d-yyyy");

		List<DateFormatType3> res = engine.readResource("/Good/DateFormat2.txt");
		assertEquals(6, res.size());

		Common.assertSameDate(getDate(1996, 7, 4), res.get(0).orderDate);
		Common.assertSameDate(getDate(1996, 7, 5), res.get(1).orderDate);
		Common.assertSameDate(getDate(1996, 7, 8), res.get(2).orderDate);
		Common.assertSameDate(getDate(1996, 7, 8), res.get(3).orderDate);

		Common.assertSameDate(getDate(1996, 8, 1), res.get(0).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 16), res.get(1).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 5), res.get(2).requiredDate);
		Common.assertSameDate(getDate(1996, 8, 5), res.get(3).requiredDate);

		Common.assertSameDate(getDate(1996, 7, 16), res.get(0).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 10), res.get(1).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 12), res.get(2).shippedDate);
		Common.assertSameDate(getDate(1996, 7, 15), res.get(3).shippedDate);		
	}
	
	private static Date getDate(final int y, final int m, final int d) {
		Calendar c = Calendar.getInstance();
		c.set(y, m-1, d, 0, 0, 0);
		return c.getTime();
	}
}
