/*
 * ConverterBase.java
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

package org.coury.jfilehelpers.converters;

import java.lang.annotation.Annotation;

/**
 * This is the base class for all converters.
 * 
 * @author Felipe Gonçalves Coury <felipe.coury@gmail.com>
 */
public abstract class ConverterBase {

	public abstract Object stringToField(String from);
	
	public String fieldToString(final Object from) {
		if (from == null) {
			return "";
		}
		else {
			return from.toString();
		}
	}
	
	public boolean isCustomNullHandling() {
		return false;
	}
	
	public Class<? extends Annotation> getOptionsAnnotationType(){
		return null;
	}
	
	public void setOptionsFromAnnotation(final Annotation annotation){
		
	}
	
}
