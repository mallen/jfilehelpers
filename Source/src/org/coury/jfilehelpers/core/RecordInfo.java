/*
 * RecordInfo.java
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

package org.coury.jfilehelpers.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.coury.jfilehelpers.annotations.FixedLengthRecord;
import org.coury.jfilehelpers.annotations.IgnoreCommentedLines;
import org.coury.jfilehelpers.annotations.IgnoreEmptyLines;
import org.coury.jfilehelpers.annotations.IgnoreFirst;
import org.coury.jfilehelpers.annotations.IgnoreLast;
import org.coury.jfilehelpers.annotations.PostReadRecord;
import org.coury.jfilehelpers.annotations.PostReadRecordHandler;
import org.coury.jfilehelpers.converters.BigDecimalConverterProvider;
import org.coury.jfilehelpers.converters.BooleanConverterProvider;
import org.coury.jfilehelpers.converters.ConverterProvider;
import org.coury.jfilehelpers.converters.DateTimeConverterProvider;
import org.coury.jfilehelpers.converters.DoubleConverterProvider;
import org.coury.jfilehelpers.converters.EnumConverterProvider;
import org.coury.jfilehelpers.converters.FloatConverterProvider;
import org.coury.jfilehelpers.converters.IntConverterProvider;
import org.coury.jfilehelpers.converters.LongConverterProvider;
import org.coury.jfilehelpers.converters.StringConverterProvider;
import org.coury.jfilehelpers.engines.LineInfo;
import org.coury.jfilehelpers.enums.RecordCondition;
import org.coury.jfilehelpers.fields.FieldBase;
import org.coury.jfilehelpers.fields.FieldFactory;
import org.coury.jfilehelpers.fields.FixedLengthField;
import org.coury.jfilehelpers.helpers.ConditionHelper;
import org.coury.jfilehelpers.helpers.ConstructorHelper;
import org.coury.jfilehelpers.helpers.StringHelper;
import org.coury.jfilehelpers.interfaces.NotifyRead;
import org.coury.jfilehelpers.interfaces.NotifyWrite;

/**
 * Information about one record of information. Keep field types and its values
 * and settings for importing/exporting from this records.
 *  
 * @author Felipe Gon�alves Coury <felipe.coury@gmail.com>
 * @param <T> Type of the record
 */
public final class RecordInfo<T> {
	private FieldBase[] fields;
	private final Class<T> recordClass;
	private Constructor<T> recordConstructor;

	private int ignoreFirst = 0;
	private int ignoreLast = 0;
	private boolean ignoreEmptyLines = false;
	private boolean trimBeforeEmptyLineCheck = false;
	private PostReadRecordHandler<T> postReadRecordHandler = null;
	private String commentMarker = null;
	private boolean commentAnyPlace = true;
	private RecordCondition recordCondition = RecordCondition.None;
	private String recordConditionSelector = "";

	private boolean notifyRead = false;
	private boolean notifyWrite = false;
	private String conditionRegEx = null;
	
	private int sizeHint = 32;
	
	//private ConverterBase converterProvider = null;
	private final List<ConverterProvider> converterProviders = new ArrayList<ConverterProvider>(); 
	private int fieldCount;
	private final DateTimeConverterProvider dateTimeConverterProvider;
	
	
	public RecordInfo(final Class<T> recordClass) {
		converterProviders.add(new StringConverterProvider());
		converterProviders.add(new BooleanConverterProvider());
		converterProviders.add(new IntConverterProvider());
		converterProviders.add(new LongConverterProvider());
		converterProviders.add(new DoubleConverterProvider());
		converterProviders.add(new FloatConverterProvider());
		converterProviders.add(new BigDecimalConverterProvider());
		converterProviders.add(new EnumConverterProvider());
		dateTimeConverterProvider = new DateTimeConverterProvider();
		converterProviders.add(dateTimeConverterProvider);
		
		this.recordClass = recordClass;
		initFields();
	}
		
	public DateTimeConverterProvider getDateTimeConverterProvider() {
		return dateTimeConverterProvider;
	}


