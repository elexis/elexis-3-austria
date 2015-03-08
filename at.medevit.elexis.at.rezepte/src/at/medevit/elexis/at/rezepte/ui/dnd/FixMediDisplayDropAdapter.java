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
package at.medevit.elexis.at.rezepte.ui.dnd;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.TableItem;

import at.medevit.elexis.at.rezepte.ui.FixMediOrderingComparator;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.model.IPersistentObject;
import ch.elexis.core.ui.Hub;
import ch.elexis.data.Artikel;
import ch.elexis.data.Patient;
import ch.elexis.data.Prescription;
import ch.rgw.tools.TimeTool;

public class FixMediDisplayDropAdapter extends ViewerDropAdapter {
	
	private TableViewer viewer;
	private int dropLocation;
	private Prescription dropTarget;
	private LinkedList<Prescription> clp;
	
	public FixMediDisplayDropAdapter(TableViewer viewer){
		super(viewer);
		this.viewer = viewer;
		setFeedbackEnabled(true);
	}
	
	@Override
	public void drop(DropTargetEvent event){
		dropLocation = determineLocation(event);
		dropTarget = (Prescription) determineTarget(event);
		if (clp != null)
			clp.clear();
		clp = new LinkedList<Prescription>(Arrays.asList(((Prescription[]) viewer.getInput())));
		Collections.sort(clp, new FixMediOrderingComparator());
		super.drop(event);
	}
	
	@Override
	public boolean performDrop(Object data){
		String drp = (String) data;
		
		if (drp.startsWith(FixMediDisplayDragListener.REQUEST_MOVE)) {
			String id = drp.replaceFirst(FixMediDisplayDragListener.REQUEST_MOVE, "").trim();
			Prescription dropSource = Prescription.load(id);
			
			int orderTarget = 0;
			int orderSource = 0;
			try {
				if (dropTarget != null)
					orderTarget = Integer.parseInt(dropTarget.get(Prescription.FLD_COUNT));
				orderSource = Integer.parseInt(dropSource.get(Prescription.FLD_COUNT));
			} catch (NumberFormatException e) {
				updateElementLocations();
			}
//			System.out.println("-----");
//			printOrder(clp, dropLocation);
//			
			switch (dropLocation) {
			case LOCATION_BEFORE:
				if (orderTarget >= 1) {
					clp.remove(dropSource);
//					printOrder(clp, dropLocation);
					clp.add(clp.indexOf(dropTarget), dropSource);
				} else {
					System.out.println("dropBefore "+orderTarget);
					clp.remove(dropSource);
//					printOrder(clp, dropLocation);
					clp.addFirst(dropSource);
				}
				break;
			case LOCATION_AFTER:
				clp.remove(dropSource);
//				printOrder(clp, dropLocation);
				clp.add(clp.indexOf(dropTarget)+1, dropSource);
				break;
			case LOCATION_ON: // Swap places
				clp.set(orderTarget, dropSource);
				clp.set(orderSource, dropTarget);
				break;
			case LOCATION_NONE:
				clp.remove(dropSource);
//				printOrder(clp, dropLocation);
				clp.addLast(dropSource);
				break;
			default:
				break;
			}
//			printOrder(clp, dropLocation);
			for (int i = 0; i < clp.size(); i++) {
				Prescription prescription = clp.get(i);
				prescription.set(Prescription.FLD_COUNT, i + "");
			}
			viewer.refresh();
//			printOrder(clp, dropLocation);
			
		} else {
			String[] dl = drp.split(","); //$NON-NLS-1$
			
			Patient pat = ElexisEventDispatcher.getSelectedPatient();
			for (String obj : dl) {
				IPersistentObject dropped = CoreHub.poFactory.createFromString(obj);
				if (dropped instanceof Artikel) {
					Prescription pre = new Prescription((Artikel) dropped, pat, "", "");
					pre.set(Prescription.FLD_DATE_FROM, new TimeTool().toString(TimeTool.DATE_GER));
					viewer.refresh();
					updateElementLocations();
				}
			}
		}
		
		return true;
	}
	
	private void printOrder(LinkedList<Prescription> clp2, int dropLocation2){
		System.out.print(dropLocation2+": ");
		for (Prescription prescription : clp2) {
			System.out.print(prescription.getLabel().substring(0, 6)+"\t");
		}
		System.out.print("\n");
	}

	@Override
	public void dragEnter(DropTargetEvent event){
		if (ElexisEventDispatcher.getSelectedPatient() == null) {
			event.detail = DND.DROP_NONE;
		} else {
			if (event.detail == DND.DROP_DEFAULT)
				event.detail = DND.DROP_COPY;
		}
	}
	
	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType){
		return true;
	}
	
	private void updateElementLocations(){
		TableItem[] ti = viewer.getTable().getItems();
		for (int i = 0; i < ti.length; i++) {
			TableItem tableItem = ti[i];
			Prescription p = (Prescription) tableItem.getData();
			p.set(Prescription.FLD_COUNT, findRowForElement(p) + "");
		}
	}
	
	/**
	 * Find the row number of the element in the tableviewer
	 * 
	 * @param pre
	 * @return
	 */
	private int findRowForElement(Prescription pre){
		TableItem[] ti = viewer.getTable().getItems();
		for (int i = 0; i < ti.length; i++) {
			TableItem tableItem = ti[i];
			if (tableItem.getData().equals(pre)) {
				return viewer.getTable().indexOf(tableItem);
			}
		}
		return -1;
	}
	
}
