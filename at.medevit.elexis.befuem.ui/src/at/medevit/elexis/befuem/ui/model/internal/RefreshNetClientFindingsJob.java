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
package at.medevit.elexis.befuem.ui.model.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.netservice.INetClient.Location;
import at.medevit.elexis.befuem.ui.Messages;

public class RefreshNetClientFindingsJob extends Job {
	private Location location;
	private List<NetClientFinding> localFindings;
	private NetClientFindingContextJob contextJob;
	private NetClientFindingGetCurrentJob currentJob;
	
	public RefreshNetClientFindingsJob(List<NetClientFinding> list, Location location) {
		super(Messages.UpdateNetClientFindingsJob_UpdateJobTitle);
		localFindings = Collections.synchronizedList(list);
		this.location = location;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// add and remove findings from list according to new data ...
		// cancel currently running job before refreshing
		if (contextJob != null && contextJob.getState() == Job.RUNNING) {
			contextJob.cancel();
			try {
				contextJob.join();
			} catch (InterruptedException e) {
				// we just wait until the job is canceled
			}
		}
		if(Location.INBOX == location) {
			// remove archived from inbox findings
			synchronized (localFindings) {
				for (Iterator<NetClientFinding> oldi = localFindings.iterator(); oldi
						.hasNext();) {
					NetClientFinding oldf = oldi.next();
					
					if (oldf.isArchived() && location == Location.INBOX) {
						// IMPORTANT to use iterator here ... else
						// ConcurrentModificationException ...
						oldi.remove();
					}
				}
			}
		}
		
		return Status.OK_STATUS;
	}

}
