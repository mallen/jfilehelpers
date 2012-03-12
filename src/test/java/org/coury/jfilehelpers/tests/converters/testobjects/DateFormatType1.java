package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldFormat;

@DelimitedRecord
public class DateFormatType1 {
	public int orderID;
	public int employeeID;
	
	public DateFormatType1() {
		
	}
	
	@FieldFormat("d-M-yyyy")
	public Date orderDate;
	@FieldFormat("MMddyyyy")
	public Date requiredDate;
	@FieldFormat("d/M/yy")
	public Date shippedDate;
}
