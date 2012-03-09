/*
 * HeaderVerticalBar.java
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
package org.coury.jfilehelpers.tests.masterdetail;

import org.coury.jfilehelpers.annotations.DelimitedRecord;

@DelimitedRecord("|")
public class HeaderVerticalBar {
	private String headerText;
	private int headerNumber;
	
	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public int getHeaderNumber() {
		return headerNumber;
	}

	public void setHeaderNumber(int headerNumber) {
		this.headerNumber = headerNumber;
	}

	@Override
	public boolean equals(final Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(!(obj instanceof HeaderVerticalBar)){
			return false;
		}
		
		HeaderVerticalBar other = (HeaderVerticalBar) obj;
		
		return other.headerText.equals(headerText) && other.headerNumber == headerNumber;
	}
	
	@Override
	public int hashCode() {
		
		return (headerText == null || headerText.equals("") ? 1 : headerText.hashCode()) * headerNumber;
	}
}
