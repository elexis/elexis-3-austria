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
package at.medevit.elexis.at.rezepte.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import at.medevit.elexis.at.rezepte.PrintHelper;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.Hub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.core.ui.preferences.inputs.ComboFieldEditor;

public class RezeptausdruckPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public static String ID = "at.medevit.elexis.at.rezepte.ausdruck";
	public static String LRP = "RezepteAT/LocalRezeptPrinter";
	public static String NOPRINT_IF_OP_NULL = "RezeptAT/NoPrintIfOPNull";
	private static String[] printerList;

	public RezeptausdruckPreferencePage() {
		super(GRID);
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.localCfg));
	}

	@Override
	public void init(IWorkbench workbench) {
		printerList = PrintHelper.getPrinterList();
	}

	@Override
	protected void createFieldEditors() {
		addField(new ComboFieldEditor(LRP, "Rezeptdrucker", printerList,
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(NOPRINT_IF_OP_NULL,
				"Medikamente mit nicht gesetzter OP vom Druck ausnehmen",
				getFieldEditorParent()));
	}

}
