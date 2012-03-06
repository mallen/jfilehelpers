package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.FieldFormat;
import org.coury.jfilehelpers.annotations.DelimitedRecord;

@DelimitedRecord
public class DateFormatType1 {
	public int orderID;
	public int employeeID;
	
	public DateFormatType1() {
		
	}
	
	@FieldFormat(format="d-M-yyyy")
	public Date orderDate;
	@FieldFormat(format="MMddyyyy")
	public Date requiredDate;
	@FieldFormat(format="d/M/yy")
	public Date shippedDate;
}
