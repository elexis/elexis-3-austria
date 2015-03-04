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
package at.medevit.elexis.at.rezepte.ui.dnd;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import ch.elexis.data.Prescription;

public class FixMediDisplayDragListener implements DragSourceListener {

	public static final String REQUEST_MOVE = "MOVE" ;
	
	private final TableViewer viewer;

	public FixMediDisplayDragListener(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {}

	@Override
	public void dragSetData(DragSourceEvent event) {
		// Here you do the convertion to the type which is expected.
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Prescription pres = (Prescription) selection.getFirstElement();
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = REQUEST_MOVE+pres.getId();
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {}

}
