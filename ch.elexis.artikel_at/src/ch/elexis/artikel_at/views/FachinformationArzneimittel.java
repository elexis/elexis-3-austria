package ch.elexis.artikel_at.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import ch.elexis.artikel_at.PreferenceConstants;
import ch.elexis.core.data.activator.CoreHub;

public class FachinformationArzneimittel extends ViewPart {
	public static final String URL_BASE =
		"https://root.ami-info.at/company/ami-info/fachinformation.asp?";
	public static final String ID = "elexis-artikel-oesterreich.fachinformationarzneimittel";
	protected static String CURRENT_PhZnR = "";
	protected static String CURRENT_ZNr = "";
	protected static String currLoaded = "";
	private static String loadUrl = "";
	
	public static boolean setActiveMedikament(String PhZNr, String ZNr){
		CURRENT_PhZnR = PhZNr;
		CURRENT_ZNr = ZNr;
		return true;
	}
	
	@Override
	public void createPartControl(Composite parent){
		final Browser browser = new Browser(parent, SWT.NONE);
		StringBuilder sb = new StringBuilder();
		sb.append(URL_BASE);
		sb.append("uid="
			+ CoreHub.globalCfg.get(PreferenceConstants.ARTIKEL_AT_VIDAL_BENUTZERKENNUNG, ""));
		sb.append("&pid=" + CURRENT_PhZnR);
		sb.append("&znr=" + CURRENT_ZNr);
		// System.out.println(sb.toString());
		browser.setUrl(sb.toString());
	}
	
	@Override
	public void setFocus(){
		// if currLoaded != CURRENT_PhZnR -> Reload
	}
	
}
