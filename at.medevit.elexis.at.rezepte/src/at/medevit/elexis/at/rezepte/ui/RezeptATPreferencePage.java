package at.medevit.elexis.at.rezepte.ui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import at.medevit.elexis.at.rezepte.PrintHelper;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.core.ui.preferences.inputs.ComboFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

public class RezeptATPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
		
	public static final String BASE = "RezepteAT/";
	public static final String LRP = BASE + "LocalRezeptPrinter";
	public static final String NOPRINT_IF_OP_NULL = BASE + "NoPrintIfOPNull";
	public static final String ARZT_MENUMMER = BASE + "ArztMENummer";
	public static final String ARZT_NAME = BASE + "ArztName";
	public static final String ARZT_ZEILE3 = BASE + "ArztZeile3";
	public static final String ARZT_ANSCHRIFT = BASE + "ArztAnschrift";
	
	private static String[] printerList;
	
	/**
	 * Create the preference page.
	 */
	public RezeptATPreferencePage(){
		super(GRID);
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.mandantCfg));
	}
	
	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors(){
		// Create the field editors
		addField(new ComboFieldEditor(LRP, "Rezeptdrucker", printerList, getFieldEditorParent()));
		addField(new BooleanFieldEditor(NOPRINT_IF_OP_NULL,
			"Medikamente mit nicht gesetzter OP vom Druck ausnehmen", BooleanFieldEditor.DEFAULT,
			getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_MENUMMER, "Arzt ME Nummer", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_NAME, "Arzt Name", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_ZEILE3, "Arzt Zeile 3", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor(ARZT_ANSCHRIFT, "Arzt Anschrift", -1,
			StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench){
		printerList = PrintHelper.getPrinterList();
	}
	
}
