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
package at.medevit.elexis.befuem.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.NetClientPreferenceDescritpion;
import at.medevit.elexis.befuem.netservice.NetClientPreferenceDescritpion.PreferenceFieldType;
import at.medevit.elexis.befuem.ui.model.PreferenceConstants;
import at.medevit.elexis.befuem.ui.netserviceconsumer.NetServiceConsumer;
import ch.elexis.core.data.activator.CoreHub;

public class NetClientPreferencePage extends PreferencePage
		implements IWorkbenchPreferencePage {
	
	private Combo combo;
	private Composite prefComp;
    private List<FieldEditor> fields = null;

	public NetClientPreferencePage() {
		super();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Control createContents(final Composite parent) {
		noDefaultAndApplyButton();
		final Composite ret = new Composite(parent, SWT.NONE);
		ret.setLayout(new FormLayout());
		
		final FormData fd_prefComp = new FormData();
		
		Label lblBitteWhlenSie = new Label(ret, SWT.NONE);
		FormData fd_lblBitteWhlenSie = new FormData();
		fd_lblBitteWhlenSie.top = new FormAttachment(0, 10);
		fd_lblBitteWhlenSie.left = new FormAttachment(0, 0);
		lblBitteWhlenSie.setLayoutData(fd_lblBitteWhlenSie);
		lblBitteWhlenSie.setText(Messages.NetClientPreferencePage_Network);
		
		combo = new Combo(ret, SWT.NONE);
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(lblBitteWhlenSie, -4, SWT.TOP);
		fd_combo.left = new FormAttachment(lblBitteWhlenSie, 6);
		combo.setLayoutData(fd_combo);
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// attach the new preference composite of the selected client
				String selectedClient = combo.getText();
				if(selectedClient != null) {
					INetClient selected = NetServiceConsumer.getClientImplementation(selectedClient);
					prefComp.setVisible(false);
					prefComp.dispose();
					prefComp = createNetClientComposite(selected, ret);
					prefComp.setLayoutData(fd_prefComp);
					ret.layout();
				}
			}
		});
		
		Label label = new Label(ret, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(combo, 7, SWT.BOTTOM);
		fd_label.top = new FormAttachment(combo, 5);
		fd_label.left = new FormAttachment(0, 0);
		fd_label.right = new FormAttachment(100, 0);
		label.setLayoutData(fd_label);
		
		String[] NetClients = NetServiceConsumer.getClientImplementations();
		for (int i = 0; i < NetClients.length; i++) {
			combo.add(NetClients[i]);
			if (NetClients[i].equalsIgnoreCase(CoreHub.globalCfg.get(
					PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET,
					"GNV"))) { //$NON-NLS-1$
				combo.select(i);
			}
		}
		
		fd_prefComp.top = new FormAttachment(label, 5);
		fd_prefComp.left = new FormAttachment(label, 0, SWT.LEFT);
		fd_prefComp.right = new FormAttachment(100, 0);
		fd_prefComp.bottom = new FormAttachment(100, 0);
		
		INetClient netClient = NetServiceConsumer.getClientImplementation(CoreHub.globalCfg.get(PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET, "GNV")); //$NON-NLS-1$
		prefComp = createNetClientComposite(netClient, ret);
		prefComp.setLayoutData(fd_prefComp);

		return ret;
	}

	private Composite createNetClientComposite(INetClient selected, Composite parent) {
		clearField();
		
		Composite clientPref = new Composite(parent, SWT.NONE);
		clientPref.setLayout(new FormLayout());
		
		Label client = new Label(clientPref, SWT.NONE);
		client.setText(selected.getNetName());
		
		FormData fd_client = new FormData();
		fd_client.top = new FormAttachment(0, 5);
		fd_client.left = new FormAttachment(0, 0);
		fd_client.right = new FormAttachment(100, 0);
		client.setLayoutData(fd_client);
		
		NetClientPreferenceDescritpion pref = selected.getPreferenceDescription();
		
		Composite prevField = null;
		Composite newField = null;
		for(int index = 0; index < pref.size(); index++) {
			newField = new Composite(clientPref, SWT.NONE);
			FormData fd_field = new FormData();
			if(prevField == null)
				fd_field.top = new FormAttachment(client, 5);
			else
				fd_field.top = new FormAttachment(prevField, 5);
				
			fd_field.left = new FormAttachment(0, 0);
			fd_field.right = new FormAttachment(100, 0);
			newField.setLayoutData(fd_field);
			
			if(pref.getFieldTypeAt(index) == PreferenceFieldType.FILE) {
				addField(new FileFieldEditor(pref.getFieldConstantAt(index), pref.getLabelTextAt(index), newField));
			} else if (pref.getFieldTypeAt(index) == PreferenceFieldType.STRING) {
				addField(new StringFieldEditor(pref.getFieldConstantAt(index), pref.getLabelTextAt(index), newField));
			}
			prevField = newField;
		}
		initField();
		
		return clientPref;
	}
	
	@Override
	public boolean performOk() {
		CoreHub.globalCfg.set(PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET, combo.getText());
		
		boolean ret = okField();
		
		// set the preferences for the selected client
		INetClient netClient = NetServiceConsumer.getClientImplementation(CoreHub.globalCfg.get(PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET, "GNV")); //$NON-NLS-1$
		NetClientPreferenceDescritpion pref = netClient.getPreferenceDescription();
		for(int index = 0; index < pref.size(); index++) {
			netClient.setPreferenceValue(pref.getFieldConstantAt(index), CoreHub.globalCfg.get(pref.getFieldConstantAt(index), "")); //$NON-NLS-1$
		}
		
		return ret;
	}

    private void addField(FieldEditor editor) {
        if (fields == null) {
			fields = new ArrayList<FieldEditor>();
		}
        fields.add(editor);
    }
    
    private void clearField() {
        if (fields != null) {
            Iterator<FieldEditor> e = fields.iterator();
            while (e.hasNext()) {
                FieldEditor pe = e.next();
                pe.setPage(null);
                pe.setPropertyChangeListener(null);
                pe.setPreferenceStore(null);
            }
            fields.clear();
        }
    }

    private boolean okField() {
        if (fields != null) {
        	Iterator<FieldEditor> e = fields.iterator();
            while (e.hasNext()) {
                FieldEditor pe = (FieldEditor) e.next();
                pe.store();
            }
        }
        return true;
    }
    
    private void initField() {
        if (fields != null) {
        	Iterator<FieldEditor> e = fields.iterator();
            while (e.hasNext()) {
                FieldEditor pe = (FieldEditor) e.next();
                pe.setPreferenceStore(getPreferenceStore());
                pe.load();
            }
        }
    }
}
