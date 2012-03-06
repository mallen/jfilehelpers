package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.FieldFormat;
import org.coury.jfilehelpers.annotations.DelimitedRecord;

@DelimitedRecord(",")
public class DateFormatType2 {
	public int orderID;
	public int employeeID;
	@FieldFormat(format="M-d-yyyy")
	public Date orderDate;
	@FieldFormat(format="MMddyyyy")
	public Date requiredDate;
	@FieldFormat(format="M/d/yy")
	public Date shippedDate;
}
