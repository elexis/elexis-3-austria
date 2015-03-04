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

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.medevit.elexis.kassen.core.model.KassenLeistung;

/**
 * Filter for KassenLeistung objects. All comparison is done on lower case
 * strings.
 * 
 * @author thomas
 * 
 */
public class KassenLeistungPositionTitleFilter extends ViewerFilter {

	private String searchString;

	public void setSearchText(String s) {
		if (s == null || s.length() == 0)
			searchString = s;
		else
			searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

    /**
     * Check if the current (leaf) element is a match with the filter text.  
     * The default behavior checks that the label of the element is a match. 
     * 
     * Subclasses should override this method.
     * 
     * @param viewer the viewer that contains the element
     * @param element the tree element to check
     * @return true if the given element's label matches the filter text
     */
    protected boolean isLeafMatch(Viewer viewer, Object element){
    	KassenLeistung leistung = (KassenLeistung) element;
        String position = leistung.getPosition();
        String title = leistung.getTitle();
        
        return stringLowerMatch(position) || stringLowerMatch(title);  
    }
	
    /**
     * Returns true if any of the elements makes it through the filter.
     * This method uses caching if enabled; the computation is done in
     * computeAnyVisible.
     *  
     * @param viewer
     * @param parent
     * @param elements the elements (must not be an empty array)
     * @return true if any of the elements makes it through the filter.
     */
    private boolean isAnyVisible(Viewer viewer, Object parent, Object[] elements) {
		boolean elementFound = false;
		for (int i = 0; i < elements.length && !elementFound; i++) {
			Object element = elements[i];
			elementFound = isElementVisible(viewer, element);
		}
		return elementFound;
    }
    
    /**
     * Check if the parent (category) is a match to the filter text.  The default 
     * behavior returns true if the element has at least one child element that is 
     * a match with the filter text.
     * 
     * Subclasses may override this method.
     * 
     * @param viewer the viewer that contains the element
     * @param element the tree element to check
     * @return true if the given element has children that matches the filter text
     */
    protected boolean isParentMatch(Viewer viewer, Object element){
        Object[] children = ((ITreeContentProvider) ((AbstractTreeViewer) viewer)
                .getContentProvider()).getChildren(element);

        if ((children != null) && (children.length > 0)) {
			return isAnyVisible(viewer, element, children);
		}	
        return false;
    }
	
    /**
     * Answers whether the given element in the given viewer matches
     * the filter pattern.  This is a default implementation that will 
     * show a leaf element in the tree based on whether the provided  
     * filter text matches the text of the given element's text, or that 
     * of it's children (if the element has any).  
     * 
     * Subclasses may override this method.
     * 
     * @param viewer the tree viewer in which the element resides
     * @param element the element in the tree to check for a match
     * 
     * @return true if the element matches the filter pattern
     */
    public boolean isElementVisible(Viewer viewer, Object element){
    	return isParentMatch(viewer, element) || isLeafMatch(viewer, element);
    }
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
        return isElementVisible(viewer, element);
	}

	private boolean stringLowerMatch(String match) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}

		if (match != null && match.toLowerCase().matches(searchString)) {
			return true;
		}
		return false;
	}
}
