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
package at.medevit.elexis.befuem.ui.importer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;

import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.Messages;
import ch.elexis.core.ui.util.ImporterPage;
import ch.elexis.core.ui.util.SWTHelper;
import ch.rgw.tools.StringTool;

public class NetParticiantImporter extends ImporterPage {

	@Override
	public IStatus doImport(IProgressMonitor monitor) throws Exception {
		String filename = results[0];
		if (StringTool.isNothing(filename)) return new Status(Status.ERROR, "at.medevit.elexis.befuem", Messages.NetParticiantImporter_ErrorNoFilename); //$NON-NLS-1$
		
		try {			
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1"); //$NON-NLS-1$
			Reader in = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char)ch);
			}
			in.close();
			String inputData = buffer.toString();
			String[] entries = inputData.split("\\n"); //$NON-NLS-1$
			
			monitor.beginTask(Messages.NetParticiantImporter_ImportJobTitle, entries.length);
			
			for (int i = 1; i < entries.length; i++) {
				String[] line = entries[i].split(";"); //$NON-NLS-1$
				new NetworkParticipant(line[0], line[1], line[2], line[3], line[4]);
				monitor.worked(1);
			}
			monitor.done();
			
		} catch (FileNotFoundException e) {	
			return new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.NetParticiantImporter_ErrorFileNotFound+e.getMessage()); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			return new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.NetParticiantImporter_ErrorUnsupportedEncoding+e.getMessage()); //$NON-NLS-1$
		} catch (IOException e) {
			return new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.NetParticiantImporter_ErrorIO+e.getMessage()); //$NON-NLS-1$
		}
		return Status.OK_STATUS;
	}

	@Override
	public String getTitle() {
		return new String(Messages.NetParticiantImporter_Title);
	}

	@Override
	public String getDescription() {
		return new String(Messages.NetParticiantImporter_Description);
	}

	@Override
	public Composite createPage(Composite parent) {
		FileBasedImporter fbi = new FileBasedImporter(parent, this);
		fbi.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		return fbi;
	}

}
