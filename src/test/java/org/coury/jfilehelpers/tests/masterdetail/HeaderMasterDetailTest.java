/*
 * MasterDetailTest.java
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.coury.jfilehelpers.masterdetail.HeaderMasterDetailEngine;
import org.coury.jfilehelpers.masterdetail.HeaderMasterDetailSelector;
import org.coury.jfilehelpers.masterdetail.HeaderMasterDetails;
import org.coury.jfilehelpers.masterdetail.RecordAction;
import org.coury.jfilehelpers.tests.types.customers.CustomersVerticalBar;
import org.coury.jfilehelpers.tests.types.orders.OrdersVerticalBar;
import org.junit.Test;

public class HeaderMasterDetailTest extends TestCase {
	HeaderMasterDetailEngine<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar> engine;

	@SuppressWarnings("unchecked")
	public void testCustomersOrderRead() throws IOException {
		engine = new HeaderMasterDetailEngine<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>(
				HeaderVerticalBar.class, CustomersVerticalBar.class,
				OrdersVerticalBar.class, new HeaderMasterDetailSelector() {

					@Override
					public boolean isHeader(final String recordString) {
						return recordString.startsWith("HEADER");
					}

					@Override
					public RecordAction getRecordAction(
							final String recordString) {
						if (Character.isLetter(recordString.charAt(0))) {
							return RecordAction.Master;
						} else {
							return RecordAction.Detail;
						}
					}
				});

		List<HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>> res = engine
				.readResource("/Good/HeaderMasterDetail1.txt");

		HeaderVerticalBar header1 = new HeaderVerticalBar();
		header1.setHeaderText("HEADER1");
		header1.setHeaderNumber(999);

		HeaderVerticalBar header2 = new HeaderVerticalBar();
		header2.setHeaderText("HEADER2");
		header2.setHeaderNumber(123);

		assertEquals(4, res.size());
		assertEquals(4, engine.getTotalRecords());

		assertEquals(header1, res.get(0).getHeader());
		assertEquals(header1, res.get(1).getHeader());
		assertEquals(header2, res.get(2).getHeader());
		assertEquals(header2, res.get(3).getHeader());

		assertEquals(4, res.get(0).getDetails().size());
		assertEquals(3, res.get(1).getDetails().size());
		assertEquals(2, res.get(2).getDetails().size());
		assertEquals(0, res.get(3).getDetails().size());

		assertEquals("ALFKI", res.get(0).getMaster().customerID);
		assertEquals(10248, res.get(0).getDetails().get(0).orderID);
		assertEquals(10249, res.get(0).getDetails().get(1).orderID);
		assertEquals(10250, res.get(0).getDetails().get(2).orderID);
		assertEquals(10251, res.get(0).getDetails().get(3).orderID);

		assertEquals("ANATR", res.get(1).getMaster().customerID);
		assertEquals(10252, res.get(1).getDetails().get(0).orderID);
		assertEquals(10253, res.get(1).getDetails().get(1).orderID);
		assertEquals(10254, res.get(1).getDetails().get(2).orderID);

		assertEquals("ANTON", res.get(2).getMaster().customerID);
		assertEquals(10257, res.get(2).getDetails().get(0).orderID);
		assertEquals(10258, res.get(2).getDetails().get(1).orderID);

		assertEquals("DUMON", res.get(3).getMaster().customerID);
	}

	@Test
	public void testWrite() throws IOException {

		List<HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>> toWrite = new ArrayList<HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>>();

		HeaderVerticalBar header1 = new HeaderVerticalBar();
		header1.setHeaderText("HEADER1");
		header1.setHeaderNumber(999);
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2012, 02, 15);
		Date d = calendar.getTime();

		CustomersVerticalBar cust1 = new CustomersVerticalBar();
		cust1.customerID = "C1";
		cust1.contactName = "Contact1";
		cust1.address="wibble";
		cust1.companyName="big co";
		cust1.contactTitle="mr";
		cust1.city = "City1";
		cust1.country="";

		OrdersVerticalBar order1 = new OrdersVerticalBar();
		order1.customerID = "C1";
		order1.orderID = 1;
		order1.employeeID = 123;
		order1.shippedDate = d;

		OrdersVerticalBar order2 = new OrdersVerticalBar();
		order2.customerID = "C1";
		order2.orderID = 2;
		order2.employeeID = 234;
		order2.shippedDate = d;

		HeaderVerticalBar header2 = new HeaderVerticalBar();
		header2.setHeaderText("HEADER2");
		header2.setHeaderNumber(123);

		CustomersVerticalBar cust2 = new CustomersVerticalBar();
		cust2.customerID = "C2";
		cust2.contactName = "Contact2";
		cust2.city = "City2";
		cust2.address="";
		cust2.companyName="";
		cust2.contactTitle="";
		cust2.country="";
		

		OrdersVerticalBar order3 = new OrdersVerticalBar();
		order3.customerID = "C2";
		order3.orderID = 3;
		order3.employeeID = 345;
		order3.shippedDate = d;

		HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar> hmd1 = new HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>(
				header1);
		hmd1.setMaster(cust1);
		List<OrdersVerticalBar> orders1 = new ArrayList<OrdersVerticalBar>();
		orders1.add(order1);
		orders1.add(order2);
		hmd1.setDetails(orders1);

		HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar> hmd2 = new HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>(
				header2);
		hmd2.setMaster(cust2);
		List<OrdersVerticalBar> orders2 = new ArrayList<OrdersVerticalBar>();
		orders2.add(order3);
		hmd2.setDetails(orders2);
		
		toWrite.add(hmd1);
		toWrite.add(hmd2);
		
		HeaderMasterDetailEngine<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar> engine
			= new HeaderMasterDetailEngine<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>(HeaderVerticalBar.class, CustomersVerticalBar.class, OrdersVerticalBar.class, 
					new HeaderMasterDetailSelector() {

				@Override
				public boolean isHeader(final String recordString) {
					return recordString.startsWith("HEADER");
				}

				@Override
				public RecordAction getRecordAction(
						final String recordString) {
					if (Character.isLetter(recordString.charAt(0))) {
						return RecordAction.Master;
					} else {
						return RecordAction.Detail;
					}
				}
			});
		
		List<HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>> read;
		try{
			engine.writeFile("write_header_test.txt", toWrite);
			read = engine.readFile("write_header_test.txt");
		}
		finally{
			new File("write_header_test.txt").delete();
		}
		
		assertEquals(2, read.size());
		assertEquals(header1, read.get(0).getHeader());
		assertEquals(header2, read.get(1).getHeader());
		assertEquals(cust1, read.get(0).getMaster());
		assertEquals(cust2, read.get(1).getMaster());
		OrdersVerticalBar ordersVerticalBar = read.get(0).getDetails().get(0);
		assertEquals(order1, ordersVerticalBar);
		assertEquals(order2, read.get(0).getDetails().get(1));
		assertEquals(order3, read.get(1).getDetails().get(0));
		
	}

}
