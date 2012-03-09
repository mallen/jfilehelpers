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

package org.coury.jfilehelpers.tests.callbacks;

import java.io.IOException;

import org.coury.jfilehelpers.engines.EngineBase;
import org.coury.jfilehelpers.engines.FileHelperEngine;
import org.coury.jfilehelpers.events.AfterReadRecordEventArgs;
import org.coury.jfilehelpers.events.AfterReadRecordHandler;
import org.coury.jfilehelpers.events.AfterWriteRecordEventArgs;
import org.coury.jfilehelpers.events.AfterWriteRecordHandler;
import org.coury.jfilehelpers.events.BeforeReadRecordEventArgs;
import org.coury.jfilehelpers.events.BeforeReadRecordHandler;
import org.coury.jfilehelpers.events.BeforeWriteRecordEventArgs;
import org.coury.jfilehelpers.events.BeforeWriteRecordHandler;

/**
 * @author Robert Eccardt
 *
 */
public class EventsTest extends CallbacksBase {

	public void testBeforeRead() {
		FileHelperEngine<Customer> fileEngine = engine;
		fileEngine.setBeforeReadRecordHandler(
				new BeforeReadRecordHandler<Customer>() {
					@Override
					public void handleBeforeReadRecord(final EngineBase<Customer> engine, final BeforeReadRecordEventArgs e) {
						engineTester(engine);
						incrementBeforeReadCount();
						if(e.getRecordLine().equals("2,Jane Rowe,2")) {
							e.setRecordLine("2,Groucho Marx,2");
						}
					}
				}
		);
		fileEngine.setAfterReadRecordHandler(
				new AfterReadRecordHandler<Customer>() {
					@Override
					public void handleAfterReadRecord(final EngineBase<Customer> engine, final AfterReadRecordEventArgs<Customer> e) {
						engineTester(engine);
						incrementAfterReadCount();
						if(e.getLineNumber() == 2) {
							assertEquals(e.getRecord().name,"Groucho Marx");
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, customers);
			customers = engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(beforeReadCount,4);
		assertEquals(afterReadCount,4);
		assertEquals(customers.size(),4);
	}

	public void testBeforeReadSkip() {
		FileHelperEngine<Customer> fileEngine = engine;
		fileEngine.setBeforeReadRecordHandler(
				new BeforeReadRecordHandler<Customer>() {
					@Override
					public void handleBeforeReadRecord(final EngineBase<Customer> engine, final BeforeReadRecordEventArgs e) {
						if(e.getLineNumber() == 1) {
							e.setSkipThisRecord(true);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, customers);
			customers = engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(customers.size(),3);
	}

	public void testAfterRead() {
		FileHelperEngine<Customer> fileEngine = engine;
		fileEngine.setAfterReadRecordHandler(
				new AfterReadRecordHandler<Customer>() {
					@Override
					public void handleAfterReadRecord(final EngineBase<Customer> engine, final AfterReadRecordEventArgs<Customer> e) {
						engineTester(engine);
						incrementAfterReadCount();
						if(e.getLineNumber() == 2) {
							assertTrue(e.getRecord().name.equals("Jane Rowe"));
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, customers);
			customers = engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(afterReadCount,4);
		assertEquals(customers.size(),4);
	}

	public void testAfterReadSkip() {
		FileHelperEngine<Customer> fileEngine = engine;
		fileEngine.setAfterReadRecordHandler(
				new AfterReadRecordHandler<Customer>() {
					@Override
					public void handleAfterReadRecord(final EngineBase<Customer> engine, final AfterReadRecordEventArgs<Customer> e) {
						if(e.getLineNumber() == 1) {
							e.setSkipThisRecord(true);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, customers);
			customers = engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(customers.size(),3);
	}

	public void testWriteRecord() {
		FileHelperEngine<Customer> fileEngine = engine;
		fileEngine.setBeforeWriteRecordHandler(
				new BeforeWriteRecordHandler<Customer>() {
					@Override
					public void handleBeforeWriteRecord(final EngineBase<Customer> engine, final BeforeWriteRecordEventArgs<Customer> e) {
						engineTester(engine);
						incrementBeforeWriteCount();
						if(e.getRecord().name.equals("Jane Rowe")) {
							e.getRecord().name = "Groucho Marx";
						}
					}
				}
		);
		fileEngine.setAfterWriteRecordHandler(
				new AfterWriteRecordHandler<Customer>() {
					@Override
					public void handleAfterWriteRecord(final EngineBase<Customer> engine, final AfterWriteRecordEventArgs<Customer> e) {
						engineTester(engine);
						incrementAfterWriteCount();
						if(e.getLineNumber() == 2) {
							assertEquals(e.getRecordLine(),"2,Groucho Marx,2");
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, customers);
			customers = engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(beforeWriteCount,4);
		assertEquals(afterWriteCount,4);
		assertEquals(customers.size(),4);
	}

	public void testBeforeWriteSkip() {
		FileHelperEngine<Customer> fileEngine = engine;
		fileEngine.setBeforeWriteRecordHandler(
				new BeforeWriteRecordHandler<Customer>() {
					@Override
					public void handleBeforeWriteRecord(final EngineBase<Customer> engine, final BeforeWriteRecordEventArgs<Customer> e) {
						if(e.getLineNumber() == 1) {
							e.setSkipThisRecord(true);
						}
					}
				}
				);
		try {
			engine.writeFile(customerFile, customers);
			customers = engine.readFile(customerFile);
		} catch (IOException e) {
		}
		assertEquals(customers.size(),3);
	}

}