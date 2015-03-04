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
/**
 * (c) Holger Nestermann auf
 * http://www.java-forum.org/awt-swing-swt/91526-jface-tableviewer-naechster-celleditor-enter-aktivieren.html
 * 
 * Januar 6, 2011
 */
package at.medevit.elexis.at.rezepte.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;

public class PropertyTextCellEditor extends TextCellEditor {
	private TableViewer tableViewer;
    private final int columnIndex;
    
    public PropertyTextCellEditor(TableViewer tableViewer, int columnIndex) {
        super(tableViewer.getTable());
        this.columnIndex = columnIndex;
        this.tableViewer = tableViewer;
    }
    
    @Override
    protected void handleDefaultSelection(SelectionEvent event) {    	
    	int totalNo = tableViewer.getTable().getItemCount();
        int selIndex = tableViewer.getTable().getSelectionIndex();
        if(selIndex < totalNo-1) selIndex++;
        tableViewer.getTable().setSelection(selIndex);
        Object data = tableViewer.getTable().getItem(selIndex).getData();
        tableViewer.editElement(data, columnIndex);
        
        super.handleDefaultSelection(event);
    }
    
    @Override
    protected void doSetValue(Object value) {
        if (value == null) {
            value = "";
        }
        super.doSetValue(value);
    }

    @Override
    protected void keyReleaseOccured(KeyEvent keyEvent) {
    	int totalNo;
    	int selIndex;
    	switch (keyEvent.keyCode) {
		case 16777218: // key down event
			// save and move selection down
			totalNo = tableViewer.getTable().getItemCount();
	        selIndex = tableViewer.getTable().getSelectionIndex();
	        if(selIndex < totalNo-1) {
	        	selIndex++;
	        	tableViewer.getTable().setSelection(selIndex);
	            Object data = tableViewer.getTable().getItem(selIndex).getData();
	            tableViewer.editElement(data, columnIndex);
	        }       	
			break;
		case 16777217: // key up event
			// save and move selection up
			totalNo = tableViewer.getTable().getItemCount();
	        selIndex = tableViewer.getTable().getSelectionIndex();
	        if(selIndex <= totalNo-1) {
	        	selIndex--;
	        	tableViewer.getTable().setSelection(selIndex);
	            Object data = tableViewer.getTable().getItem(selIndex).getData();
	            tableViewer.editElement(data, columnIndex);
	        }
			break;
		default:
			break;
		}
    }
    
    
}
