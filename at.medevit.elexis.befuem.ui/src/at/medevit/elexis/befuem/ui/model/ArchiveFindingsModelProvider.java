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
package at.medevit.elexis.befuem.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.jobs.IJobChangeListener;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.netservice.INetClient.Location;
import at.medevit.elexis.befuem.ui.model.internal.RefreshNetClientFindingsJob;
import at.medevit.elexis.befuem.ui.model.internal.UpdateNetClientFindingsJob;

public class ArchiveFindingsModelProvider {
	private static ArchiveFindingsModelProvider content;
	private List<NetClientFinding> findings;

	private ArchiveFindingsModelProvider() {
		findings = new ArrayList<NetClientFinding>();
	}

	public static synchronized ArchiveFindingsModelProvider getInstance() {
		if (content != null) {
			return content;
		}
		content = new ArchiveFindingsModelProvider();
		return content;
	}

	public List<NetClientFinding> getArchiveFindings() {
		return findings;
	}

	public void updateModel() {
		// start job that will update the findings for us ...
		UpdateNetClientFindingsJob updateJob = new UpdateNetClientFindingsJob(findings, Location.ARCHIVE);
		updateJob.setUser(false);
		updateJob.schedule();
	}
	
	public void updateModel(IJobChangeListener listener) {
		// start job that will update the findings for us ...
		UpdateNetClientFindingsJob updateJob = new UpdateNetClientFindingsJob(findings, Location.ARCHIVE);
		updateJob.setUser(false);
		updateJob.schedule();
		if(listener != null)
			updateJob.addJobChangeListener(listener);
	}

	public void refreshModel(IJobChangeListener listener) {
		// start job that will update the findings for us ...
		RefreshNetClientFindingsJob updateJob = new RefreshNetClientFindingsJob(findings, Location.ARCHIVE);
		updateJob.setUser(false);
		updateJob.schedule();
		if(listener != null)
			updateJob.addJobChangeListener(listener);	}
}
