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
package at.medevit.elexis.kassen.core.model.expressions;

import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.core.data.interfaces.IVerrechenbar;

/**
 * IVariableResolver for all IVerrechenbar specific variables.
 * 
 * <li>
 * MandantSpeciality
 * </li>
 * 
 * @author thomas
 *
 */
public class VerrechenbarResolver implements IVariableResolver {

	Class<? extends IVerrechenbar> codeSystemClazz;
	IVerrechenbar obj;
	
	public static final String variableName = "Verrechenbar"; 
	public enum ResolveVariableId{CODESYSTEMCLASS, CODESYSTEMCLASSNAME, IVERRECHENBARKASSENLEISTUNGOBJ};
	
	public VerrechenbarResolver(IVerrechenbar obj, Class<? extends IVerrechenbar> codeSystemClazz) {
		this.codeSystemClazz = codeSystemClazz;
		this.obj = obj;
	}
	
	@Override
	public Object resolve(String name, Object[] args) throws CoreException {
		if(name.equals(variableName)) {
			if(args[0] instanceof ResolveVariableId) {
				ResolveVariableId id = (ResolveVariableId)args[0];
				switch (id) {
				case CODESYSTEMCLASS :
					return codeSystemClazz;
				case CODESYSTEMCLASSNAME :
					return codeSystemClazz.getName();
				case IVERRECHENBARKASSENLEISTUNGOBJ :
					if(obj instanceof KassenLeistung)
						return obj;
				}
			}
		}
		return null;
	}

}
