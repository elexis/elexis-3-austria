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
package at.medevit.elexis.befuem.ui.dialogs.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFindingContent;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.LabResultTest;

public class ElexisFindingTreeContentProvider implements ITreeContentProvider {

	private ArrayList<ElexisFinding> findings;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {
		this.findings = (ArrayList<ElexisFinding>)newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(findings != null)
			return findings.toArray();
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ElexisFinding) {
			// create a list that will be populated with TextContent or LabResultTest objects
			ArrayList<Object> ret = new ArrayList<Object>();
			ElexisFinding finding = (ElexisFinding) parentElement;
			List<AbstractFindingContent>content = finding.getContent();
			for(AbstractFindingContent con : content) {
				if(con.getTyp() == AbstractFindingContent.Typ.TEXT) {
					ret.add(con);
				}
				else if(con.getTyp() == AbstractFindingContent.Typ.LAB) {
					// only show unresolved tests
					List<LabResultTest> tests = finding.getUnresolvedLabResults();
					for(LabResultTest test : tests) {
						ret.add(test);
					}
				}
			}
			return ret.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ElexisFinding) {
			return true;
		}
		return false;
	}

}
