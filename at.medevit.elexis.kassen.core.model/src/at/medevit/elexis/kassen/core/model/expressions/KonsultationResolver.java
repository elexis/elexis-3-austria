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

import ch.elexis.data.Konsultation;

/**
 * IVariableResolver for all konsultation specific variables.
 * 
 * <li>
 * KonsultationCurrent
 * </li>
 * 
 * @author thomas
 *
 */
public class KonsultationResolver implements IVariableResolver {

	public static final String variableName = "Konsultation"; 
	public enum ResolveVariableId{LEISTUNGENLIST};
	
	private Konsultation konsultation;
	
	public KonsultationResolver(Konsultation konsultation) {
		this.konsultation = konsultation;
	}
	
	@Override
	public Object resolve(String name, Object[] args) throws CoreException {

		if(name.equals(variableName)) {
			if(args[0] instanceof ResolveVariableId) {
				ResolveVariableId id = (ResolveVariableId)args[0];
				switch (id) {
				case LEISTUNGENLIST:
					return konsultation.getLeistungen();
				}
			}
		}
		return null;
	}

}
