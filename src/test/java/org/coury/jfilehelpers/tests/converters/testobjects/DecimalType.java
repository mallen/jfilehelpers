package org.coury.jfilehelpers.tests.converters.testobjects;

import java.math.BigDecimal;

import org.coury.jfilehelpers.annotations.DelimitedRecord;

@DelimitedRecord("|")
public class DecimalType {
	public int intField;
	public float floatField;
	public double doubleField;
	public BigDecimal decimalField;
}
