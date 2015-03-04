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

import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.LabResultNonUniqueException;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.contextserviceconsumer.ContextServiceConsumer;

public class NetClientFindingContextJob extends Job {
	private List<NetClientFinding> localFindings;

	public NetClientFindingContextJob(List<NetClientFinding> list) {
		super("befuem context"); //$NON-NLS-1$
		localFindings = Collections.synchronizedList(list);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			synchronized (localFindings) {
				Iterator<NetClientFinding> i = localFindings.iterator();
				while (i.hasNext()) {
					NetClientFinding finding = i.next();
					if (finding.getDOM() == null)
						ContextServiceConsumer.fillClientFindingDOM(finding);

					if (!finding.isResolved()) {
						ContextServiceConsumer
								.fillClientFindingElexisFindings(finding);
						if (!finding.isResolved()
								&& finding.getElexisFindings() != null
								&& finding.getSenderParticipant() != null) {
							List<ElexisFinding> list = finding
									.getElexisFindings();
							for (ElexisFinding ef : list) {
								// set source information of the elexis finding
								ef.setSourceInfo(finding.getSenderParticipant());
								// now we can try to resolve
								try {
									finding.resolve();
								} catch (LabResultNonUniqueException lie) {
									// ignore non unique LabResults here
								}
							}
						}
					}
					// get rid of the DOM we want to save mem
					finding.setDOM(null);

					finding.setVisited(true);
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
				}
			}
			return Status.OK_STATUS;
		} catch (Exception e) {
			e.printStackTrace();
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					IStatus.ERROR, "Update Job failed.", //$NON-NLS-1$
					e);
		}
	}
}
