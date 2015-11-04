package at.medevit.elexis.at.rezepte.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import at.medevit.elexis.at.rezepte.PrintHelper;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.core.ui.preferences.inputs.ComboFieldEditor;

public class RezeptATPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
		
	public static final String BASE = "RezepteAT/";
	public static final String LRP = BASE + "LocalRezeptPrinter";
	public static final String NOPRINT_IF_OP_NULL = BASE + "NoPrintIfOPNull";
	
	private static String[] printerList;
	
	/**
	 * Create the preference page.
	 */
	public RezeptATPreferencePage(){
		super(GRID);
	}
	
	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors(){
		addField(new ComboFieldEditor(LRP, "Rezeptdrucker", printerList, getFieldEditorParent()));
		addField(new BooleanFieldEditor(NOPRINT_IF_OP_NULL,
			"Medikamente mit nicht gesetzter OP vom Druck ausnehmen", BooleanFieldEditor.DEFAULT,
			getFieldEditorParent()));
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench){
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.localCfg));
		printerList = PrintHelper.getPrinterList();
	}
	
}
