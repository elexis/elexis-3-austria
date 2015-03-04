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
package at.medevit.elexis.diag.eigene;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import at.medevit.elexis.diag.eigene.model.Diagnose;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.ui.UiDesk;
import ch.elexis.core.ui.views.IDetailDisplay;


public class EigeneDetailDisplay implements IDetailDisplay {
	
	private FormToolkit toolkit;
	private ScrolledForm form;

	protected Diagnose actDiag;
	
	private Section infoSection;
	private Text diagCode;
	private Text diagText;
	
	public EigeneDetailDisplay(){
	}
	
	public Class getElementClass(){
		return Diagnose.class;
	}
	
	public String getTitle(){
		return "Eigene"; //$NON-NLS-1$
	}

	@Override
	public Composite createDisplay(Composite parent, IViewSite site) {
		toolkit = UiDesk.getToolkit();
		form = toolkit.createScrolledForm(parent);
		TableWrapLayout twl = new TableWrapLayout();
		form.getBody().setLayout(twl);
		form.setText("Keine Diagnose ausgewählt.");
		
		// General Information
		infoSection = toolkit.createSection(form.getBody(), Section.COMPACT | Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.grabHorizontal = true;
		infoSection.setLayoutData(twd);
		infoSection.addExpansionListener(new SectionExpansionHandler()); 
		infoSection.setText("Details");
		
		Composite info = toolkit.createComposite(infoSection);
		twl = new TableWrapLayout();
		info.setLayout(twl);
		
		Label lbl = toolkit.createLabel(info, "Code");
		// get a bold version of the standard font
		FontData[]bfd = lbl.getFont().getFontData();
		bfd[0].setStyle(SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), bfd[0]);
		lbl.setFont(boldFont);
		
		diagCode = toolkit.createText(info, "");
		diagCode.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		diagCode.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String txt = diagCode.getText();
				String oldCode = actDiag.getCode();
				if(!txt.equals(oldCode)) {
					if(txt.length() < 25) {
						List<Diagnose> diag = Diagnose.getByCode(txt);
						if(diag.size() == 0) {
							actDiag.setCode(txt);
						} else {
							MessageBox mb = new MessageBox(UiDesk.getTopShell(), SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
							mb.setMessage("Diagnose mit dem code " + txt + " bereits vorhanden.");
							mb.open();
						}
						ElexisEventDispatcher.reload(Diagnose.class);
					} else {
						MessageBox mb = new MessageBox(UiDesk.getTopShell(), SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
						mb.setMessage("Diagnose code darf max. 25 Zeichen lang sein.");
						mb.open();
					}
				}
			}
		});
		lbl = toolkit.createLabel(info, "(max. 25 Zeichen)");
		
		lbl = toolkit.createLabel(info, "Text");
		lbl.setFont(boldFont);
		
		diagText = toolkit.createText(info, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.heightHint = 100;
		diagText.setLayoutData(twd);
		diagText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String txt = diagText.getText();
				String oldTxt = actDiag.getText();
				if(!txt.equals(oldTxt)) {
					if(txt.length() < 256) {
						actDiag.set(Diagnose.FLD_TEXT, txt);
					} else {
						MessageBox mb = new MessageBox(UiDesk.getTopShell(), SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
						mb.setMessage("Diagnose Text darf max. 255 Zeichen lang sein.");
						mb.open();
					}
				}
			}
		});
		lbl = toolkit.createLabel(info, "(max. 255 Zeichen)");
		
		infoSection.setClient(info);
		
		return form.getBody();
	}

	@Override
	public void display(Object obj) {
		if (obj instanceof Diagnose) {
			actDiag = (Diagnose)obj;
			form.setText(actDiag.getCode());

			diagCode.setText(actDiag.getCode());
			diagText.setText(actDiag.getText());

			infoSection.layout();
			form.reflow(true);
		}
	}

	private final class SectionExpansionHandler extends ExpansionAdapter {
		@Override
		public void expansionStateChanged(ExpansionEvent e) {
			form.reflow(true);
		}
	}
}
