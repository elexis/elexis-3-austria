/*******************************************************************************
 * Copyright (c) 2007-2011, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/

package ch.elexis.artikel_at.views;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.artikel_at.data.Substance;
import ch.elexis.core.ui.UiDesk;
import ch.elexis.core.ui.util.LabeledInputField;
import ch.elexis.core.ui.util.LabeledInputField.InputData;
import ch.elexis.core.ui.util.SWTHelper;
import ch.elexis.data.Query;
import ch.rgw.tools.ExHandler;
import ch.rgw.tools.TimeTool;

public class MedikamentDetailBlatt extends Composite {
	InputData[] fields = new InputData[] {
		new InputData("Pharmazentral-Nr", "ExtInfo", InputData.Typ.STRING, "PhZNr"),
		new InputData("Zulassungs-Nr", "ExtInfo", InputData.Typ.STRING, "ZNr"),
		// new InputData("ZNrNum","ExtInfo",InputData.Typ.STRING,"ZNrNum"),
		// new InputData("SUnit","ExtInfo",InputData.Typ.STRING,"SUnit"),
		new InputData("Letzte Änderung", "ExtInfo", InputData.Typ.STRING, "DoLC"),
		// new InputData("Storage","ExtInfo",InputData.Typ.STRING,"Storage"),
		// new InputData("Quantity","ExtInfo",InputData.Typ.STRING,"Quantity"),
		// new InputData("Unit","ExtInfo",InputData.Typ.STRING,"Unit"),
		// new InputData("EnhUnitDesc","ExtInfo",InputData.Typ.STRING,"EnhUnitDesc"),
		new InputData("Kassen-VP", "ExtInfo", InputData.Typ.CURRENCY, "KVP"),
		new InputData("Apotheken-VP", "ExtInfo", InputData.Typ.CURRENCY, "AVP"),
		new InputData("Zulassungsinhaber", "ExtInfo", InputData.Typ.STRING, "ZInh"),
		new InputData("Remb", "ExtInfo", InputData.Typ.STRING, "Remb")
	
	};
	LabeledInputField.AutoForm fld;
	ScrolledForm form;
	Text fullName;
	Text tLagerung;
	Text tUnit;
	Label tSubstances, tIndikation, tRule, tRemarks, tLastUpdate;
	Group gRsigns, gSsigns, gSubstances, gIndikation, gRule, gRemarks;
	Button[] bRsigns, bSsigns;
	// Composite parent;
	final FormToolkit tk = UiDesk.getToolkit();
	
	public MedikamentDetailBlatt(Composite pr){
		super(pr, SWT.NONE);
		setLayout(new GridLayout());
		setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		form = tk.createScrolledForm(this);
		Composite ret = form.getBody();
		form.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		ret.setLayout(new GridLayout());
		// --- FULL Name
		fullName = SWTHelper.createText(tk, ret, 3, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		fullName.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		// ----
		Group g0 = new Group(ret, SWT.NONE);
		g0.setText("Packungs- und Lagerungsangaben");
		g0.setLayout(new GridLayout());
		g0.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		tUnit = tk.createText(g0, "", SWT.BORDER | SWT.READ_ONLY);
		tUnit.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		tLagerung = tk.createText(g0, "", SWT.BORDER | SWT.READ_ONLY);
		tLagerung.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		// ---- PhZNr ...
		fld = new LabeledInputField.AutoForm(ret, fields);
		fld.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		fld.setEnabled(false);
		tk.adapt(fld);
		// ----
		gRsigns = new Group(ret, SWT.NONE);
		gRsigns.setText("Rezeptzeichen und Lagerungshinweise");
		ColumnLayout cl1 = new ColumnLayout();
		cl1.topMargin = 20;
		cl1.bottomMargin = 10;
		cl1.minNumColumns = 3;
		cl1.maxNumColumns = 10;
		gRsigns.setLayout(cl1);
		gRsigns.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		bRsigns = new Button[Medikament.RSIGNS.length];
		for (int i = 0; i < Medikament.RSIGNS.length; i++) {
			bRsigns[i] = tk.createButton(gRsigns, Medikament.RSIGNS[i], SWT.CHECK);
		}
		gRsigns.setEnabled(false);
		tk.adapt(gRsigns);
		// ----
		gSsigns = new Group(ret, SWT.NONE);
		gSsigns.setText("Kassenzeichen und Texte der Sozialversicherung");
		gSsigns.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		ColumnLayout cl2 = new ColumnLayout();
		cl2.topMargin = 20;
		cl2.bottomMargin = 10;
		cl2.minNumColumns = 3;
		cl2.maxNumColumns = 10;
		bSsigns = new Button[Medikament.SSIGNS.length];
		gSsigns.setLayout(cl2);
		for (int i = 0; i < Medikament.SSIGNS.length; i++) {
			bSsigns[i] = tk.createButton(gSsigns, Medikament.SSIGNS[i], SWT.CHECK);
		}
		gSsigns.setEnabled(false);
		tk.adapt(gSsigns);
		// ---- Substanzen
		gSubstances = new Group(ret, SWT.NONE);
		gSubstances.setText("Wirkstoffe");
		gSubstances.setLayout(new GridLayout());
		gSubstances.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		tSubstances = tk.createLabel(gSubstances, "", SWT.READ_ONLY | SWT.WRAP);
		tSubstances.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		gSubstances.setEnabled(false);
		tk.adapt(gSubstances);
		// ---- Indikation
		gIndikation = new Group(ret, SWT.NONE);
		gIndikation.setText("Indikation");
		gIndikation.setLayout(new GridLayout());
		gIndikation.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		tIndikation = tk.createLabel(gIndikation, "", SWT.READ_ONLY | SWT.WRAP);
		tIndikation.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		gIndikation.setEnabled(false);
		tk.adapt(gIndikation);
		// ---- Erläuterung zu Kassenzeichen
		gRule = new Group(ret, SWT.NONE);
		gRule.setText("Erläuterung zu den Kassenzeichen");
		gRule.setLayout(new GridLayout());
		gRule.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		tRule = tk.createLabel(gRule, "", SWT.READ_ONLY | SWT.WRAP);
		tRule.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		gRule.setEnabled(false);
		tk.adapt(gRule);
		// ---- Erläuterung zu Kassenzeichen
		gRemarks = new Group(ret, SWT.NONE);
		gRemarks.setText("Hinweistext zur Verschreibung");
		gRemarks.setLayout(new GridLayout());
		gRemarks.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		tRemarks = tk.createLabel(gRemarks, "", SWT.READ_ONLY | SWT.WRAP);
		tRemarks.setLayoutData(SWTHelper.getFillGridData(1, true, 1, false));
		gRemarks.setEnabled(false);
		tk.adapt(gRemarks);
		// texte=tk.createComposite(ret);
		// texte.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		// texte.setLayout(new GridLayout());
		// tIndikation=SWTHelper.createText(tk, texte, 4, SWT.READ_ONLY|SWT.WRAP);
		// tIndikation.setText("foo");
		// tRules=SWTHelper.createText(tk, texte, 4, SWT.READ_ONLY|SWT.WRAP);
		// tRemarks=SWTHelper.createText(tk, texte, 4, SWT.READ_ONLY|SWT.WRAP);
		Hyperlink hl = tk.createHyperlink(ret, "Zeichenerklärung", SWT.NONE);
		hl.addHyperlinkListener(new HyperlinkAdapter() {
			
			@Override
			public void linkActivated(HyperlinkEvent e){
				IWorkbenchPage rnPage =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					rnPage.showView(ZeichenErklaerung.ID);
				} catch (PartInitException e1) {
					ExHandler.handle(e1);
				}
			}
			
		});
		
		Hyperlink mkml = tk.createHyperlink(ret, "Fachinformation zum Arzneimittel", SWT.NONE);
		mkml.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e){
				IWorkbenchPage qnPage =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					qnPage.showView(FachinformationArzneimittel.ID);
				} catch (PartInitException e2) {
					ExHandler.handle(e2);
				}
			}
		});
		SWTHelper.addSeparator(ret);
		tLastUpdate = tk.createLabel(ret, "tLastUpdate");
		
	}
	
	public void display(Medikament med){
		if (med.getBox().startsWith("B")) {
			form.setText(med.getExt("SName") + " [GELÖSCHT]");
			form.setToolTipText("Medikament wurde gelöscht!");
		} else {
			form.setText(med.getExt("SName"));
		}
		
		String status = med.getExt("Status");
		StringBuilder sbFullName = new StringBuilder();
		if (!status.equalsIgnoreCase(""))
			sbFullName.append("(").append(status).append(")").append(" ");
		sbFullName.append(med.getExt("OName"));
		
		if (med.getBox().startsWith("B"))
			sbFullName.append(" [GELÖSCHT]");
		fullName.setText(sbFullName.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append(med.getExt("Quantity")).append(" ").append(med.getExt("Unit")).append(" (")
			.append(med.getExt("EnhUnitDesc")).append(")");
		tUnit.setText(sb.toString());
		tLagerung.setText(med.getExt("Storage"));
		fld.reload(med);
		Map extInfo = med.getMap(Medikament.FLD_EXTINFO);
		Hashtable<String, String> ssigns = (Hashtable<String, String>) extInfo.get("SSigns");
		if (ssigns != null) {
			for (int i = 0; i < Medikament.SSIGNS.length; i++) {
				bSsigns[i].setSelection(ssigns.get(Medikament.SSIGNS[i]).equals("1"));
			}
		}
		Hashtable<String, String> rsigns = (Hashtable<String, String>) extInfo.get("RSigns");
		if (rsigns != null) {
			for (int i = 0; i < Medikament.RSIGNS.length; i++) {
				String val = rsigns.get(Medikament.RSIGNS[i]);
				bRsigns[i].setSelection(val.equals("1"));
			}
		}
		
		String Substances = extInfo.get("Substances").toString();
		if (Substances != null) {
			StringBuilder SubstanceOut = new StringBuilder();
			String[] SubstancesList = Substances.split("/");
			int noOfSubstances = Integer.parseInt(SubstancesList[0]);
			if (noOfSubstances >= 1) {
				Query<Substance> qbe = new Query<Substance>(Substance.class);
				for (int i = 1; i <= noOfSubstances; i++) {
					qbe.clear();
					qbe.add("ID", "=", SubstancesList[i]);
					List<Substance> list = qbe.execute();
					Substance subst = list.get(0);
					SubstanceOut.append(subst.get(Substance.FLD_NAME));
					if (i != noOfSubstances)
						SubstanceOut.append(" / ");
				}
			}
			tSubstances.setText(SubstanceOut.toString());
		}
		
		// Point s=getSize();
		// GridData gd=(GridData)texte.getLayoutData();
		// gd.widthHint=s.x;
		// texte.setLayoutData(gd);
		
		String t = med.getExt("RuleText");
		tRule.setText(t == null ? "" : t);
		t = med.getExt("RemarkText");
		tRemarks.setText(t == null ? "" : t);
		t = med.getExt("INDText");
		tIndikation.setText(t == null ? "" : t);
		
		FachinformationArzneimittel.setActiveMedikament(med.getExt("PhZNr"), med.getExt("ZNr"));
		TimeTool ts = new TimeTool(med.getLastUpdate());
		tLastUpdate.setText("Letztes Medikamentenupdate: " + ts.toString(TimeTool.FULL_GER));
	}
	
}
