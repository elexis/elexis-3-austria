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

public class TextContent extends AbstractFindingContent {

	@Override
	public Typ getTyp() {
		return Typ.TEXT;
	}

	/**
	 * 
	 * @param text the content as string
	 * @param id a String describing the content
	 */
	public TextContent(String text, ElexisFinding parent) {
		this.text = text;
		this.parent = parent;
	}

	@Override
	public String getDescription() {
		return "Text for Patient " +
		parent.getPatientInfo().getLastname() + " " +
		parent.getPatientInfo().getFirstname() + ", " + 
		((parent.getPatientInfo().getBirthdate() == null) ? "" : parent.getPatientInfo().getBirthdate());
	}
}
