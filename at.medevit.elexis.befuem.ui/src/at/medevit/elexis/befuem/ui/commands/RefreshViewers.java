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
import org.eclipse.ui.handlers.HandlerUtil;

import at.medevit.elexis.befuem.ui.views.BefuemView;

public class RefreshViewers extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final BefuemView view = (BefuemView) HandlerUtil.getActiveWorkbenchWindow(event)
		.getActivePage().findView(BefuemView.ID);
		view.updateAdressbookViewer();
		view.updateArchiveViewer(true);
		view.updateInboxViewer(true);
		return null;
	}
}
