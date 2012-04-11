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
import org.coury.jfilehelpers.events.AfterReadRecordEventArgs;
import org.coury.jfilehelpers.events.AfterReadRecordHandler;
import org.coury.jfilehelpers.events.AfterWriteRecordEventArgs;
import org.coury.jfilehelpers.events.AfterWriteRecordHandler;
import org.coury.jfilehelpers.events.BeforeReadRecordEventArgs;
import org.coury.jfilehelpers.events.BeforeReadRecordHandler;
import org.coury.jfilehelpers.events.BeforeWriteRecordEventArgs;
import org.coury.jfilehelpers.events.BeforeWriteRecordHandler;
import org.coury.jfilehelpers.helpers.ProgressHelper;
import org.coury.jfilehelpers.helpers.StringHelper;

/**
 * Handles flat files with master-detail information
 * 
 * @author Felipe G. Coury <felipe.coury@gmail.com>
 * 
 * @param <MT>
 *            Master Type
 * @param <DT>
 *            Detail Type
 */
public class MasterDetailEngine<MT, DT> extends EngineBase<DT> {

	@SuppressWarnings("unused")
	private Class<MT> masterRecordClass;
	private final RecordInfo<MT> masterInfo;
	private final MasterDetailSelector recordSelector;
	private BeforeReadRecordHandler beforeReadRecordHandler;
	private AfterReadRecordHandler<MT> afterReadMasterRecordHandler;
	private AfterReadRecordHandler<DT> afterReadDetailRecordHandler;
	private int totalDetailRecords;
	private BeforeWriteRecordHandler<MT> beforeWriteMasterRecordHandler;
	private BeforeWriteRecordHandler<DT> beforeWriteDetailRecordHandler;
	private AfterWriteRecordHandler<MT> afterWriteMasterRecordHandler;
	private AfterWriteRecordHandler<DT> afterWriteDetailRecordHandler;

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

	@Override
	protected void resetFields() {
		super.resetFields();
		this.totalDetailRecords = 0;
	}

