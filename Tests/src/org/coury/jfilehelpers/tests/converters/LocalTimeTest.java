/*
 * LocalTimeTest.java
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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.coury.jfilehelpers.tests.converters.testobjects.LocalTimeType;
import org.joda.time.LocalTime;
import org.junit.Test;

public class LocalTimeTest {

	@Test
	public void test() throws IOException {
		FileHelperEngine<LocalTimeType> engine = new FileHelperEngine<LocalTimeType>(LocalTimeType.class);
 
		List<LocalTimeType> res = engine.readResource("/test/Good/LocalTime.txt");
		assertEquals(3, res.size());
		assertEquals(new LocalTime(23, 59, 0), res.get(0).time);
		assertEquals(new LocalTime(0, 0, 0), res.get(1).time);
		assertEquals(new LocalTime(14, 23, 0), res.get(2).time);
	}

}
