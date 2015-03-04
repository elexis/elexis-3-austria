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

import javax.print.event.PrintJobAttributeEvent;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

public class PrintListener implements PrintJobListener,
		PrintJobAttributeListener {

	@Override
	public void printDataTransferCompleted(PrintJobEvent pje) {
		System.out.println("printDataTransferCompleted");
		//TODO: Rezept wurde an Drucker übertragen, Output Log eintrag erstellen
	}

	@Override
	public void printJobNoMoreEvents(PrintJobEvent pje) {
		System.out.println("printJobNoMoreEvents");
	}

	// -- Die unteren methoden scheinen nicht aufgerufen zu werden. :(
	@Override
	public void printJobCanceled(PrintJobEvent pje) {
		System.out.println("printJobCanceled");
	}

	@Override
	public void printJobCompleted(PrintJobEvent pje) {
		System.out.println("printJobCompleted");
	}

	@Override
	public void printJobFailed(PrintJobEvent pje) {
		System.out.println("printJobFailed");
	}

	@Override
	public void printJobRequiresAttention(PrintJobEvent pje) {
		System.out.println("printJobRequiresAttention");
	}

	@Override
	public void attributeUpdate(PrintJobAttributeEvent arg0) {
		System.out.println("Attribute updated: " + arg0);
	}

}
