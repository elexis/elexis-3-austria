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
import at.medevit.elexis.befuem.ui.editor.ClientFindingViewer;

public class OpenClientFinding extends AbstractHandler {
	public static final String ID = "at.medevit.elexis.befuem.ui.commands.openClientFinding"; //$NON-NLS-1$
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object sel = strucSelection.getFirstElement();
			// create the view with the selected finding
			if(sel != null && sel instanceof NetClientFinding)
				new ClientFindingViewer((NetClientFinding)sel);
		}

		return null;
	}

}
