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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import at.medevit.elexis.kassen.core.model.IPointsArea;

public class PointsAreaStyledCellLabelProvider extends StyledCellLabelProvider {

	public enum ContentTyp {COLUMN_VALIDFROM, COLUMN_VALIDTO, COLUMN_AREADEFINITION, COLUMN_VALUE};
	
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
	
	private static Color colorDisabledFg = Display.getCurrent().getSystemColor(
			SWT.COLOR_WIDGET_DARK_SHADOW);
	
	private static Color colorDisabledBg = Display.getCurrent().getSystemColor(
			SWT.COLOR_WIDGET_LIGHT_SHADOW);
	
	private static Color colorEnabledFg = Display.getCurrent().getSystemColor(
			SWT.COLOR_BLACK);

	private static Color colorEnabledBg = Display.getCurrent().getSystemColor(
			SWT.COLOR_WHITE);
	
	private ContentTyp typ;
	
	public PointsAreaStyledCellLabelProvider(ContentTyp typ) {
		super();
		this.typ = typ;
	}

	@Override
	public void update(ViewerCell cell) {
		IPointsArea area = (IPointsArea) cell.getElement();
		cell.setText(getText(area));
		
		if(!area.isEnabled()) {
			cell.setForeground(colorDisabledFg);
			cell.setBackground(colorDisabledBg);
		} else {
			cell.setForeground(colorEnabledFg);
			cell.setBackground(colorEnabledBg);			
		}
		
		super.update(cell);
	}
	
	private String getText(IPointsArea area) {
		Date date;
		switch(typ) {
		case COLUMN_VALIDFROM:
			date = area.getValidFromDate();
			if(date == null)
				return "-";
			else
				return dateformat.format(date);
		case COLUMN_VALIDTO:
			date = area.getValidToDate();
			if(date == null)
				return "-";
			else
				return dateformat.format(date);
		case COLUMN_AREADEFINITION:
			return area.getAreaDefinition();
		case COLUMN_VALUE:
			return area.getValue();
		default:
			return area.getLabel();
		}
	}
}
