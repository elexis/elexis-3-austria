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
package at.medevit.elexis.kassen.core.model;

import java.util.List;

public abstract class DAOPersistenceFactory<T> {
	protected List<T> cache;
	
	public T getPersistent( T obj, Class<?> clazz) {
		T ret = findPersistent(obj, clazz);
		if(ret != null)
			return ret;
		updateCache(clazz);
		ret = findPersistent(obj, clazz);
		if(ret != null)
			return ret;
		createPersistent(obj, clazz);
		return obj;
	}
	
	private T findPersistent( T lookup, Class<?> clazz) {
		if(cache == null)
			updateCache(clazz);
		for(T obj : cache) {
			if(obj.equals(lookup))
				return obj;
		}
		return null;
	}
	
	protected abstract void updateCache(Class<?> clazz);

	protected abstract void createPersistent(T obj, Class<?> clazz);
}
