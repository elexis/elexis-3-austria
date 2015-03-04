package ch.elexis.artikel_at.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.elexis.admin.AccessControlDefaults;
import ch.elexis.artikel_at.PreferenceConstants;
import ch.elexis.artikel_at.data.Artikel_AT_Cache;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.util.SWTHelper;

public class Artikel_AT extends PreferencePage implements IWorkbenchPreferencePage {
	
	Text benutzerkennung;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk(){
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_VIDAL_BENUTZERKENNUNG, benutzerkennung
			.getText().trim());
		return super.performOk();
	}
	
	public Artikel_AT(){
		super();
		setDescription("Informationen und Konfiguration zum Modul Medikamente (AT)");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench){}
	
	@Override
	protected Control createContents(Composite parent){
		noDefaultAndApplyButton();
		final Composite ret = new Composite(parent, SWT.NONE);
		ret.setLayout(new GridLayout(2, false));
		new Label(ret, SWT.NONE).setText("Benutzerkennung vidal.at:");
		benutzerkennung = new Text(ret, SWT.NONE);
		benutzerkennung.setEditable(true);
		benutzerkennung.setText(CoreHub.globalCfg.get(
			PreferenceConstants.ARTIKEL_AT_VIDAL_BENUTZERKENNUNG, "nicht gesetzt"));
		SWTHelper.addSeparator(ret);
		new Label(ret, SWT.NONE).setText("Datum und Uhrzeit der Ver\u00f6ffentlichung:");
		new Label(ret, SWT.NONE).setText(CoreHub.globalCfg.get(
			PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBDATE, "nicht gesetzt"));
		new Label(ret, SWT.NONE).setText("Dateiname der Datenaustauschdatei:");
		new Label(ret, SWT.READ_ONLY).setText(CoreHub.globalCfg.get(
			PreferenceConstants.ARTIKEL_AT_RPHEADER_FILENAME, "nicht gesetzt"));
		new Label(ret, SWT.NONE).setText("Author der Informationen:");
		new Label(ret, SWT.READ_ONLY).setText(CoreHub.globalCfg.get(
			PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBAUTHOR, "nicht gesetzt"));
		new Label(ret, SWT.NONE).setText("Copyright-Notizen:");
		new Label(ret, SWT.READ_ONLY).setText(CoreHub.globalCfg.get(
			PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBCOPYRIGHT, "nicht gesetzt"));
		SWTHelper.addSeparator(ret);
		
		new Label(ret, SWT.NONE).setText("Cache erstellt am "
			+ CoreHub.globalCfg.get(PreferenceConstants.ARTIKEL_AT_CACHEUPDATE_TIME, "n/a"));
		Button updateCache = new Button(ret, INFORMATION);
		updateCache.setText("Cache updaten");
		updateCache.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0){
				Artikel_AT_Cache.updateCache();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0){}
		});
		
		// ---
		new Label(ret, SWT.NONE).setText("Experimentell!");
		Button cleanArtikelTable = new Button(ret, INFORMATION);
		if (!CoreHub.acl.request(AccessControlDefaults.DELETE_MEDICATION)) {
			cleanArtikelTable.setEnabled(false);
		}
		cleanArtikelTable.setText("Rezept Tabelle reinigen");
		cleanArtikelTable.setToolTipText("Reinigt die Rezepte Tabelle. "
			+ "Datensätze die weder einen existenten Patient, einen existenten "
			+ "Artikel noch ein existentes Rezept vorweisen werden gelöscht.");
		cleanArtikelTable.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0){
				Utilities.cleanPrescriptionTable();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0){
				// TODO Auto-generated method stub
				
			}
		});
		
		// ---
		new Label(ret, SWT.NONE).setText("Experimentell!");
		Button updateRefDB = new Button(ret, INFORMATION);
		updateRefDB.setText("PhZNr Referenzen updaten");
		updateRefDB.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0){
				Utilities.updateMediReferences();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0){
				// TODO Auto-generated method stub
				
			}
		});
		// --
		new Label(ret, SWT.NONE).setText("Experimentell!");
		Button cleanMedikamente = new Button(ret, INFORMATION);
		if (!CoreHub.acl.request(AccessControlDefaults.DELETE_MEDICATION)) {
			cleanArtikelTable.setEnabled(false);
		}
		cleanMedikamente.setText("Medikamente löschen");
		cleanMedikamente.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0){
				Utilities.cleanMedikamente();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0){
				// TODO Auto-generated method stub
				
			}
		});
		
		return ret;
	}
	
}