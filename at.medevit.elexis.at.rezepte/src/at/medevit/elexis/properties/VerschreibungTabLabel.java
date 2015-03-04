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
package at.medevit.elexis.properties;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import at.medevit.elexis.at.rezepte.properties.PrescriptionAdapterFactory;
import at.medevit.elexis.at.rezepte.properties.PrescriptionPropertyAdapter;
import ch.elexis.data.Prescription;

public class VerschreibungTabLabel extends AbstractPropertySection {
	
	private Prescription pres;
	private Text labelText;
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	
	public VerschreibungTabLabel(){
		// TODO Auto-generated constructor stub
	}
	
	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0){
			PrescriptionPropertyAdapter properties =
				(PrescriptionPropertyAdapter) new PrescriptionAdapterFactory().getAdapter(pres,
					IPropertySource.class);
// properties.setPropertyValue(ButtonElementProperties.PROPERTY_TEXT,
// labelText.getText());
		}
	};

	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage){
		super.createControls(parent, aTabbedPropertySheetPage);
		
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Verschreibung");
		form.getBody().setLayout(new FormLayout());
		
		FormData data;
		labelText = toolkit.createText(form.getBody(), "");

		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		labelText.setLayoutData(data);
		labelText.addModifyListener(listener);
		
		Label labelLabel = toolkit.createLabel(form.getBody(), "Bezeichnung");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(labelText, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(labelText, 0, SWT.CENTER);
		labelLabel.setLayoutData(data);
		
		toolkit.paintBordersFor(parent);
	}
	
	@Override
	public void dispose(){
		toolkit.dispose();
		super.dispose();
	}
	
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection){
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Prescription);
		this.pres = (Prescription) input;
	}
	
	@Override
	public void refresh(){
		labelText.removeModifyListener(listener);
		PrescriptionPropertyAdapter properties =
			(PrescriptionPropertyAdapter) new PrescriptionAdapterFactory().getAdapter(pres,
				IPropertySource.class);
		labelText.setText((String) properties.getPropertyValue("artikelname"));
		labelText.addModifyListener(listener);
	}
}
