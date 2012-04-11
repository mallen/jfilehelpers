/*
 * EngineBase.java
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

package org.coury.jfilehelpers.engines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.coury.jfilehelpers.converters.BigDecimalConverterProvider;
import org.coury.jfilehelpers.converters.BooleanConverterProvider;
import org.coury.jfilehelpers.converters.ConverterProvider;
import org.coury.jfilehelpers.converters.DateConverterProvider;
import org.coury.jfilehelpers.converters.DoubleConverterProvider;
import org.coury.jfilehelpers.converters.EnumConverterProvider;
import org.coury.jfilehelpers.converters.FloatConverterProvider;
import org.coury.jfilehelpers.converters.IntConverterProvider;
import org.coury.jfilehelpers.converters.LongConverterProvider;
import org.coury.jfilehelpers.converters.StringConverterProvider;
import org.coury.jfilehelpers.core.RecordInfo;
import org.coury.jfilehelpers.enums.ProgressMode;
import org.coury.jfilehelpers.progress.ProgressChangeHandler;


public abstract class EngineBase<T> {
	protected RecordInfo<T> recordInfo;
	protected Encoding encoding;	
	protected String footerText;
	protected String headerText;
	protected int lineNumber;
	protected int totalRecords;
	protected final List<ConverterProvider> converterProviders = new ArrayList<ConverterProvider>(); 
	private final DateConverterProvider dateConverterProvider;
	private final BooleanConverterProvider booleanConverterProvider;
	
	public EngineBase(final Class<T> recordClass) {
		
		//default providers
		converterProviders.add(new StringConverterProvider());
		booleanConverterProvider = new BooleanConverterProvider();
		converterProviders.add(booleanConverterProvider);
		converterProviders.add(new IntConverterProvider());
		converterProviders.add(new LongConverterProvider());
		converterProviders.add(new DoubleConverterProvider());
		converterProviders.add(new FloatConverterProvider());
		converterProviders.add(new BigDecimalConverterProvider());
		converterProviders.add(new EnumConverterProvider());
		dateConverterProvider = new DateConverterProvider();
		converterProviders.add(dateConverterProvider);
		
		this.recordInfo = new RecordInfo<T>(recordClass, converterProviders);
	}
	
	/**
	 * Adds a ConverterProvider to the list that is searched when finding a converter
	 * for a field<br>
	 * ConverterProviders added here take precedence over the built in providers 
	 */
	public void addConverterProvider(final ConverterProvider converterProvider){
		this.converterProviders.add(0, converterProvider);
	}
	
	/**
	 * Adds the supplied ConverterProviders to the list that is searched when finding a converter
	 * for a field<br>
	 * ConverterProviders added here take precedence over the built in providers 
	 */
	public void addConverterProviders(final Collection<ConverterProvider> converterProviders){
		this.converterProviders.addAll(0, converterProviders);
	}
	
	public String getDefaultDateFormat(){
		return dateConverterProvider.getDefaultFormat();
	}
	
	/**
	 * Set the default date format used by the converter that handles the java.util.Date class 
	 * @param format default date format
	 */
	public void setDefaultDateFormat(final String format){
		dateConverterProvider.setDefaultFormat(format);
	}
	
	public String getDefaultTrueString(){
		return booleanConverterProvider.getDefaultTrueString();
	}
	
	/**
	 * Set the default true string used by the converter that handles the java.lang.Boolean class and boolean primitive 
	 * @param format default true string
	 */
	public void setDefaultTrueString(final String trueString){
		booleanConverterProvider.setDefaultTrueString(trueString);
	}
	
	public String getDefaultFalseString(){
		return booleanConverterProvider.getDefaultFalseString();
	}
	
	/**
	 * Set the default false string used by the converter that handles the java.lang.Boolean class and boolean primitive 
	 * @param format default false string
	 */
	public void setDefaultFalseString(final String falseString){
		booleanConverterProvider.setDefaultFalseString(falseString);
	}
	
	
	protected ProgressMode progressMode = ProgressMode.DontNotify;
	// TODO Enable this field
	/*
	private ErrorManager errorManager;
	*/
	protected ProgressChangeHandler notifyHandler = null;
	
	public void setProgressHandler(final ProgressChangeHandler handler) {
		setProgressHandler(handler, ProgressMode.NotifyRecords);
	}
	
	public void setProgressHandler(final ProgressChangeHandler handler, final ProgressMode mode) {
		this.notifyHandler = handler;
		
		if (mode == ProgressMode.NotifyBytes) {
			throw new UnsupportedOperationException("Not implemented yet. Planned for version 1.5.0");
		}
		
		this.progressMode = mode;
	}
	
	protected void resetFields() {
		this.lineNumber = 0;
		//this.errorManager.clearErrors();
		this.totalRecords = 0;
	}
	
	public Encoding getEncoding() {
		return encoding;
	}
	public void setEncoding(final Encoding encoding) {
		this.encoding = encoding;
	}
	public String getFooterText() {
		return footerText;
	}
	public void setFooterText(final String footerText) {
		this.footerText = footerText;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(final String headerText) {
		this.headerText = headerText;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
//	public ErrorManager getErrorManager() {
//		return errorManager;
//	}

	public void setLineNumber(final int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
}
