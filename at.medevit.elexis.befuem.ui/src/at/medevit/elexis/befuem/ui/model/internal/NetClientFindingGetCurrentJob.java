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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.medevit.elexis.befuem.contextservice.finding.FindingSource;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.INetClient.Location;
import at.medevit.elexis.befuem.netservice.NetClientFile;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.netserviceconsumer.NetServiceConsumer;

public class NetClientFindingGetCurrentJob extends Job {
	private List<NetClientFinding> localFindings;
	private boolean archived;
	
	public NetClientFindingGetCurrentJob(List<NetClientFinding> list, boolean archived) {
		super("befuem collecting"); //$NON-NLS-1$
		this.archived = archived;
		localFindings = Collections.synchronizedList(list);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		INetClient client = NetServiceConsumer.getConfiguredClientImplementation();
		
		try {
			List<NetClientFile> files = client.getFindings(Location.INBOX);
			for(NetClientFile file : files) {
				NetClientFinding f = new NetClientFinding(new FindingSource(file.getFile()), Location.INBOX, file.getReceptionDate());
				if(f.isArchived() == archived)
					localFindings.add(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					IStatus.ERROR, "Get Findings Job failed.", //$NON-NLS-1$
					e);
		}
		return Status.OK_STATUS;
	}

}
