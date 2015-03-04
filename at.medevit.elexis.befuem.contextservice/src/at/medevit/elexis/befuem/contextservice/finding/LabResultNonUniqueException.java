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

import java.util.List;

import ch.elexis.data.LabItem;

/**
 * Exception thrown if there are more than one LabItems matching for a LabResult object.
 * The LabItems are supplied for further handling of the situation.
 * 
 * @author thomas
 *
 */
@SuppressWarnings("serial")
public class LabResultNonUniqueException extends Exception {

	List<LabItem> items;
	
	
	public LabResultNonUniqueException(List<LabItem> items) {
		this.items = items;
	}

	public List<LabItem> getItems() {
		return items;
	}
	
	public String getItemsAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Items:\n");
		for(LabItem item : items) {
			sb.append(item.getLabel());
			sb.append("\n");
		}
		return sb.toString();
	}
}
