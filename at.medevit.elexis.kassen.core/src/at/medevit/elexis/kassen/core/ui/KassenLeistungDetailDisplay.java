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
package at.medevit.elexis.kassen.core.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.core.ui.UiDesk;
import ch.elexis.core.ui.views.IDetailDisplay;

public abstract class KassenLeistungDetailDisplay implements IDetailDisplay {
	private FormToolkit toolkit;
	private ScrolledForm form;

	protected KassenLeistung actCode;
	
	private Section infoSection;
	private FormText positionLbl;
	private FormText groupLbl;
	private FormText pointValueLbl;
	private FormText moneyValueLbl;
	private FormText validFromLbl;
	private FormText validToLbl;
	
	private Section adviceSection;
	private FormText adviceTxt;
	
	private Section specialitySection;
	private FormText specialityTxt;
	
	/**
	 * Fetch the group for the selected KassenLeistung. This has to be done in sub
	 * classes.
	 * 
	 * @return
	 */
	protected abstract KassenLeistung getGroupForActCode();

	
	@Override
	public Composite createDisplay(Composite parent, IViewSite site) {
		toolkit = UiDesk.getToolkit();
		form = toolkit.createScrolledForm(parent);
		TableWrapLayout twl = new TableWrapLayout();
		form.getBody().setLayout(twl);
		form.setText("Keine Leistung ausgewählt.");

		// General Information
		infoSection = toolkit.createSection(form.getBody(), Section.COMPACT | Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.grabHorizontal = true;
		infoSection.setLayoutData(twd);
		infoSection.addExpansionListener(new SectionExpansionHandler()); 
		infoSection.setText("Details");
		
		Composite info = toolkit.createComposite(infoSection);
		twl = new TableWrapLayout();
		twl.numColumns = 2;
		info.setLayout(twl);
		
		toolkit.createLabel(info, "Position");
		positionLbl = toolkit.createFormText(info, true);
		positionLbl.setLayoutData(new TableWrapData(TableWrapData.FILL));
		// get a bold version of the standard font
		FontData[]bfd = positionLbl.getFont().getFontData();
		bfd[0].setStyle(SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), bfd[0]);
		positionLbl.setFont(boldFont);

		toolkit.createLabel(info, "Gruppe");
		groupLbl = toolkit.createFormText(info, true);
		groupLbl.setLayoutData(new TableWrapData(TableWrapData.FILL));
		groupLbl.setFont(boldFont);
		
		toolkit.createLabel(info, "Punkt Wert");
		pointValueLbl = toolkit.createFormText(info, true);
		pointValueLbl.setLayoutData(new TableWrapData(TableWrapData.FILL));
		pointValueLbl.setFont(boldFont);

		toolkit.createLabel(info, "Geld Wert");
		moneyValueLbl = toolkit.createFormText(info, true);
		moneyValueLbl.setLayoutData(new TableWrapData(TableWrapData.FILL));
		moneyValueLbl.setFont(boldFont);		
		
		toolkit.createLabel(info, "Gültig Ab");
		validFromLbl = toolkit.createFormText(info, true);
		validFromLbl.setLayoutData(new TableWrapData(TableWrapData.FILL));
		validFromLbl.setFont(boldFont);		

		toolkit.createLabel(info, "Gültig Bis");
		validToLbl = toolkit.createFormText(info, true);
		validToLbl.setLayoutData(new TableWrapData(TableWrapData.FILL));
		validToLbl.setFont(boldFont);		
		
		infoSection.setClient(info);
		
		// Advice Text
		adviceSection = toolkit.createSection(form.getBody(), Section.COMPACT | Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.grabHorizontal = true;
		adviceSection.setLayoutData(twd);
		adviceSection.addExpansionListener(new SectionExpansionHandler()); 
		adviceSection.setText("Hinweis");
		
		Composite advice = toolkit.createComposite(adviceSection);
		advice.setLayout(new TableWrapLayout());

		adviceTxt = toolkit.createFormText(advice, true);
		adviceTxt.setLayoutData(new TableWrapData(TableWrapData.FILL));
		
		adviceSection.setClient(advice);
		
		// Speciality 
		specialitySection = toolkit.createSection(form.getBody(), Section.COMPACT | Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.grabHorizontal = true;
		specialitySection.setLayoutData(twd);
		specialitySection.addExpansionListener(new SectionExpansionHandler()); 
		specialitySection.setText("Fachgebiete");
		
		Composite speciality = toolkit.createComposite(specialitySection);
		speciality.setLayout(new TableWrapLayout());

		specialityTxt = toolkit.createFormText(speciality, true);
		specialityTxt.setLayoutData(new TableWrapData(TableWrapData.FILL));
		
		specialitySection.setClient(speciality);
		
		return form.getBody();
	}

	@Override
	public void display(Object obj) {
		if (obj instanceof KassenLeistung) {
			actCode = (KassenLeistung) obj;
			form.setText(actCode.getLabel());
			
			adviceTxt.setText(actCode.getAdviceText(), false, false);

			if(actCode.isGroup()) {
				positionLbl.setText("", false, false);
				groupLbl.setText(actCode.getGroup() + " " + actCode.getTitle(), false, false);
				pointValueLbl.setText("", false, false);
				moneyValueLbl.setText("", false, false);
				validFromLbl.setText("", false, false);
				validToLbl.setText("", false, false);
				specialityTxt.setText("", false, false);
			} else {
				positionLbl.setText(actCode.getPosition(), false, false);
				
				KassenLeistung posGroup = getGroupForActCode();
				if(posGroup != null)
					groupLbl.setText(actCode.getPositionGroup() + " " + posGroup.getTitle(), false, false);
				
				pointValueLbl.setText(KassenLeistung.getStringForDouble(actCode.getPointValue()), false, false);
				moneyValueLbl.setText(KassenLeistung.getStringForDouble(actCode.getMoneyValue()) + " €", false, false);
				DateRange range = actCode.getValidRange();
				validFromLbl.setText(range.getFromDateAsString("dd.MM.yyyy"), false, false);
				validToLbl.setText(range.getToDateAsString("dd.MM.yyyy"), false, false);
				specialityTxt.setText(actCode.getSpecialitiesAsString(), false, false);
			}
			specialitySection.layout();
			adviceSection.layout();
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