	/**
	 * Parses a text line into a record object
	 * 
	 * @param line current text line extracted from file 
	 * @return parsed object
	 */
	public T strToRecord(final LineInfo line) {
		if (mustIgnoreLine(line.getLineStr())) {
			return null;
		}

		Object[] values = new Object[fieldCount];

		// array that holds the fields values
		T record = null;
		try {
			for (int i = 0; i < fieldCount; i++) {
				values[i] = fields[i].extractValue(line);
			}
	
			record = createRecordObject();
			for (int i = 0; i < fieldCount; i++) {
				// sets the field on the object
				setInternalField(fields[i].getFieldInfo().getName(), record, values[i]);
//				Field f = record.getClass().getDeclaredField(fields[i].getFieldInfo().getName());
//				f.set(record, values[i]);
				// fields[i].getFieldInfo().set(record, values[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Problems while reading field values from " + record + " object", e);
		}
		
		return record;
		
		// TODO Improve
//		CreateAssingMethods();
//
//        try
//        {
//            // Asign all values via dinamic method that creates an object and assign values
//           return mCreateHandler(mValues);
//        }
//        catch (InvalidCastException)
//        {
//            // Occurrs when the a custom converter returns an invalid value for the field.
//            for (int i = 0; i < mFieldCount; i++)
//            {
//                if (mValues[i] != null && ! mFields[i].mFieldType.IsInstanceOfType(mValues[i]))
//                    throw new ConvertException(null, mFields[i].mFieldType, mFields[i].mFieldInfo.Name, line.mReader.LineNumber, -1, "The converter for the field: " + mFields[i].mFieldInfo.Name + " returns an object of Type: " + mValues[i].GetType().Name + " and the field is of type: " + mFields[i].mFieldType.Name);
//            }
//            return null;
//        }
	}
	
	static Object getInternalField(final String fieldName, final Object target) {
	    Object value = AccessController
	            .doPrivileged(new PrivilegedAction<Object>() {
	                @Override
					public Object run() {
	                    Object result = null;
	                    java.lang.reflect.Field field = null;
	                    try {
	                        field = target.getClass().getDeclaredField(
	                                fieldName);
	                        field.setAccessible(true);
	                        result = field.get(target);
	                    } catch (Exception e1) {
	                        return null;
	                    }
	                    return result;
	                }
	            });
	    return value;
	}	
	
	static void setInternalField(final String fieldName, final Object target, final Object value) {
	    AccessController.doPrivileged(new PrivilegedAction<Object>() {
	                @Override
					public Object run() {
	                    Object result = null;
	                    java.lang.reflect.Field field = null;
	                    try {
	                        field = target.getClass().getDeclaredField(
	                                fieldName);
	                        field.setAccessible(true);
	                        field.set(target, value);
	                    } catch (Exception e1) {
	                        return null;
	                    }
	                    return result;
	                }
	            });
	}	
	
	/**
	 * Creates a string representation of the record object
	 * 
	 * @param record the record object
	 * @return string representation of the record object, respecting rules defined
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String recordToStr(final T record) throws IllegalArgumentException, IllegalAccessException {
		StringBuffer sb = new StringBuffer(this.sizeHint);
		
		Object[] values = new Object[fieldCount];
		for (int i = 0; i < fieldCount; i++) {
//			values[i] = fields[i].getFieldInfo().get(record);
			values[i] = getInternalField(fields[i].getFieldInfo().getName(), record);

		}

		for (int i = 0; i < fieldCount; i++) {
			fields[i].assignToString(sb, values[i]);
		}
		
		return sb.toString();
	}
	
	/**
	 * Instantiates a new object of the record class type
	 * @return the newly instatiated object
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private T createRecordObject() throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			return recordConstructor.newInstance();
		}
		catch (IllegalArgumentException e) {
			Object parameter = recordClass.getEnclosingClass().newInstance();
			return recordConstructor.newInstance(parameter);
		}
	}
		
	/**
	 * Verifies if current line should be ignored
	 * 
	 * @param line line to be examined
	 * @return true or false indicating if passed line should be ignored
	 */
	private boolean mustIgnoreLine(final String line) {
		if (ignoreEmptyLines) {
			if(line.length() == 0){
				return true;
			}
			if (trimBeforeEmptyLineCheck && line.trim().length() == 0) {
				return true;
			}
		}
		
		if (commentMarker != null && commentMarker.length() > 0) {
			if (commentAnyPlace && line.trim().startsWith(commentMarker) || line.startsWith(commentMarker)) {
				return true;
			}
		}
		
		switch (recordCondition) {
		case ExcludeIfBegins:
			return ConditionHelper.beginsWith(line, recordConditionSelector);
			
		case IncludeIfBegins:
			return !ConditionHelper.beginsWith(line, recordConditionSelector);
			
		case ExcludeIfContains:
			return ConditionHelper.contains(line, recordConditionSelector);
			
		case IncludeIfContains:
			return !ConditionHelper.contains(line, recordConditionSelector);
			
		case ExcludeIfEnclosed:
			return ConditionHelper.enclosed(line, recordConditionSelector);
			
		case IncludeIfEnclosed:
			return !ConditionHelper.enclosed(line, recordConditionSelector);
			
		case ExcludeIfEnds:
			return ConditionHelper.endsWith(line, recordConditionSelector);
			
		case IncludeIfEnds:
			return !ConditionHelper.endsWith(line, recordConditionSelector);
			
		case ExcludeIfMatchRegex:
			return Pattern.matches(conditionRegEx, line);
			
		case IncludeIfMatchRegex:
			return !Pattern.matches(conditionRegEx, line);
			
		}
		
		return false;
	}

	/**
	 * Initiate the values of member fields by looking for annotations on the
	 * record object that changes behavior
	 */
	private void initFields() {
		IgnoreFirst igf = recordClass.getAnnotation(IgnoreFirst.class);
		if (igf != null) {
			this.ignoreFirst = igf.lines();
		}
		
		IgnoreLast igl = recordClass.getAnnotation(IgnoreLast.class);
		if (igl != null) {
			this.ignoreFirst = igl.lines();
		}
		
		IgnoreEmptyLines iel = recordClass.getAnnotation(IgnoreEmptyLines.class);
		if(iel != null){
			this.ignoreEmptyLines = true;
			this.trimBeforeEmptyLineCheck = iel.trimBeforeCheck();
		}
		
		IgnoreCommentedLines igc = recordClass.getAnnotation(IgnoreCommentedLines.class);
		if (igc != null) {
			this.commentMarker = igc.commentMarker();
			this.commentAnyPlace = igc.anyPlace();
		}
		
		PostReadRecord arr = recordClass.getAnnotation(PostReadRecord.class);
		if(arr != null){
			Constructor<?> constructor = ConstructorHelper.getPublicEmptyConstructor(arr.handlerClass());
			try {
				@SuppressWarnings("unchecked")
				PostReadRecordHandler<T> temp = (PostReadRecordHandler<T>) constructor.newInstance();
				postReadRecordHandler = temp;
			} catch (InstantiationException e) {
				throw new RuntimeException("Error creating handler (1)", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error creating handler (2)", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Error creating handler (3)", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Error creating handler (4)", e);
			}
		}
		
		// TODO ConditionalRecord

		if(NotifyRead.class.isAssignableFrom(recordClass)) {
			notifyRead = true;
		}

		if(NotifyWrite.class.isAssignableFrom(recordClass)) {
			notifyWrite = true;
		}
		
		recordConstructor = ConstructorHelper.getPublicEmptyConstructor(recordClass);
		
		fields = createCoreFields(recordClass.getDeclaredFields(), recordClass);
		fieldCount = this.fields.length;
		
		if (isFixedLength()) {
			sizeHint = 0;
			for (int i = 0; i < fieldCount; i++) {
				sizeHint += ((FixedLengthField) fields[i]).getFieldLength();
			}
		}
		
		if (fieldCount == 0) {
			throw new IllegalArgumentException("The record class " + recordClass.getName() + " don't contains any field.");
		}
	}

	

	/**
	 * Indicates if this record is of fixed length
	 * @return true if record is fixed length, false otherwise
	 */
	private boolean isFixedLength() {
		return recordClass.isAnnotationPresent(FixedLengthRecord.class);
	}
	
	/**
	 * Creates field descriptors for each field of the record class 
	 * 
	 * @param fields current fields of the record class, acquired via reflection
	 * @param recordClass the record class
	 * @return an array of FieldBase, field descriptor objects
	 */
	@SuppressWarnings("unchecked")
	private FieldBase[] createCoreFields(final Field[] fields, final Class recordClass) {
		FieldBase field;
		List<FieldBase> fieldArr = new ArrayList<FieldBase>();
		
		boolean someOptional = false;
		for (Field f : fields) {
			field = FieldFactory.createField(f, recordClass, someOptional, converterProviders);
			if (field != null) {
				someOptional = field.isOptional();
				fieldArr.add(field);
				if (fieldArr.size() > 1) {
					fieldArr.get(fieldArr.size() - 2).setNextOptional(fieldArr.get(fieldArr.size() - 1).isOptional());
				}
			}
		}
		
		if (fieldArr.size() > 0) {
			fieldArr.get(0).setFirst(true);
			fieldArr.get(fieldArr.size() - 1).setLast(true);
		}
		
		return fieldArr.toArray(new FieldBase[] {});
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringHelper.toStringBuilder(this);
	}

	public FieldBase[] getFields() {
		return fields;
	}

	public void setFields(final FieldBase[] fields) {
		this.fields = fields;
	}

	public int getIgnoreFirst() {
		return ignoreFirst;
	}

	public void setIgnoreFirst(final int ignoreFirst) {
		this.ignoreFirst = ignoreFirst;
	}

	public int getIgnoreLast() {
		return ignoreLast;
	}

	public void setIgnoreLast(final int ignoreLast) {
		this.ignoreLast = ignoreLast;
	}

	public boolean isIgnoreEmptyLines() {
		return ignoreEmptyLines;
	}

	public void setIgnoreEmptyLines(final boolean ignoreEmptyLines) {
		this.ignoreEmptyLines = ignoreEmptyLines;
	}

	public boolean isIgnoreEmptySpaces() {
		return trimBeforeEmptyLineCheck;
	}

	public void setIgnoreEmptySpaces(final boolean ignoreEmptySpaces) {
		this.trimBeforeEmptyLineCheck = ignoreEmptySpaces;
	}

	public String getCommentMarker() {
		return commentMarker;
	}

	public void setCommentMarker(final String commentMaker) {
		this.commentMarker = commentMaker;
	}

	public boolean isCommentAnyPlace() {
		return commentAnyPlace;
	}

	public void setCommentAnyPlace(final boolean commentAnyPlace) {
		this.commentAnyPlace = commentAnyPlace;
	}

	public RecordCondition getRecordCondition() {
		return recordCondition;
	}

	public void setRecordCondition(final RecordCondition recordCondition) {
		this.recordCondition = recordCondition;
	}

	public String getRecordConditionSelector() {
		return recordConditionSelector;
	}

	public void setRecordConditionSelector(final String recordConditionSelector) {
		this.recordConditionSelector = recordConditionSelector;
	}

	public boolean isNotifyRead() {
		return notifyRead;
	}

	public void setNotifyRead(final boolean notifyRead) {
		this.notifyRead = notifyRead;
	}

	public boolean isNotifyWrite() {
		return notifyWrite;
	}

	public void setNotifyWrite(final boolean notifyWrite) {
		this.notifyWrite = notifyWrite;
	}

	public String getConditionRegEx() {
		return conditionRegEx;
	}

	public void setConditionRegEx(final String conditionRegEx) {
		this.conditionRegEx = conditionRegEx;
	}

	public int getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(final int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public Constructor<T> getRecordConstructor() {
		return recordConstructor;
	}

	public void setRecordConstructor(final Constructor<T> recordConstructor) {
		this.recordConstructor = recordConstructor;
	}

	public PostReadRecordHandler<T> getPostReadRecordHandler() {
		return postReadRecordHandler;
	}

	public void setPostReadRecordHandler(final PostReadRecordHandler<T> postReadRecordHandler) {
		this.postReadRecordHandler = postReadRecordHandler;
	}
	
}
