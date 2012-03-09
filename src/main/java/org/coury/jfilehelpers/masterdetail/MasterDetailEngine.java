/*
 * MasterDetailEngine.java
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
package org.coury.jfilehelpers.masterdetail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.coury.jfilehelpers.core.ForwardReader;
import org.coury.jfilehelpers.core.RecordInfo;
import org.coury.jfilehelpers.engines.EngineBase;
import org.coury.jfilehelpers.engines.LineInfo;
import org.coury.jfilehelpers.helpers.ProgressHelper;
import org.coury.jfilehelpers.helpers.StringHelper;

/**
 * Handles flat files with master-detail information
 * 
 * @author Felipe G. Coury <felipe.coury@gmail.com>
 *
 * @param <MT> Master Type
 * @param <DT> Detail Type
 */
public class MasterDetailEngine<MT, DT> extends EngineBase<DT> {

	@SuppressWarnings("unused")
	private Class<MT> masterRecordClass;
	private final RecordInfo<MT> masterInfo;
	private final MasterDetailSelector recordSelector;

	public MasterDetailEngine(final Class<MT> masterRecordClass, final Class<DT> detailRecordClass, final MasterDetailSelector recordSelector) {
		super(detailRecordClass);
		this.masterRecordClass = masterRecordClass;
		this.masterInfo = new RecordInfo<MT>(masterRecordClass, converterProviders);
		this.recordSelector = recordSelector;
	}
	
	public MasterDetailEngine(final Class<MT> masterRecordClass, final Class<DT> detailRecordClass, final CommonSelector action, final String selector) {
		super(detailRecordClass);
		this.masterInfo = new RecordInfo<MT>(masterRecordClass, converterProviders);
		final CommonInternalSelector sel = new CommonInternalSelector(action, selector, masterInfo.isIgnoreEmptyLines() || recordInfo.isIgnoreEmptyLines());
		
		this.recordSelector = new MasterDetailSelector() {

			@Override
			public RecordAction getRecordAction(final String recordString) {
				return sel.getCommonSelectorMethod(recordString);			
			}
			
		};
	}
	
	public List<? extends MasterDetails<MT, DT>> readResource(final String fileName) throws IOException {
		List<MasterDetails<MT, DT>> tempRes = null;		
		
		InputStreamReader fr = null;
		try {
			fr = new InputStreamReader(getClass().getResourceAsStream(fileName));
			tempRes = readStream(fr);
		}
		finally {
			if (fr != null) {
				fr.close();
			}
		}
		
		return tempRes;
	}
	
	public List<? extends MasterDetails<MT, DT>> fromString(final String s) throws IOException {
		return readStream(new InputStreamReader(new ByteArrayInputStream(s.getBytes())));
	}
	
	public List<? extends MasterDetails<MT, DT>> readFile(final String fileName) throws IOException {
		List<MasterDetails<MT, DT>> tempRes = null;
		
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
			tempRes = readStream(fr);
		}
		finally {
			if (fr != null) {
				fr.close();
			}
		}
		
