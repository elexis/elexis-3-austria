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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.handlers.HandlerUtil;

import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public class OpenClientFindingAsPDF extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object sel = strucSelection.getFirstElement();
			// transform the finding to pdf and open external viewer ...
			if (sel != null && sel instanceof NetClientFinding) {
				NetClientFinding finding = (NetClientFinding) sel;
				List<ElexisFinding> efindings = finding.getElexisFindings();
				for (ElexisFinding efinding : efindings) {
					// create the pdf ...
					try {
						ByteArrayOutputStream pdf = efinding.getPdfFile();
						File temp = File.createTempFile("finding_", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$
						temp.deleteOnExit();
						FileOutputStream fos = new FileOutputStream(temp);
						fos.write(pdf.toByteArray());
						fos.close();
						
						Program pdfViewer = Program.findProgram("pdf"); //$NON-NLS-1$
						if(pdfViewer != null)
							pdfViewer.execute(temp.getAbsolutePath());
						else
							throw new ExecutionException("No pdf Viewer available."); //$NON-NLS-1$
						
					} catch (IOException e) {
						throw new ExecutionException("Error during PDF creation.", e); //$NON-NLS-1$
					}
				}
			}
		}

		return null;
	}

}
