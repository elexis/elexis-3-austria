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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractFindingContent {
	public enum Typ { TEXT, LAB };
	
	@XmlElement(name="text")
	protected String text;
	
	@XmlElement(name="labresult")
	protected LabResultInfo result;
	
	@XmlTransient
	ElexisFinding parent;
	
	public AbstractFindingContent() {
		
	}
	/**
	 * The subclasses are identified via this typ
	 * value
	 */
	public abstract Typ getTyp(); 
	
	/**
	 * Get a description String for the content
	 * containing patient information
	 */
	public abstract String getDescription();
}
