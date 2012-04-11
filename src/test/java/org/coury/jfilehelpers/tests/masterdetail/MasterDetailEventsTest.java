/*
 * EventsTest.java
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

package org.coury.jfilehelpers.tests.masterdetail;

import java.io.IOException;
import java.util.List;

import org.coury.jfilehelpers.events.AfterReadRecordEventArgs;
import org.coury.jfilehelpers.events.AfterReadRecordHandler;
import org.coury.jfilehelpers.events.AfterWriteRecordEventArgs;
import org.coury.jfilehelpers.events.AfterWriteRecordHandler;
import org.coury.jfilehelpers.events.BeforeReadRecordEventArgs;
import org.coury.jfilehelpers.events.BeforeReadRecordHandler;
import org.coury.jfilehelpers.events.BeforeWriteRecordEventArgs;
import org.coury.jfilehelpers.events.BeforeWriteRecordHandler;
import org.coury.jfilehelpers.masterdetail.MasterDetailEngine;
import org.coury.jfilehelpers.masterdetail.MasterDetails;
import org.coury.jfilehelpers.tests.types.customers.CustomersVerticalBar;
import org.coury.jfilehelpers.tests.types.orders.OrdersVerticalBar;

/**
 * @author Robert Eccardt
 *
 */
public class MasterDetailEventsTest extends MasterDetailCallbacksBase {

	public void testBeforeRead() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setBeforeReadRecordHandler(
				new BeforeReadRecordHandler() {
					@Override
					public void handleBeforeReadRecord(final BeforeReadRecordEventArgs e) {
						incrementBeforeReadCount();
						String recordLine = e.getRecordLine();
						if(recordLine.equals("10248|VINET|5|04071996|01081996|16071996|3|32.38")) {
							e.setRecordLine("10248|WIBBLE|5|04071996|01081996|16071996|3|32.38");
						}
					}
				}
		);
		fileEngine.setAfterReadMasterRecordHandler(
				new AfterReadRecordHandler<CustomersVerticalBar>() {
					@Override
					public void handleAfterReadRecord(final AfterReadRecordEventArgs<CustomersVerticalBar> e) {
						incrementAfterReadMasterCount();
						if(e.getLineNumber() == 6) {
							assertEquals(e.getRecord().customerID,"ANATR");
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(13, beforeReadCount); //13 lines
		//assertEquals(afterReadCount,13);
		assertEquals(4, data.size()); //4 master records
	}

	
	public void testBeforeReadSkip() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setBeforeReadRecordHandler(
				new BeforeReadRecordHandler() {
					@Override
					public void handleBeforeReadRecord(final BeforeReadRecordEventArgs e) {
						if(e.getLineNumber() == 1) {
							e.setSkipThisRecord(true);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(3, data.size());
		assertEquals(3, engine.getTotalRecords());
	}

	
	public void testAfterMasterRead() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setAfterReadMasterRecordHandler(
				new AfterReadRecordHandler<CustomersVerticalBar>() {
					@Override
					public void handleAfterReadRecord(final AfterReadRecordEventArgs<CustomersVerticalBar> e) {
						incrementAfterReadMasterCount();
						if(e.getLineNumber() == 6) {
							assertEquals(e.getRecord().customerID,"ANATR");
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(4, afterReadMasterCount);
		assertEquals(4, data.size());
		assertEquals(4, engine.getTotalRecords());
	}
	

	public void testAfterMasterReadSkip() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setAfterReadMasterRecordHandler(
				new AfterReadRecordHandler<CustomersVerticalBar>() {
					@Override
					public void handleAfterReadRecord(final AfterReadRecordEventArgs<CustomersVerticalBar> e) {
						if(e.getLineNumber() == 1) {
							e.setSkipThisRecord(true);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(3, data.size());
		assertEquals(3, engine.getTotalRecords());
	}
	
	public void testAfterDetailRead() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setAfterReadDetailRecordHandler(
				new AfterReadRecordHandler<OrdersVerticalBar>() {
					@Override
					public void handleAfterReadRecord(final AfterReadRecordEventArgs<OrdersVerticalBar> e) {
						incrementAfterReadDetailCount();
						if(e.getLineNumber() == 2) {
							assertEquals("VINET", e.getRecord().customerID);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(9, afterReadDetailCount);
		assertEquals(9, engine.getTotalDetailRecords());
	}
	
	public void testAfterDetailReadSkip() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setAfterReadDetailRecordHandler(
				new AfterReadRecordHandler<OrdersVerticalBar>() {
					@Override
					public void handleAfterReadRecord(final AfterReadRecordEventArgs<OrdersVerticalBar> e) {
						if(e.getLineNumber() == 2) {
							e.setSkipThisRecord(true);
						} else {
							incrementAfterReadDetailCount();
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(8, afterReadDetailCount);
		assertEquals(8, engine.getTotalDetailRecords());
	}

	
	public void testWriteMasterRecord() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setBeforeWriteMasterRecordHandler(
				new BeforeWriteRecordHandler<CustomersVerticalBar>() {
					@Override
					public void handleBeforeWriteRecord(final BeforeWriteRecordEventArgs<CustomersVerticalBar> e) {
						incrementBeforeWriteMasterCount();
						if(e.getRecord().customerID.equals("ALFKI")) {
							e.getRecord().customerID = "WIBBLE";
						}
					}
				}
		);
		fileEngine.setAfterWriteMasterRecordHandler(
				new AfterWriteRecordHandler<CustomersVerticalBar>() {
					@Override
					public void handleAfterWriteRecord(final AfterWriteRecordEventArgs<CustomersVerticalBar> e) {
						incrementAfterWriteMasterCount();
						if(e.getLineNumber() == 1) {
							assertEquals(e.getRecordLine().substring(0, e.getRecordLine().indexOf("|")),"WIBBLE");
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(4, beforeWriteMasterCount);
		assertEquals(4, afterWriteMasterCount);
		assertEquals(4, data.size());
	}

	
	public void testBeforeWriteMasterSkip() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setBeforeWriteMasterRecordHandler(
				new BeforeWriteRecordHandler<CustomersVerticalBar>() {
					@Override
					public void handleBeforeWriteRecord(final BeforeWriteRecordEventArgs<CustomersVerticalBar> e) {
						if(e.getLineNumber() == 1) {
							e.setSkipThisRecord(true);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(3, data.size());
	}
	
	public void testWriteDetailRecord() {
		MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> fileEngine = engine;
		fileEngine.setBeforeWriteDetailRecordHandler(
				new BeforeWriteRecordHandler<OrdersVerticalBar>() {
					@Override
					public void handleBeforeWriteRecord(final BeforeWriteRecordEventArgs<OrdersVerticalBar> e) {
						incrementBeforeWriteDetailCount();
						if(e.getRecord().orderID == 10248) {
							e.getRecord().orderID = 999;
						}
					}
				}
		);
		fileEngine.setAfterWriteDetailRecordHandler(
				new AfterWriteRecordHandler<OrdersVerticalBar>() {
					@Override
					public void handleAfterWriteRecord(final AfterWriteRecordEventArgs<OrdersVerticalBar> e) {
						incrementAfterWriteDetailCount();
						if(e.getLineNumber() == 2) {
							assertEquals("999", e.getRecordLine().substring(0, e.getRecordLine().indexOf("|")).trim());
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, data);
			data = (List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>) engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(9, beforeWriteDetailCount);
		assertEquals(9, afterWriteDetailCount);
	}
	
}
