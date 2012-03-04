/*
 * HeaderMasterDetailEngine.java
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.coury.jfilehelpers.core.RecordInfo;
import org.coury.jfilehelpers.engines.LineInfo;
import org.coury.jfilehelpers.helpers.StringHelper;

public class HeaderMasterDetailEngine<HT, MT, DT> extends MasterDetailEngine<MT, DT> {

	private final RecordInfo<HT> headerInfo;
	private final HeaderMasterDetailSelector headerSelector;
	private HT currentHeader;

	public HeaderMasterDetailEngine(final Class<HT> headerRecordClass, final Class<MT> masterRecordClass, final Class<DT> detailRecordClass, 
			final HeaderMasterDetailSelector headerSelector) {
		super(masterRecordClass, detailRecordClass, headerSelector);
		this.headerSelector = headerSelector;
		this.headerInfo = new RecordInfo<HT>(headerRecordClass, super.converterProviders);	
	}
	
	@Override
	public List<HeaderMasterDetails<HT, MT, DT>> readFile(final String fileName) throws IOException {
		return (List<HeaderMasterDetails<HT, MT, DT>>) super.readFile(fileName);
	}
	
	@Override
	public List<HeaderMasterDetails<HT, MT, DT>> readResource(final String fileName) throws IOException {
		return (List<HeaderMasterDetails<HT, MT, DT>>) super.readResource(fileName);
	}
	
	@Override
	public List<HeaderMasterDetails<HT, MT, DT>> fromString(final String s) throws IOException {
		
		return (List<HeaderMasterDetails<HT, MT, DT>>) super.fromString(s);
	}
	
	@Override
	protected void beforeWriteMaster(MasterDetails<MT, DT> masterDetails, BufferedWriter writer) throws IOException {
		HeaderMasterDetails<HT, MT, DT> hmd = (HeaderMasterDetails<HT, MT, DT>) masterDetails;
		HT header = hmd.getHeader();
		if (!header.equals(currentHeader)) {
			currentHeader = header;
			String headerLine = null;
			try {
				headerLine = headerInfo.recordToStr(header);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error calling recordtoStr", e);
			}
			writer.write(headerLine + StringHelper.NEW_LINE);
		}
	}
	
	/*@Override
	public void writeFile(final String fileName, final List<? extends MasterDetails<MT, DT>> records) throws IOException {
		throw new NotImplementedException();
	}
	
	@Override
	public void writeFile(final String fileName, final List<? extends MasterDetails<MT, DT>> records, final int maxRecords) throws IOException {
		throw new NotImplementedException();
	}
	
	@Override
	public void writeFile(final String fileName, final MasterDetails<MT, DT> record) throws IOException {
		throw new NotImplementedException();
	}*/
	
	@Override
	protected MasterDetails<MT, DT> createMasterDetails() {
		return new HeaderMasterDetails<HT, MT, DT>(currentHeader);
	}
	
	@Override
	protected RecordAction getRecordAction(final String currentLine) {
		if(headerSelector.isHeader(currentLine)){
			//TODO: change signature of getRecordAction to take a LineInfo rather than a String
			//would save creating 2 line infos for every processed line
			LineInfo lineInfo = new LineInfo(currentLine);
			currentHeader = headerInfo.strToRecord(lineInfo);
			return RecordAction.Skip;
		}
		return super.getRecordAction(currentLine);
	}

}
