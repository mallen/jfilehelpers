/*
 * Customer.java
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

package org.coury.jfilehelpers.samples.delimited;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DateFormatOptions;
import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.helpers.StringHelper;

@DelimitedRecord(",")
public class Customer {
	private Integer custId;
	private String name;
	private Integer rating;
	
	@DateFormatOptions(format="dd-MM-yyyy")
	private Date addedDate;
	
	@Override
	public String toString() {
		String l = System.getProperty("line.separator");
		StringBuffer b = new StringBuffer();
		b.append("Customer: ").append(l);
		b.append("   custId = " + custId).append(l);
		b.append("   name = " + name).append(l);
		b.append("   rating = " + rating).append(l);
		b.append("   addedDate = " + addedDate).append(l);
		return StringHelper.toStringBuilder(this, b.toString());
	}
}
