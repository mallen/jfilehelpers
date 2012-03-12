package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldFormat;

@DelimitedRecord(",")
public class DateFormatType3 {
	public int orderID;
	public int employeeID;
	//no @FieldConverter annotation so will use default date converter 
	public Date orderDate;
	//no converter kind annotation
	@FieldFormat("MMddyyyy")
	public Date requiredDate;
	@FieldFormat("M/d/yy")
	public Date shippedDate;
}
