/*
 * ConstructorHelper.java
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
package org.coury.jfilehelpers.helpers;

import java.lang.reflect.Constructor;

public class ConstructorHelper {

	public static <T> Constructor<T> getAccessibleEmptyConstructor(final Class<T> clazz) {
		Constructor<T> constructor = null;
		try {
			constructor = clazz.getConstructor();
		} catch (SecurityException e) {
			throw new RuntimeException(
					"The class " + clazz.getName() + 
					" needs to be accessible to be used");
		} catch (NoSuchMethodException e) {
			boolean throwIt = true;

			try {
				if (clazz.getEnclosingClass() != null) {
					constructor = clazz.getConstructor(clazz.getEnclosingClass());
				}
				throwIt = false;
			}
			catch (NoSuchMethodException e1) {
			}

			if (throwIt) {
				throw new RuntimeException(
						"The class " + clazz.getName() + 
						" needs to have an empty constructor to be used");
			}
		}
		return constructor;
	}
	
}
