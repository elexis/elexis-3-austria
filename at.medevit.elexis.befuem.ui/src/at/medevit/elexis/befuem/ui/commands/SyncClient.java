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
package at.medevit.elexis.befuem.ui.commands;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.INetClient.State;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.Messages;
import at.medevit.elexis.befuem.ui.netserviceconsumer.NetServiceConsumer;
import at.medevit.elexis.befuem.ui.views.BefuemView;

public class SyncClient extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final INetClient client = NetServiceConsumer.getConfiguredClientImplementation();
		final BefuemView view = (BefuemView) HandlerUtil.getActiveWorkbenchWindow(event)
		.getActivePage().findView(BefuemView.ID);
		Job job = new Job(Messages.SyncClient_SyncJobTitle + client.getNetName()) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					client.synchronizeFindings();
					if(client.getState() == State.ERROR) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.SyncClient_SyncJobClientFailure);
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
					// refresh the viewers
					view.updateInboxViewer(true);
					view.updateArchiveViewer(true);
				} catch (IOException e) {
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.SyncClient_SyncJobFailure, e);
					StatusManager.getManager().handle(status, StatusManager.SHOW);
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		return null;

	}

}
