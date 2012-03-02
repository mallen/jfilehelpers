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

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.coury.jfilehelpers.masterdetail.HeaderMasterDetailEngine;
import org.coury.jfilehelpers.masterdetail.HeaderMasterDetailSelector;
import org.coury.jfilehelpers.masterdetail.HeaderMasterDetails;
import org.coury.jfilehelpers.masterdetail.RecordAction;
import org.coury.jfilehelpers.tests.types.customers.CustomersVerticalBar;
import org.coury.jfilehelpers.tests.types.orders.OrdersVerticalBar;

public class HeaderMasterDetailTest extends TestCase {
	HeaderMasterDetailEngine<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar> engine;
	
	@SuppressWarnings("unchecked")
	public void testCustomersOrderRead() throws IOException {
		engine = new HeaderMasterDetailEngine<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>(HeaderVerticalBar.class, CustomersVerticalBar.class, OrdersVerticalBar.class,
				new HeaderMasterDetailSelector() {
					
					@Override
					public boolean isHeader(final String recordString) {
						return recordString.startsWith("HEADER");
					}
					
					@Override
					public RecordAction getRecordAction(final String recordString) {
			            if (Character.isLetter(recordString.charAt(0))) {
							return RecordAction.Master;
						} else {
							return RecordAction.Detail;
						}
					}
				});		
		
		List<HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>> res = 
				(List<HeaderMasterDetails<HeaderVerticalBar, CustomersVerticalBar, OrdersVerticalBar>>) engine.readResource("/test/Good/HeaderMasterDetail1.txt");
		
		HeaderVerticalBar header1 = new HeaderVerticalBar();
		header1.headerText="HEADER1";
		header1.headerNumber=999;
		
		HeaderVerticalBar header2 = new HeaderVerticalBar();
		header2.headerText="HEADER2";
		header2.headerNumber=123;
		
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
}
