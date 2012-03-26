package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldDateFormat;

@DelimitedRecord
public class DateFormatType1 {
	public int orderID;
	public int employeeID;
	
	public DateFormatType1() {
		
	}
	
	@FieldDateFormat("d-M-yyyy")
	public Date orderDate;
	@FieldDateFormat("MMddyyyy")
	public Date requiredDate;
	@FieldDateFormat("d/M/yy")
	public Date shippedDate;
}
