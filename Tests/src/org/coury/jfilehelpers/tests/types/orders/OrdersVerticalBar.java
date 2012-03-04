/*
 * OrdersVerticalBar.java
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
package org.coury.jfilehelpers.tests.types.orders;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldNullValue;

@DelimitedRecord("|")
public class OrdersVerticalBar {

	public int orderID;
	public String customerID;
	public int employeeID;
	public Date orderDate;
	public Date requiredDate;		
	@FieldNullValue("2005-1-1") 
	public Date shippedDate;
	public int shipVia;
	public float freight;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerID == null) ? 0 : customerID.hashCode());
		result = prime * result + employeeID;
		result = prime * result + Float.floatToIntBits(freight);
		result = prime * result
				+ ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + orderID;
		result = prime * result
				+ ((requiredDate == null) ? 0 : requiredDate.hashCode());
		result = prime * result + shipVia;
		result = prime * result
				+ ((shippedDate == null) ? 0 : shippedDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdersVerticalBar other = (OrdersVerticalBar) obj;
		if (customerID == null) {
			if (other.customerID != null)
				return false;
		} else if (!customerID.equals(other.customerID))
			return false;
		if (employeeID != other.employeeID)
			return false;
		if (Float.floatToIntBits(freight) != Float
				.floatToIntBits(other.freight))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (orderDate.getTime() != (other.orderDate.getTime()))
			return false;
		if (orderID != other.orderID)
			return false;
		if (requiredDate == null) {
			if (other.requiredDate != null)
				return false;
		} else if (!requiredDate.equals(other.requiredDate))
			return false;
		if (shipVia != other.shipVia)
			return false;
		if (shippedDate == null) {
			if (other.shippedDate != null)
				return false;
		} else if (!shippedDate.equals(other.shippedDate))
			return false;
		return true;
	}
	
	

}