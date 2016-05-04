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
package at.medevit.elexis.at.rezepte.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.data.Patient;
import ch.elexis.data.Prescription;
import ch.rgw.tools.TimeTool;

public class FixMediContentProvider implements IStructuredContentProvider {
	
	@Override
	public void dispose(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	/**
	 * @return {@link Prescription[]}
	 */
	public Object[] getElements(Object inputElement){
		Patient act = ElexisEventDispatcher.getSelectedPatient();
		List<Prescription> ret = new ArrayList<Prescription>();
		if (act != null) {
			List<Prescription> fix = Arrays.asList(act.getFixmedikation());
			TimeTool now = new TimeTool();
			for (Prescription pr : fix) {
				// skip stopped prescriptions 
				String endTimeStr = pr.getEndTime();
				if (!endTimeStr.isEmpty()) {
					TimeTool endTime = new TimeTool(endTimeStr);
					if (endTime.isBefore(now)) {
						continue;
					}
				}
				ret.add(pr);
			}
		}
		return ret.toArray(new Prescription[]{});
	}
	
}
