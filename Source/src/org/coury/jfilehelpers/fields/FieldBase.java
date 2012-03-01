/*
 * FieldBase.java
 *
 * Copyright (C) 2007 Felipe Gon�alves Coury <felipe.coury@gmail.com>
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

import java.io.IOException;
import java.lang.reflect.Field;

import org.coury.jfilehelpers.annotations.FieldConverter;
import org.coury.jfilehelpers.annotations.FieldNullValue;
import org.coury.jfilehelpers.converters.ConvertHelpers;
import org.coury.jfilehelpers.converters.ConverterBase;
import org.coury.jfilehelpers.core.ExtractedInfo;
import org.coury.jfilehelpers.engines.LineInfo;
import org.coury.jfilehelpers.enums.TrimMode;
import org.coury.jfilehelpers.helpers.NumberHelper;
import org.coury.jfilehelpers.helpers.StringHelper;

public abstract class FieldBase {
	private boolean stringField;
	private Field fieldInfo;
	
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
	
	protected int charsToDiscard = 0;
	
	private ConverterBase convertProvider;
	private int impliedDecimalPlaces;
		
	public FieldBase(final Field field) {
		fieldInfo = field;
		stringField = field.getType().equals(String.class);
		
		FieldConverter fc = field.getAnnotation(FieldConverter.class);
		if (fc == null) {
			convertProvider = ConvertHelpers.getDefaultConverter(field);
		}
		else {
			convertProvider = ConvertHelpers.getConverter(fc.converter(), fc.format());
		}
		
		FieldNullValue fn = field.getAnnotation(FieldNullValue.class);
		if (fn != null) {
			nullValue = fn.value();
		}		
	}
	
	public Object extractValue(final LineInfo line) throws IOException {
		if (this.inNewLine) {
			if (!line.isEmptyFromPos()) {
//				throw new BadUsageException("Text '" + line.CurrentString +
//                        "' found before the new line of the field: " + mFieldInfo.Name +
//                        " (this is not allowed when you use [FieldInNewLine])");
				throw new RuntimeException("Text '" + line.getCurrentString() +
	              "' found before the new line of the field: " + fieldInfo.getName() +
	              " (this is not allowed when you use [FieldInNewLine])");
			}

			line.reload(line.getReader().readNextLine());
				
			if (line.getLineStr() == null) {
//				throw new BadUsageException("End of stream found parsing the field " + fieldInfo.getName() +
//                ". Please check the class record.");
				throw new RuntimeException("End of stream found parsing the field " + fieldInfo.getName() +
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

	public Object createValueForField(final Object fieldValue) {
		Object val = null;

		if (fieldValue == null) {
			if (nullValue == null) {
				val = null;
			}
 			else {
				val = nullValue;
			}
		}
		else
		{
			if (convertProvider == null) {
            	// TODO what to do in this case: we're trying to convert the extracted string to a field
				// val = Convert.ChangeType(fieldValue, mFieldType, null);
				val = fieldValue.toString();
			}
			else {
				//val = Convert.ChangeType(fieldValue, mFieldType, null);
				val = fieldValue.toString();
			}
		}

		return val;
	}
	
	protected abstract ExtractedInfo extractFieldString(LineInfo line);
	
	protected abstract void createFieldString(StringBuffer sb, Object fieldValue);
	
	protected String baseFieldString(Object fieldValue) {
		
		if(impliedDecimalPlaces != 0){
			fieldValue = NumberHelper.applyImpliedDecimalPlaces((Number) fieldValue, -1 * impliedDecimalPlaces);
		}
		
		if (convertProvider == null) {
			if (fieldValue == null) {
				return "";
			}
			else {
				return fieldValue.toString();
			}
		}
		else {
			return convertProvider.fieldToString(fieldValue);
		}
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
		
        if (convertProvider == null) {
            if (stringField) {
                val = fieldString.extractedString();
            }
            else {
                // Trim it to use Convert.ChangeType
                fieldString.trimBoth(StringHelper.WHITESPACE_CHARS);

                if (fieldString.length() == 0) {
                    // Empty stand for null
                    val = getNullValue();
                }
                else {
                	// TODO what to do in this case: we're trying to convert the extracted string to a field
                    //val = Convert.ChangeType(fieldString.extractedString(), fieldInfo., null);
                	val = changeType(fieldString.extractedString(), fieldInfo);
                }
            }
        }
        else {
            if (convertProvider.isCustomNullHandling() == false &&
                fieldString.hasOnlyThisChars(StringHelper.WHITESPACE_CHARS)) {
                val = getNullValue();
            }
            else {
                String from = fieldString.extractedString();
                val = convertProvider.stringToField(from);

                if (val == null) {
                    val = getNullValue();
                }
            }
        }
        
        if(impliedDecimalPlaces != 0){
        	val = NumberHelper.applyImpliedDecimalPlaces((Number) val, impliedDecimalPlaces);
        }
		
        return val;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Object changeType(final String s, final Field fieldInfo) {
		
		Number number = NumberHelper.convertStringToNumber(s, fieldInfo.getType());
		if(number != null){
			return number;
		}
		
		 if (fieldInfo.getType().isEnum()) {
			Object ret = null;
			try {
				System.out.println(s);
				ret = Enum.valueOf((Class<Enum>) fieldInfo.getType(), s); 
			}
			catch (IllegalArgumentException e) {
			}
			return ret;
		}
		return fieldInfo.getType().cast(s);
		//return s.getClass().cast(fieldInfo.getType());
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
	
	public boolean isStringField() {
		return stringField;
	}

	public void setStringField(final boolean stringField) {
		this.stringField = stringField;
	}

	public Field getFieldInfo() {
		return fieldInfo;
	}

	public void setFieldInfo(final Field fieldInfo) {
		this.fieldInfo = fieldInfo;
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
		return convertProvider;
	}

	public void setConvertProvider(final ConverterBase converterProvider) {
		this.convertProvider = converterProvider;
	}

	
	public int getImpliedDecimalPlaces() {
		return impliedDecimalPlaces;
	}

	public void setImpliedDecimalPlaces(final int impliedDecimalPlaces) {
		this.impliedDecimalPlaces = impliedDecimalPlaces;
	}
}
