/*
 * CallbacksBase.java
 *
 * Copyright (C) 2007 Felipe GonÃ§alves Coury <felipe.coury@gmail.com>
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.coury.jfilehelpers.masterdetail.MasterDetailEngine;
import org.coury.jfilehelpers.masterdetail.MasterDetailSelector;
import org.coury.jfilehelpers.masterdetail.MasterDetails;
import org.coury.jfilehelpers.masterdetail.RecordAction;
import org.coury.jfilehelpers.tests.types.customers.CustomersVerticalBar;
import org.coury.jfilehelpers.tests.types.orders.OrdersVerticalBar;


/**
 * @author Robert Eccardt
 *
 */
public abstract class MasterDetailCallbacksBase extends TestCase {
	static MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar> engine;
	static final String customerFile = "master_details_customer_test.txt";
	static List<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>> data;
	static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	static int readCount;
	static int beforeReadCount;
	static int afterReadMasterCount;
	static int afterReadDetailCount;
	static int writeCount;
	static int beforeWriteMasterCount;
	static int beforeWriteDetailCount;
	static int afterWriteMasterCount;
	static int afterWriteDetailCount;
	static String eventLine;
	static String notifyLine;
	@Override
	protected void setUp() throws ParseException {
		engine = new MasterDetailEngine<CustomersVerticalBar, OrdersVerticalBar>(CustomersVerticalBar.class, OrdersVerticalBar.class, new MasterDetailSelector() {

			@Override
			public RecordAction getRecordAction(final String recordString) {
	            if (Character.isLetter(recordString.charAt(0))) {
					return RecordAction.Master;
				} else {
					return RecordAction.Detail;
				}
			}
	
		});		
		data = new ArrayList<MasterDetails<CustomersVerticalBar, OrdersVerticalBar>>();
		readCount = writeCount = 0;
		beforeReadCount = beforeWriteMasterCount = beforeWriteDetailCount = 0;
		afterReadMasterCount = afterWriteMasterCount = 0;
		afterReadDetailCount = afterWriteDetailCount = 0;
		eventLine = "";
		notifyLine = "";
		
		//ALFKI|Alfreds Futterkiste|Maria Anders|Sales Representative|Obere Str. 57|Berlin|Germany
		CustomersVerticalBar c = new CustomersVerticalBar();
		c.customerID = "ALFKI";
		c.companyName = "Alfreds Futterkiste";
		c.contactName = "Maria Anders";
		c.contactTitle="Sales Representative";
		c.address="Obere Str. 57";
		c.city="Berlin";
		c.country="Germany";
		//10248|VINET|5|04071996|01081996|16071996|3|32.38
		OrdersVerticalBar o1 = new OrdersVerticalBar();
		o1.orderID=10248;
		o1.customerID="VINET";
		o1.employeeID=5;
		o1.orderDate= sdf.parse("04071996");
		o1.requiredDate=sdf.parse("01081996");
		o1.shippedDate=sdf.parse("16071996");
		o1.shipVia=3;
		o1.freight=32.38f;
		//10249|TOMSP|6|05071996|16081996|10071996|1|11.61
		OrdersVerticalBar o2 = new OrdersVerticalBar();
		o2.orderID=10249;
		o2.customerID="TOMSP";
		o2.employeeID=6;
		o2.orderDate= sdf.parse("05071996");
		o2.requiredDate=sdf.parse("16081996");
		o2.shippedDate=sdf.parse("10071996");
		o2.shipVia=1;
		o2.freight=11.61f;
		//10250|HANAR|4|08071996|05081996|12071996|2|65.83
		OrdersVerticalBar o3 = new OrdersVerticalBar();
		o3.orderID=10250;
		o3.customerID="HANAR";
		o3.employeeID=4;
		o3.orderDate= sdf.parse("08071996");
		o3.requiredDate=sdf.parse("05081996");
		o3.shippedDate=sdf.parse("12071996");
		o3.shipVia=2;
		o3.freight=65.83f;
		//10251|VICTE|3|08071996|05081996|15071996|1|41.34
		OrdersVerticalBar o4 = new OrdersVerticalBar();
		o4.orderID=10251;
		o4.customerID="VICTE";
		o4.employeeID=3;
		o4.orderDate= sdf.parse("08071996");
		o4.requiredDate=sdf.parse("05081996");
		o4.shippedDate=sdf.parse("15071996");
		o4.shipVia=1;
		o4.freight=41.34f;
		List<OrdersVerticalBar> orders = new ArrayList<OrdersVerticalBar>();
		orders.add(o1);
		orders.add(o2);
		orders.add(o3);
		orders.add(o4);
		MasterDetails<CustomersVerticalBar, OrdersVerticalBar> masterDetails = new MasterDetails<CustomersVerticalBar, OrdersVerticalBar>(c, orders);
		data.add(masterDetails);

		//ANATR|Ana Trujillo Emparedados y helados|Ana Trujillo|Owner|Avda. de la Constitución 2222|México D.F.|Mexico
		c = new CustomersVerticalBar();
		c.customerID = "ANATR";
		c.companyName = "Ana Trujillo Emparedados y helados";
		c.contactName = "Ana Trujillo";
		c.contactTitle="Owner";
		c.address="Avda. de la Constitución 2222";
		c.city="México D.F.";
		c.country="Mexico";
		//10252|SUPRD|4|09071996|06081996|11071996|2|51.3
		o1 = new OrdersVerticalBar();
		o1.orderID=10252;
		o1.customerID="SUPRD";
		o1.employeeID=4;
		o1.orderDate= sdf.parse("09071996");
		o1.requiredDate=sdf.parse("06081996");
		o1.shippedDate=sdf.parse("11071996");
		o1.shipVia=2;
		o1.freight=51.3f;
		//10253|HANAR|3|10071996|24071996|16071996|2|58.17
		o2 = new OrdersVerticalBar();
		o2.orderID=10253;
		o2.customerID="HANAR";
		o2.employeeID=3;
		o2.orderDate= sdf.parse("10071996");
		o2.requiredDate=sdf.parse("24071996");
		o2.shippedDate=sdf.parse("16071996");
		o2.shipVia=2;
		o2.freight=58.17f;
		//10254|CHOPS|5|11071996|08081996|23071996|2|22.98
		o3 = new OrdersVerticalBar();
		o3.orderID=10254;
		o3.customerID="CHOPS";
		o3.employeeID=5;
		o3.orderDate= sdf.parse("11071996");
		o3.requiredDate=sdf.parse("08081996");
		o3.shippedDate=sdf.parse("23071996");
		o3.shipVia=2;
		o3.freight=22.98f;
		orders = new ArrayList<OrdersVerticalBar>();
		orders.add(o1);
		orders.add(o2);
		orders.add(o3);
		masterDetails = new MasterDetails<CustomersVerticalBar, OrdersVerticalBar>(c, orders);
		data.add(masterDetails);

		//ANTON|Antonio Moreno Taquería|Antonio Moreno|Owner|Mataderos  2312|México D.F.|Mexico
		c = new CustomersVerticalBar();
		c.customerID = "ANTON";
		c.companyName = "Antonio Moreno Taquería";
		c.contactName = "Antonio Moreno";
		c.contactTitle="Owner";
		c.address="Mataderos  2312";
		c.city="México D.F.";
		c.country="Mexico";
		//10257|HILAA|4|16071996|13081996|22071996|3|81.91
		o1 = new OrdersVerticalBar();
		o1.orderID=10257;
		o1.customerID="HILAA";
		o1.employeeID=4;
		o1.orderDate= sdf.parse("16071996");
		o1.requiredDate=sdf.parse("13081996");
		o1.shippedDate=sdf.parse("22071996");
		o1.shipVia=3;
		o1.freight=81.91f;
		//10258|ERNSH|1|17071996|14081996|23071996|1|140.51
		o2 = new OrdersVerticalBar();
		o2.orderID=10258;
		o2.customerID="ERNSH";
		o2.employeeID=1;
		o2.orderDate= sdf.parse("17071996");
		o2.requiredDate=sdf.parse("14081996");
		o2.shippedDate=sdf.parse("23071996");
		o2.shipVia=1;
		o2.freight=140.51f;
		orders = new ArrayList<OrdersVerticalBar>();
		orders.add(o1);
		orders.add(o2);
		masterDetails = new MasterDetails<CustomersVerticalBar, OrdersVerticalBar>(c, orders);
		data.add(masterDetails);

		//DUMON|Du monde entier|Janine Labrune|Owner|67, rue des Cinquante Otages|Nantes|France
		c = new CustomersVerticalBar();
		c.customerID = "DUMON";
		c.companyName = "Du monde entier";
		c.contactName = "Janine Labrune";
		c.contactTitle="Owner";
		c.address="67, rue des Cinquante Otages";
		c.city="Nantes";
		c.country="France";
		orders = new ArrayList<OrdersVerticalBar>();
		masterDetails = new MasterDetails<CustomersVerticalBar, OrdersVerticalBar>(c, orders);
		data.add(masterDetails);

	}
	@Override
	protected void tearDown() {
		new File(customerFile).delete();
	}
	public static void incrementReadCount() {
		++readCount;
	}
	public static void incrementBeforeReadCount() {
		++beforeReadCount;
	}
	public static void incrementAfterReadMasterCount() {
		++afterReadMasterCount;
	}
	public static void incrementAfterReadDetailCount() {
		++afterReadDetailCount;
	}
	public static void incrementWriteCount() {
		++writeCount;
	}
	public static void incrementBeforeWriteMasterCount() {
		++beforeWriteMasterCount;
	}
	public static void incrementBeforeWriteDetailCount() {
		++beforeWriteDetailCount;
	}
	public static void incrementAfterWriteMasterCount() {
		++afterWriteMasterCount;
	}
	public static void incrementAfterWriteDetailCount() {
		++afterWriteDetailCount;
	}
	public static void setEventLine(final String s) {
		eventLine = s;
	}
	public static void setNotifyLine(final String s) {
		notifyLine = s;
	}
}
