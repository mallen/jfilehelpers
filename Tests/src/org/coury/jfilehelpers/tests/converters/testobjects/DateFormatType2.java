package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldFormat;

@DelimitedRecord(",")
public class DateFormatType2 {
	public int orderID;
	public int employeeID;
	@FieldFormat("M-d-yyyy")
	public Date orderDate;
	@FieldFormat("MMddyyyy")
	public Date requiredDate;
	@FieldFormat("M/d/yy")
	public Date shippedDate;
}