	public List<? extends MasterDetails<MT, DT>> readResource(final String fileName) throws IOException {
		List<MasterDetails<MT, DT>> tempRes = null;

		InputStreamReader fr = null;
		try {
			fr = new InputStreamReader(getClass().getResourceAsStream(fileName));
			tempRes = readStream(fr);
		} finally {
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
		} finally {
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
			// fw.write("ABCDEF\n");
			writeStream(fw, records, maxRecords);
		} finally {
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

				ProgressHelper.notify(notifyHandler, progressMode, i + 1, max);
				lineNumber++;

				MasterDetails<MT, DT> masterDetails = records.get(i);
				beforeWriteMaster(masterDetails, writer);

				MT master = masterDetails.getMaster();

				boolean skip = onBeforeWriteMasterRecord(master);
				if (!skip) {
					currentLine = masterInfo.recordToStr(master);
					currentLine = onAfterWriteMasterRecord(master, lineNumber, currentLine);
					writer.write(currentLine + StringHelper.NEW_LINE);
				}

				List<DT> details = masterDetails.getDetails();
				if (details != null) {
					for (int d = 0; d < details.size(); d++) {
						lineNumber++;
						DT detail = details.get(d);
						skip = onBeforeWriteDetailRecord(detail);
						if(!skip){
							currentLine = recordInfo.recordToStr(detail);
							currentLine = onAfterWriteDetailRecord(detail, lineNumber, currentLine);
							writer.write(currentLine + StringHelper.NEW_LINE);
						}
					}
				}

				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
				// TODO error manager
				// switch (mErrorManager.ErrorMode)
				// {
				// case ErrorMode.ThrowException:
				// throw;
				// case ErrorMode.IgnoreAndContinue:
				// break;
				// case ErrorMode.SaveAndContinue:
				// ErrorInfo err = new ErrorInfo();
				// err.mLineNumber = mLineNumber;
				// err.mExceptionInfo = ex;
				// // err.mColumnNumber = mColumnNum;
				// err.mRecordString = currentLine;
				// mErrorManager.AddError(err);
				// break;
				// }
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

	private String onAfterWriteDetailRecord(final DT record, final int lineNumber, final String line){
		if(afterWriteDetailRecordHandler != null){
			AfterWriteRecordEventArgs<DT> e = new AfterWriteRecordEventArgs<DT>(record, lineNumber, line);
			afterWriteDetailRecordHandler.handleAfterWriteRecord(e);
			return e.getRecordLine();
		}
		return line;
	}
	
	private String onAfterWriteMasterRecord(final MT record, final int lineNumber, final String line){
		if(afterWriteMasterRecordHandler != null){
			AfterWriteRecordEventArgs<MT> e = new AfterWriteRecordEventArgs<MT>(record, lineNumber, line);
			afterWriteMasterRecordHandler.handleAfterWriteRecord(e);
			return e.getRecordLine();
		}
		return line;
	}
	
	private boolean onBeforeWriteMasterRecord(final MT record) {
		if (beforeWriteMasterRecordHandler != null) {
			BeforeWriteRecordEventArgs<MT> e = new BeforeWriteRecordEventArgs<MT>(record, lineNumber);
			beforeWriteMasterRecordHandler.handleBeforeWriteRecord(e);
			return e.getSkipThisRecord();
		}
		return false;
	}
	
	private boolean onBeforeWriteDetailRecord(final DT record) {
		if (beforeWriteDetailRecordHandler != null) {
			BeforeWriteRecordEventArgs<DT> e = new BeforeWriteRecordEventArgs<DT>(record, lineNumber);
			beforeWriteDetailRecordHandler.handleBeforeWriteRecord(e);
			return e.getSkipThisRecord();
		}
		return false;
	}

	protected void beforeWriteMaster(final MasterDetails<MT, DT> masterDetails, final BufferedWriter writer) throws IOException {
	}

	public BeforeReadRecordHandler getBeforeReadRecordHandler() {
		return beforeReadRecordHandler;
	}

	public void setBeforeReadRecordHandler(final BeforeReadRecordHandler beforeReadRecordHandler) {
		this.beforeReadRecordHandler = beforeReadRecordHandler;
	}

	private boolean onBeforeReadRecord(final BeforeReadRecordEventArgs e) {

		if (beforeReadRecordHandler != null) {
			beforeReadRecordHandler.handleBeforeReadRecord(e);
			return e.getSkipThisRecord();
		}
		return false;
	}

	public AfterReadRecordHandler<MT> getAfterReadMasterRecordHandler() {
		return afterReadMasterRecordHandler;
	}

	public void setAfterReadMasterRecordHandler(final AfterReadRecordHandler<MT> afterReadMasterRecordHandler) {
		this.afterReadMasterRecordHandler = afterReadMasterRecordHandler;
	}

	public AfterReadRecordHandler<DT> getAfterReadDetailRecordHandler() {
		return afterReadDetailRecordHandler;
	}

	public void setAfterReadDetailRecordHandler(final AfterReadRecordHandler<DT> afterReadDetailRecordHandler) {
		this.afterReadDetailRecordHandler = afterReadDetailRecordHandler;
	}

	private boolean onAfterReadMasterRecord(final String line, final MT record) {

		// PostReadRecordHandler<T> postReadRecordHandler =
		// recordInfo.getPostReadRecordHandler();
		AfterReadRecordEventArgs<MT> e = null;
		// if(postReadRecordHandler != null || afterReadRecordHandler != null){

		// }

		/*
		 * if(postReadRecordHandler != null){
		 * postReadRecordHandler.handleRecord(e); if(e.getSkipThisRecord()){
		 * return true; } }
		 */

		if (afterReadMasterRecordHandler != null) {
			e = new AfterReadRecordEventArgs<MT>(line, record, lineNumber);
			afterReadMasterRecordHandler.handleAfterReadRecord(e);
			return e.getSkipThisRecord();
		}
		return false;
	}

	private boolean onAfterReadDetailRecord(final String line, final DT record) {

		// PostReadRecordHandler<T> postReadRecordHandler =
		// recordInfo.getPostReadRecordHandler();
		AfterReadRecordEventArgs<DT> e = null;
		// if(postReadRecordHandler != null || afterReadRecordHandler != null){

		// }

		/*
		 * if(postReadRecordHandler != null){
		 * postReadRecordHandler.handleRecord(e); if(e.getSkipThisRecord()){
		 * return true; } }
		 */

		if (afterReadDetailRecordHandler != null) {
			e = new AfterReadRecordEventArgs<DT>(line, record, lineNumber);
			afterReadDetailRecordHandler.handleAfterReadRecord(e);
			return e.getSkipThisRecord();
		}
		return false;
	}

	private List<MasterDetails<MT, DT>> readStream(final InputStreamReader fileReader) throws IOException {
		BufferedReader reader = new BufferedReader(fileReader);

		resetFields();
		setHeaderText("");
		setFooterText("");

		List<MasterDetails<MT, DT>> resArray = new ArrayList<MasterDetails<MT, DT>>();

		ForwardReader freader = new ForwardReader(reader, masterInfo.getIgnoreLast());
		freader.setDiscardForward(true);

		lineNumber = 0;
		String currentLine = readLine(freader);

		ProgressHelper.notify(notifyHandler, progressMode, 0, -1);

		int currentRecord = 0;

		if (masterInfo.getIgnoreFirst() > 0) {
			for (int i = 0; i < masterInfo.getIgnoreFirst() && currentLine != null; i++) {
				headerText += currentLine + StringHelper.NEW_LINE;
				currentLine = freader.readNextLine();
				lineNumber++;
			}
		}

		MasterDetails<MT, DT> record = null;

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

					boolean skipMaster = false;
					MT master = masterInfo.strToRecord(line);
					if (master != null) {
						skipMaster = onAfterReadMasterRecord(currentLine, master);
					}

					if (!skipMaster && record != null) {
						// finish previous master
						record.addDetails(tmpDetails);
						resArray.add(record);
					}

					if (master != null && !skipMaster) {
						totalRecords++;
						record = createMasterDetails();
						record.setMaster(master);
						tmpDetails.clear();
					}

					break;

				case Detail:
					DT lastChild = recordInfo.strToRecord(line);
					boolean skipChild = onAfterReadDetailRecord(currentLine, lastChild);
					if (lastChild != null && !skipChild) {
						totalDetailRecords++;
						tmpDetails.add(lastChild);
					}
					break;

				default:
					break;
				}
			} catch (RuntimeException ex) {
				// TODO error handling
				ex.printStackTrace();
				throw ex;
				// switch (mErrorManager.ErrorMode)
				// {
				// case ErrorMode.ThrowException:
				// byPass = true;
				// throw;
				// case ErrorMode.IgnoreAndContinue:
				// break;
				// case ErrorMode.SaveAndContinue:
				// ErrorInfo err = new ErrorInfo();
				// err.mLineNumber = mLineNumber;
				// err.mExceptionInfo = ex;
				// // err.mColumnNumber = mColumnNum;
				// err.mRecordString = completeLine;
				//
				// mErrorManager.AddError(err);
				// break;
				// }
			}

			currentLine = readLine(freader);

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

	private String readLine(final ForwardReader freader) throws IOException {
		String currentLine = null;
		boolean skip = true;
		while (skip) {
			currentLine = freader.readNextLine();
			if (currentLine == null) {
				return null;
			}
			BeforeReadRecordEventArgs e = new BeforeReadRecordEventArgs(currentLine, ++lineNumber);
			skip = onBeforeReadRecord(e);
			if (e.getRecordLineChanged()) {
				currentLine = e.getRecordLine();
			}
		}
		return currentLine;
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

	public int getTotalDetailRecords() {
		return totalDetailRecords;
	}

	public BeforeWriteRecordHandler<MT> getBeforeWriteMasterRecordHandler() {
		return beforeWriteMasterRecordHandler;
	}

	public void setBeforeWriteMasterRecordHandler(final BeforeWriteRecordHandler<MT> beforeWriteMasterRecordHandler) {
		this.beforeWriteMasterRecordHandler = beforeWriteMasterRecordHandler;
	}

	public BeforeWriteRecordHandler<DT> getBeforeWriteDetailRecordHandler() {
		return beforeWriteDetailRecordHandler;
	}

	public void setBeforeWriteDetailRecordHandler(final BeforeWriteRecordHandler<DT> beforeWriteDetailRecordHandler) {
		this.beforeWriteDetailRecordHandler = beforeWriteDetailRecordHandler;
	}

	public AfterWriteRecordHandler<MT> getAfterWriteMasterRecordHandler() {
		return afterWriteMasterRecordHandler;
	}

	public void setAfterWriteMasterRecordHandler(final AfterWriteRecordHandler<MT> afterWriteMasterRecordHandler) {
		this.afterWriteMasterRecordHandler = afterWriteMasterRecordHandler;
	}

	public AfterWriteRecordHandler<DT> getAfterWriteDetailRecordHandler() {
		return afterWriteDetailRecordHandler;
	}

	public void setAfterWriteDetailRecordHandler(final AfterWriteRecordHandler<DT> afterWriteDetailRecordHandler) {
		this.afterWriteDetailRecordHandler = afterWriteDetailRecordHandler;
	}

}
