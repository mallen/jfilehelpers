package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DateFormatOptions;
import org.coury.jfilehelpers.annotations.DelimitedRecord;

@DelimitedRecord(",")
public class DateFormatType2 {
	public int orderID;
	public int employeeID;
	@DateFormatOptions(format="M-d-yyyy")
	public Date orderDate;
	@DateFormatOptions(format="MMddyyyy")
	public Date requiredDate;
	@DateFormatOptions(format="M/d/yy")
	public Date shippedDate;
}
