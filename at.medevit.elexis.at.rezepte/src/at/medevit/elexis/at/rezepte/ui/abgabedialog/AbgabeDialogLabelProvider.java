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
package at.medevit.elexis.at.rezepte.ui.abgabedialog;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.medevit.elexis.at.rezepte.Activator;
import at.medevit.elexis.at.rezepte.model.Verschreibung;
import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.data.Artikel;

import com.swtdesigner.ResourceManager;

public class AbgabeDialogLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Verschreibung pres = ((Verschreibung) element);
		Artikel art = pres.getPrescription().getArtikel();
		if (columnIndex == 1) {
			if (art instanceof Medikament) {
				Medikament med = (Medikament) art;
				String box = med.getBox();
				if (box.startsWith("G"))
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
							"rsc/icons/bullet_square_green.png");
				if (box.startsWith("Y"))
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
							"rsc/icons/bullet_square_yellow.png");
				if (box.startsWith("R"))
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
							"rsc/icons/bullet_square_red.png");
				if (box.startsWith("B"))
					return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
							"rsc/icons/bullet_square_grey.png");
			}
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
			"rsc/icons/invisiblebox.png");
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Verschreibung) {
			switch (columnIndex) {
			case 0:
				return element == null ? "" : ((Verschreibung) element)
						.getOriginalpackung();
			case 1:
				return element == null ? "" : ((Verschreibung) element)
						.getArtikelname();
			case 2:
				return element == null ? "" : ((Verschreibung) element)
						.getDosierung();
			default:
				return "empty";
			}
		}
		return "";
	}

}
