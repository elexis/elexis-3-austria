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
package at.medevit.elexis.at.rezepte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.statushandlers.StatusManager;
import org.nightlabs.eclipse.ui.pdfrenderer.PDFFileLoader;
import org.nightlabs.eclipse.ui.pdfviewer.AutoZoom;
import org.nightlabs.eclipse.ui.pdfviewer.OneDimensionalPDFDocument;
import org.nightlabs.eclipse.ui.pdfviewer.PDFDocument;
import org.nightlabs.eclipse.ui.pdfviewer.PDFProgressMontitorWrapper;
import org.nightlabs.eclipse.ui.pdfviewer.PDFViewer;

import at.medevit.elexis.at.rezepte.model.RezeptAT;
import ch.elexis.core.ui.actions.GlobalEventDispatcher;
import ch.elexis.core.ui.views.RezeptBlatt;
import ch.elexis.data.Patient;

import com.sun.pdfview.PDFFile;

public class RezeptTransporter extends RezeptBlatt {

	public static String ID = "at.medevit.elexis.at.rezepte.RezeptTransporter";
	public static String IDPrinterUseCase = "at.medevit.elexis.at.rezepte.rezeptAusdruck";

	public static String rezeptTemplate = "/rsc/xslt/rezeptTemplate.xsl";

	public static String backgroundImageRezeptVGKK = "/rsc//vorlagen/VGKKWA.jpg";

	private static RezeptAT rezeptOutput;
	private static RezeptAT.Verschreibungen vrs;
	private static byte[] PDFStore; // Contains the preview PDF
	private static String PDFStoreTemplate = rezeptTemplate; // Stores the type
																// of template
																// stored in
																// PDFStore
	private static PDFFile pdfFile;
	private static PDFDocument pdfDocument;
	private static PDFViewer pv;
	private static Patient p;
	private Composite container;

	public RezeptTransporter() {
	}

	@Override
	public void dispose() {
		container.dispose();
		GlobalEventDispatcher.removeActivationListener(this, this);
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			container = new Composite(parent, SWT.NONE);
			container.setLayout(new GridLayout(1, true));
			pv = new PDFViewer();
			pv.setAutoZoom(AutoZoom.pageHeight);
			Control pdfViewerControl = pv.createControl(container, SWT.NONE);
			pdfViewerControl.setLayoutData(new GridData(GridData.FILL_BOTH));

			if (PDFStore != null) {
				pdfFile = PDFFileLoader.loadPdf(PDFStore, new PDFProgressMontitorWrapper(new NullProgressMonitor()));
				pdfDocument = new OneDimensionalPDFDocument(pdfFile, new NullProgressMonitor());
				pv.setPDFDocument(pdfDocument);
			}

			Composite toolbar = new Composite(container, SWT.BAR);
			toolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			toolbar.setLayout(new GridLayout(2, true));

			// Internes Printing
			Button druckenButton = new Button(toolbar, SWT.None);
			druckenButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					PrintHelper.printRezeptToRezeptPrinterInPostscript(PDFStoreTemplate, rezeptOutput);
				}
			});
			druckenButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			// String printerName =
			// PrintHelper.getConfiguredService().getName();
			// if (printerName != null) {
			// druckenButton.setText("Drucken auf " + printerName);
			// } else {
			// druckenButton.setText("Drucken auf Standarddrucker");
			// }

			// Externes Printing (i.e. PDF Export ohne Hintergrundbild)
			Button extShowButton = new Button(toolbar, SWT.None);
			extShowButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			extShowButton.setText("Extern öffnen");
			extShowButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						File temp = File.createTempFile("rezeptAT_", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$
						temp.deleteOnExit();
						FileOutputStream fos = new FileOutputStream(temp);
						fos.write(PDFStore);
						fos.close();
						Program proggie = Program.findProgram("pdf");
						if (proggie != null) {
							proggie.execute(temp.getAbsolutePath());
						} else {
							if (Program.launch(temp.getAbsolutePath()) == false) {
								Runtime.getRuntime().exec(temp.getAbsolutePath());
							}

						}

					} catch (Exception ex) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, ex.getLocalizedMessage(), ex);
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
				}
			});

		} catch (IOException e) {
			Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getLocalizedMessage(), e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
		GlobalEventDispatcher.addActivationListener(this, this);
	}

}
