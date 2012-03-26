package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldDateFormat;

@DelimitedRecord(",")
public class DateFormatType2 {
	public int orderID;
	public int employeeID;
	@FieldDateFormat("M-d-yyyy")
	public Date orderDate;
	@FieldDateFormat("MMddyyyy")
	public Date requiredDate;
	@FieldDateFormat("M/d/yy")
	public Date shippedDate;
}
