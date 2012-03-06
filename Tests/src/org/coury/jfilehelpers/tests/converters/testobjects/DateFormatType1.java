package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DateFormatOptions;
import org.coury.jfilehelpers.annotations.DelimitedRecord;

@DelimitedRecord
public class DateFormatType1 {
	public int orderID;
	public int employeeID;
	
	public DateFormatType1() {
		
	}
	
	@DateFormatOptions(format="d-M-yyyy")
	public Date orderDate;
	@DateFormatOptions(format="MMddyyyy")
	public Date requiredDate;
	@DateFormatOptions(format="d/M/yy")
	public Date shippedDate;
}
