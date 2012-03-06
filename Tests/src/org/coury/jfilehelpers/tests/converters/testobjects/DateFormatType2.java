package org.coury.jfilehelpers.tests.converters.testobjects;

import java.util.Date;

import org.coury.jfilehelpers.annotations.DateFormatOptions;
import org.coury.jfilehelpers.annotations.DelimitedRecord;
import org.coury.jfilehelpers.annotations.FieldConverter;
import org.coury.jfilehelpers.enums.ConverterKind;

@DelimitedRecord(",")
public class DateFormatType2 {
	public int orderID;
	public int employeeID;
	@FieldConverter(converter=ConverterKind.Date)
	@DateFormatOptions(format="M-d-yyyy")
	public Date orderDate;
	@FieldConverter(converter=ConverterKind.Date)
	@DateFormatOptions(format="MMddyyyy")
	public Date requiredDate;
	@FieldConverter(converter=ConverterKind.Date)
	@DateFormatOptions(format="M/d/yy")
	public Date shippedDate;
}
