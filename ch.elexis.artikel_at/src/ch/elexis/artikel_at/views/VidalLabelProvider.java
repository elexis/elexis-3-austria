/*******************************************************************************
 * Copyright (c) 2007-2009, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    M. Descher - implemented cache
 *    
 *******************************************************************************/
package ch.elexis.artikel_at.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.artikel_at.data.Artikel_AT_Cache;
import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.core.ui.UiDesk;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.core.ui.util.viewers.DefaultLabelProvider;

public class VidalLabelProvider extends DefaultLabelProvider implements ITableColorProvider {

	private static Logger log = LoggerFactory.getLogger(VidalLabelProvider.class);

	public VidalLabelProvider() {
		if (UiDesk.getImage("VidalRed") == null) {
			UiDesk.getImageRegistry().put("VidalRed", getImageDescriptor("rsc/redbox.ico"));
		}
		if (UiDesk.getImage("VidalGreen") == null) {
			UiDesk.getImageRegistry().put("VidalGreen", getImageDescriptor("rsc/greenbox.ico"));
		}
		if (UiDesk.getImage("VidalYellow") == null) {
			UiDesk.getImageRegistry().put("VidalYellow", getImageDescriptor("rsc/yellowbox.ico"));
		}
		if (UiDesk.getImage("VidalBlack") == null) {
			UiDesk.getImageRegistry().put("VidalBlack", getImageDescriptor("rsc/blackbox.ico"));
		}
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("ch.elexis.artikel_at", path); //$NON-NLS-1$
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		String box;

		if (!(element instanceof Medikament)) {
			return Images.IMG_ACHTUNG.getImage();
		}
		Medikament art = (Medikament) element;
		box = Artikel_AT_Cache.get(art.getId(), Artikel_AT_Cache.MEDIKAMENT_AT_CACHE_ELEMENT_BOX);

		if (box != null) {
			if (box.startsWith("N")) {
				return null;
			} else if (box.startsWith("R")) {
				return UiDesk.getImage("VidalRed");
			} else if (box.startsWith("G")) {
				return UiDesk.getImage("VidalGreen");
			} else if (box.startsWith("Y")) {
				return UiDesk.getImage("VidalYellow");
			} else if (box.startsWith("B")) {
				return UiDesk.getImage("VidalBlack");
			}
		} else {
			log.error("Box is Null!");
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof Medikament) {
			Medikament art = (Medikament) element;
			return Artikel_AT_Cache.get(art.getId(), Artikel_AT_Cache.MEDIKAMENT_AT_CACHE_ELEMENT_LABEL);
		}
		return super.getColumnText(element, columnIndex);
	}

	public Color getBackground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public Color getForeground(Object element, int columnIndex) {
		// Extremely slow function
		// if (element instanceof Artikel) {
		// if (((Artikel) element).isLagerartikel()) {
		// return UiDesk.getColor(UiDesk.COL_BLUE);
		// }
		// }
		return null;
	}

}
