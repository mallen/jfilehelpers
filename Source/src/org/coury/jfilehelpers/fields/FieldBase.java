/*
 * FieldBase.java
 *
 * Copyright (C) 2007 Felipe Gonçalves Coury <felipe.coury@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.coury.jfilehelpers.fields;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.coury.jfilehelpers.annotations.FieldNullValue;
import org.coury.jfilehelpers.converters.ConverterBase;
import org.coury.jfilehelpers.converters.ConverterProvider;
import org.coury.jfilehelpers.core.ExtractedInfo;
import org.coury.jfilehelpers.engines.LineInfo;
import org.coury.jfilehelpers.enums.TrimMode;
import org.coury.jfilehelpers.helpers.NumberHelper;
import org.coury.jfilehelpers.helpers.PropertyHelper;
import org.coury.jfilehelpers.helpers.StringHelper;

public abstract class FieldBase {
	private Field field;

	private TrimMode trimMode = TrimMode.None;
	private char[] trimChars = null;
	private boolean isOptional = false;
	private boolean nextOptional = false;
	private boolean inNewLine = false;

	private boolean first = false;
	private boolean last = false;
	private boolean trailingArray = false;
	private Object nullValue = null;
	private boolean nullableType = false;

	private final boolean isPublic;
	private final Method readMethod;
	private final Method writeMethod;
	protected int charsToDiscard = 0;

	private ConverterBase converter;
	private int impliedDecimalPlaces;


	public FieldBase(final Field field, 
			final List<ConverterProvider> converterProviders) {
		this.field = field;
		Class<?> fieldType = field.getType();
		
		int modifiers = field.getModifiers();
		if(Modifier.isPublic(modifiers)){
			isPublic = true;
			readMethod = null;
			writeMethod = null;
		} else {
			isPublic = false;
			PropertyDescriptor descriptor = PropertyHelper.getPropertyDescriptorByName(field.getDeclaringClass(), field.getName());
			readMethod = descriptor.getReadMethod();
			writeMethod = descriptor.getWriteMethod();
		}
		
		//get converter for field
		converter = getConverter(fieldType, converterProviders);
		
		//converter options
		Class<? extends Annotation> optionsAnnotationClass = converter.getOptionsAnnotationType();
		if(optionsAnnotationClass != null){
			Annotation annotation = field.getAnnotation(optionsAnnotationClass);
			if(annotation != null){
				converter.setOptionsFromAnnotation(annotation);
			}
		}

		FieldNullValue fn = field.getAnnotation(FieldNullValue.class);
		if (fn != null) {
			nullValue = fn.value();
		}
	}
	
	public void setValue(final Object obj, final Object value) {
		if(isPublic){
			PropertyHelper.setFieldValue(field, obj, value);
		} else {
			PropertyHelper.setPropertyValue(writeMethod, obj, value);
		}
	}
	
	public Object getValue(final Object obj){
		if(isPublic){
			return PropertyHelper.getFieldValue(field, obj);
		} else {
			return PropertyHelper.getPropertyValue(readMethod, obj);
		}
			
	}

	private ConverterBase getConverter(final Class<?> fieldType, final List<ConverterProvider> converterProviders) {

		for (ConverterProvider provider : converterProviders) {
			if (provider.handles(fieldType)) {
				return provider.createConverter(fieldType);
			}
		}
		throw new IllegalArgumentException("No ConverterProvider found for type: " + fieldType.getName());
	}

	public Object extractValue(final LineInfo line) {
		if (this.inNewLine) {
			if (!line.isEmptyFromPos()) {
				// throw new BadUsageException("Text '" + line.CurrentString +
				// "' found before the new line of the field: " +
				// mFieldInfo.Name +
				// " (this is not allowed when you use [FieldInNewLine])");
				throw new RuntimeException("Text '" + line.getCurrentString() +
						"' found before the new line of the field: " + field.getName() +
						" (this is not allowed when you use [FieldInNewLine])");
			}

			try {
				line.reload(line.getReader().readNextLine());
			} catch (IOException e) {
				throw new RuntimeException("Error reading next line", e);
			}

			if (line.getLineStr() == null) {
				// throw new
				// BadUsageException("End of stream found parsing the field " +
				// fieldInfo.getName() +
				// ". Please check the class record.");
				throw new RuntimeException("End of stream found parsing the field " + field.getName() +
						". Please check the class record.");
			}
		}

		ExtractedInfo info = extractFieldString(line);
		if (info.getCustomExtractedString() == null) {
			line.setCurrentPos(info.getExtractedTo() + 1);
		}

		line.setCurrentPos(line.getCurrentPos() + charsToDiscard);

		return assignFromString(info, line);
	}

	

	protected abstract ExtractedInfo extractFieldString(LineInfo line);

	protected abstract void createFieldString(StringBuffer sb, Object fieldValue);

	protected String baseFieldString(Object fieldValue) {

		if (impliedDecimalPlaces != 0) {
			fieldValue = NumberHelper.applyImpliedDecimalPlaces((Number) fieldValue, -1 * impliedDecimalPlaces);
		}

		return converter.fieldToString(fieldValue);
	}

	private Object assignFromString(final ExtractedInfo fieldString, final LineInfo line) {
		Object val;

		switch (trimMode) {
		case None:
			break;

		case Both:
			fieldString.trimBoth(trimChars);
			break;

		case Left:
			fieldString.trimStart(trimChars);
			break;

		case Right:
			fieldString.TrimEnd(trimChars);
			break;
		}

		if (converter.isCustomNullHandling() == false 
				&& fieldString.hasOnlyThisChars(StringHelper.WHITESPACE_CHARS)) {
			val = getNullValue();
		} else {
			String from = fieldString.extractedString();
			val = converter.stringToField(from);

			if (val == null) {
				val = getNullValue();
			}
		}

		if (impliedDecimalPlaces != 0) {
			val = NumberHelper.applyImpliedDecimalPlaces((Number) val, impliedDecimalPlaces);
		}

		return val;
	}

	

	public void assignToString(final StringBuffer sb, final Object fieldValue) {
		if (this.inNewLine == true) {
			sb.append(StringHelper.NEW_LINE);
		}

		createFieldString(sb, fieldValue);
	}

	@Override
	public String toString() {
		return StringHelper.toStringBuilder(this);
	}

	public Field getFieldInfo() {
		return field;
	}

	public void setFieldInfo(final Field fieldInfo) {
		this.field = fieldInfo;
	}

	public TrimMode getTrimMode() {
		return trimMode;
	}

	public void setTrimMode(final TrimMode trimMode) {
		this.trimMode = trimMode;
	}

	public char[] getTrimChars() {
		return trimChars;
	}

	public void setTrimChars(final char[] trimChars) {
		this.trimChars = trimChars;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(final boolean isOptional) {
		this.isOptional = isOptional;
	}

	public boolean isNextOptional() {
		return nextOptional;
	}

	public void setNextOptional(final boolean nextIsOptinal) {
		this.nextOptional = nextIsOptinal;
	}

	public boolean isInNewLine() {
		return inNewLine;
	}

	public void setInNewLine(final boolean inNewLine) {
		this.inNewLine = inNewLine;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(final boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(final boolean last) {
		this.last = last;
	}

	public boolean isTrailingArray() {
		return trailingArray;
	}

	public void setTrailingArray(final boolean trailingArray) {
		this.trailingArray = trailingArray;
	}

	public Object getNullValue() {
		return nullValue;
	}

	public void setNullValue(final Object nullValue) {
		this.nullValue = nullValue;
	}

	public boolean isNullableType() {
		return nullableType;
	}

	public void setNullableType(final boolean nullableType) {
		this.nullableType = nullableType;
	}

	public ConverterBase getConvertProvider() {
		return converter;
	}

	public void setConvertProvider(final ConverterBase converterProvider) {
		this.converter = converterProvider;
	}

	public int getImpliedDecimalPlaces() {
		return impliedDecimalPlaces;
	}

	public void setImpliedDecimalPlaces(final int impliedDecimalPlaces) {
		this.impliedDecimalPlaces = impliedDecimalPlaces;
	}
}