		return tempRes;
	}

	public void writeFile(final String fileName, final MasterDetails<MT, DT> record) throws IOException {
		List<MasterDetails<MT, DT>> list = new ArrayList<MasterDetails<MT, DT>>();
		list.add(record);
		
		writeFile(fileName, list);
	}

	public void writeFile(final String fileName, final List<? extends MasterDetails<MT, DT>> records) throws IOException {
		writeFile(fileName, records, -1);
	}
	
	public void writeFile(final String fileName, final List<? extends MasterDetails<MT, DT>> records, final int maxRecords) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(fileName));
			//fw.write("ABCDEF\n");
			writeStream(fw, records, maxRecords);
		}
		finally {
			if (fw != null) {
				fw.flush();
				fw.close();
			}
		}
	}
	
	private void writeStream(final OutputStreamWriter osr, final List<? extends MasterDetails<MT, DT>> records, final int maxRecords) throws IOException {
		BufferedWriter writer = new BufferedWriter(osr);
		
		resetFields();
		if (getHeaderText() != null && getHeaderText().length() != 0) {
			writer.write(getHeaderText());
			if (!getHeaderText().endsWith(StringHelper.NEW_LINE)) {
				writer.write(StringHelper.NEW_LINE);
			}
		}

		String currentLine = null;

		int max = records.size();

		if (maxRecords >= 0) {
			max = Math.min(max, maxRecords);
		}

		ProgressHelper.notify(notifyHandler, progressMode, 0, max);

		for (int i = 0; i < max; i++) {
			try {
				if (records.get(i) == null) {
					throw new IllegalArgumentException("The record at index " + i + " is null.");
				}
				
				ProgressHelper.notify(notifyHandler, progressMode, i+1, max);

				MasterDetails<MT, DT> masterDetails = records.get(i);
				beforeWriteMaster(masterDetails, writer);
				currentLine = masterInfo.recordToStr(masterDetails.getMaster());
				writer.write(currentLine + StringHelper.NEW_LINE);

				if (masterDetails.getDetails() != null) { 
					for (int d = 0; d < masterDetails.getDetails().size(); d++) {
						currentLine = recordInfo.recordToStr(records.get(i).getDetails().get(d));
						writer.write(currentLine + StringHelper.NEW_LINE);
					}
				}

				writer.flush();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				// TODO error manager
//				switch (mErrorManager.ErrorMode)
//				{
//					case ErrorMode.ThrowException:
//						throw;
//					case ErrorMode.IgnoreAndContinue:
//						break;
//					case ErrorMode.SaveAndContinue:
//						ErrorInfo err = new ErrorInfo();
//						err.mLineNumber = mLineNumber;
//						err.mExceptionInfo = ex;
////						err.mColumnNumber = mColumnNum;
//						err.mRecordString = currentLine;
//						mErrorManager.AddError(err);
//						break;
//				}
			}
		}

		totalRecords = records.size();

		if (getFooterText() != null && getFooterText() != "") {
			writer.write(getFooterText());
			if (!getFooterText().endsWith(StringHelper.NEW_LINE)) {
				writer.write(StringHelper.NEW_LINE);
			}
		}		
	}
		
	protected void beforeWriteMaster(final MasterDetails<MT, DT> masterDetails, final BufferedWriter writer) throws IOException {
	}

	private List<MasterDetails<MT, DT>> readStream(final InputStreamReader fileReader) throws IOException {
		BufferedReader reader = new BufferedReader(fileReader);

		resetFields();
		setHeaderText("");
		setFooterText("");
		
		List<MasterDetails<MT,DT>> resArray = new ArrayList<MasterDetails<MT,DT>>();
		
		ForwardReader freader = new ForwardReader(reader, masterInfo.getIgnoreLast());
		freader.setDiscardForward(true);

		String currentLine, completeLine;

		lineNumber = 1;

		completeLine = freader.readNextLine();
		currentLine = completeLine;

		ProgressHelper.notify(notifyHandler, progressMode, 0, -1);
		
		int currentRecord = 0;

		if (masterInfo.getIgnoreFirst() > 0) {
			for (int i = 0; i < masterInfo.getIgnoreFirst() && currentLine != null; i++) {
				headerText += currentLine + StringHelper.NEW_LINE;
				currentLine = freader.readNextLine();
				lineNumber++;
			}
		}

		boolean byPass = false;
		MasterDetails<MT,DT> record = null;
		
		List<DT> tmpDetails = new ArrayList<DT>();

		LineInfo line = new LineInfo(currentLine);
		line.setReader(freader);
		
		while (currentLine != null) {
			try
			{
				currentRecord++; 

				line.reload(currentLine);
				
				ProgressHelper.notify(notifyHandler, progressMode, currentRecord, -1);

				RecordAction action = getRecordAction(currentLine);
				switch (action) {
					case Master:
						if (record != null) {							
							record.addDetails(tmpDetails);
							resArray.add(record);
						}

						totalRecords++;
						record = createMasterDetails();
						tmpDetails.clear();
						
						MT lastMaster = masterInfo.strToRecord(line);

						if (lastMaster != null) {
							record.setMaster(lastMaster);
						}

						break;

					case Detail:
						DT lastChild = recordInfo.strToRecord(line);

						if (lastChild != null) {
							tmpDetails.add(lastChild);
						}
						break;

					default:
						break;
				}
			}
			catch (RuntimeException ex) {
				// TODO error handling
				ex.printStackTrace();
				throw ex;
//				switch (mErrorManager.ErrorMode)
//				{
//					case ErrorMode.ThrowException:
//						byPass = true;
//						throw;
//					case ErrorMode.IgnoreAndContinue:
//						break;
//					case ErrorMode.SaveAndContinue:
//						ErrorInfo err = new ErrorInfo();
//						err.mLineNumber = mLineNumber;
//						err.mExceptionInfo = ex;
////						err.mColumnNumber = mColumnNum;
//						err.mRecordString = completeLine;
//
//						mErrorManager.AddError(err);
//						break;
//				}
			}
			finally
			{
				if (byPass == false) {
					currentLine = freader.readNextLine();
					completeLine = currentLine;
					lineNumber = freader.getLineNumber();
				}
			}

		}

		if (record != null) {
			record.addDetails(tmpDetails);
			resArray.add(record);
		}

		if (masterInfo.getIgnoreLast() > 0) {
			footerText = freader.getRemainingText();
		}

		return resArray;		
	}

	protected MasterDetails<MT, DT> createMasterDetails() {
		return new MasterDetails<MT, DT>();
	}

	protected RecordAction getRecordAction(final String currentLine) {
		return recordSelector.getRecordAction(currentLine);
	}

	class CommonInternalSelector {
		
		private final String selector;
		private final boolean ignoreEmpty;
		private final CommonSelector action;
		
		public CommonInternalSelector(final CommonSelector action, final String selector, final boolean ignoreEmpty) {
			this.action = action;
			this.selector = selector;
			this.ignoreEmpty = ignoreEmpty;
		}
		
		protected RecordAction getCommonSelectorMethod(final String recordString) {
			if (ignoreEmpty && recordString.length() < 1) {
				return RecordAction.Skip;
			}

			switch (action) {
			case DetailIfContains:
				if (recordString.indexOf(selector) >= 0) {
					return RecordAction.Detail;
				} else {
					return RecordAction.Master;
				}

			case MasterIfContains:
				if (recordString.indexOf(selector) >= 0) {
					return RecordAction.Master;
				} else {
					return RecordAction.Detail;
				}

			case DetailIfBegins:
				if (recordString.startsWith(selector)) {
					return RecordAction.Detail;
				} else {
					return RecordAction.Master;
				}
			
			case MasterIfBegins:
				if (recordString.startsWith(selector)) {
					return RecordAction.Master;
				} else {
					return RecordAction.Detail;
				}

			case DetailIfEnds:
				if (recordString.endsWith(selector)) {
					return RecordAction.Detail;
				} else {
					return RecordAction.Master;
				}

			case MasterIfEnds:
				if (recordString.endsWith(selector)) {
					return RecordAction.Master;
				} else {
					return RecordAction.Detail;
				}

			case DetailIfEnclosed:
				if (recordString.startsWith(selector) && recordString.endsWith(selector)) {
					return RecordAction.Detail;
				} else {
					return RecordAction.Master;
				}

			case MasterIfEnclosed:
				if (recordString.startsWith(selector) && recordString.endsWith(selector)) {
					return RecordAction.Master;
				} else {
					return RecordAction.Detail;
				}
			}

			return RecordAction.Skip;
		}
		
	}
}