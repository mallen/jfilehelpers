package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldConverter;
import org.coury.jfilehelpers.enums.ConverterKind;

@DelimitedRecord(",")
public class DateFormatType3 {
	public int orderID;
	public int employeeID;
	//no @FieldConverter annotation so will use default date converter 
	public Date orderDate;
	@FieldConverter(converter=ConverterKind.Date, format="MMddyyyy")
	public Date requiredDate;
	@FieldConverter(converter=ConverterKind.Date, format="M/d/yy")
	public Date shippedDate;
}
