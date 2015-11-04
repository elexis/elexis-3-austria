package at.medevit.elexis.at.rezepte.ui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

import static at.medevit.elexis.at.rezepte.ui.RezeptATPreferencePage.BASE;

public class RezeptATMandatorPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
		
	public static final String ARZT_MENUMMER = BASE + "ArztMENummer";
	public static final String ARZT_NAME = BASE + "ArztName";
	public static final String ARZT_ZEILE3 = BASE + "ArztZeile3";
	public static final String ARZT_ANSCHRIFT = BASE + "ArztAnschrift";
	
	/**
	 * Create the preference page.
	 */
	public RezeptATMandatorPreferencePage(){
		super(GRID);
	}
	
	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors(){		
		addField(new StringFieldEditor(ARZT_MENUMMER, "Arzt ME Nummer", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_NAME, "Arzt Name", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_ZEILE3, "Arzt Zeile 3", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_ANSCHRIFT, "Arzt Anschrift", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench){
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.mandantCfg));
	}
}
