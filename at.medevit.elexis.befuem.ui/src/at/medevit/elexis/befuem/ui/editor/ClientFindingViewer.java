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
package at.medevit.elexis.befuem.ui.editor;

import java.nio.charset.Charset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public class ClientFindingViewer {
	Shell shell;
	Text text;
	
	public ClientFindingViewer(NetClientFinding finding) {
		// create the shell
		shell = new Shell(Display.getDefault(), SWT.TOOL | SWT.TITLE | SWT.CLOSE | SWT.RESIZE);
		shell.setText(finding.getFindingName());
		shell.setLayout(new FillLayout());
		shell.setSize(480, 480);
		// create the text for the content
		text = new Text(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		text.setText(new String(finding.getContentAsByteArray(), Charset.forName("ISO-8859-1"))); //$NON-NLS-1$
		shell.open();
	}
}
