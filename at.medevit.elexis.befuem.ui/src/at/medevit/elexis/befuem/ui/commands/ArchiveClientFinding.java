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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.ui.views.BefuemView;

public class ArchiveClientFinding extends AbstractHandler {
	public static final String ID = "at.medevit.elexis.befuem.ui.commands.archiveClientFinding"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		BefuemView view = (BefuemView) HandlerUtil.getActiveWorkbenchWindow(event)
		.getActivePage().findView(BefuemView.ID);
		
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			if(strucSelection==null) {
				return null;
			}
			
			Object[] sel = strucSelection.toArray();
			if(sel != null) {
				for (Object object : sel) {
					// archive the selected finding
					if (object != null && object instanceof NetClientFinding)
						((NetClientFinding) object).setArchived(true);					
				}
			}
			
			// update inbox and archive
			view.updateInboxViewer(false);
			view.updateArchiveViewer(false);
		}

		return null;
	}

}
