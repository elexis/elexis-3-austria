/*******************************************************************************
 * Copyright (c) 2015 MEDEVIT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 *******************************************************************************/
package at.medevit.elexis.befuem.contextservice.finding;

import java.lang.reflect.Method;

import ch.elexis.data.PersistentObject;
import ch.elexis.data.PersistentObjectFactory;

public class PersistentFindingFactory extends PersistentObjectFactory {
	
	public PersistentObject createFromString(String code) {
		try {
			String[] ci = code.split("::"); //$NON-NLS-1$
			Class<?> clazz = Class.forName(ci[0]);
			Method load = clazz.getMethod("load", new Class[] { String.class }); //$NON-NLS-1$
			return (PersistentObject) (load
					.invoke(null, new Object[] { ci[1] }));
		} catch (Exception ex) {
			// If we can not create the object, we can just return null, so the
			// framwerk will try
			// the following PersistenObjectFactory
			return null;
		}
	}

	/**¨
	 * create a template of an instance of a given class. A template is an instance that is
	 * not stored in the database. 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public PersistentObject doCreateTemplate(Class typ) {
		try {
			return (PersistentObject) typ.newInstance();
		} catch (Exception ex) {
			// ExHandler.handle(ex);
			return null;
		}
	}
}
