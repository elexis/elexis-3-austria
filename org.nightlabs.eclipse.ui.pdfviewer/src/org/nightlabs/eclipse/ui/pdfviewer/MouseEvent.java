/* ********************************************************************
 * NightLabs PDF Viewer - http://www.nightlabs.org/projects/pdfviewer *
 * Copyright (C) 2004-2008 NightLabs GmbH - http://NightLabs.org      *
 *                                                                    *
 * This library is free software; you can redistribute it and/or      *
 * modify it under the terms of the GNU Lesser General Public         *
 * License as published by the Free Software Foundation; either       *
 * version 2.1 of the License, or (at your option) any later version. *
 *                                                                    *
 * This library is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  *
 * Lesser General Public License for more details.                    *
 *                                                                    *
 * You should have received a copy of the GNU Lesser General Public   *
 * License along with this library; if not, write to the              *
 *     Free Software Foundation, Inc.,                                *
 *     51 Franklin St, Fifth Floor,                                   *
 *     Boston, MA  02110-1301  USA                                    *
 *                                                                    *
 * Or get it online:                                                  *
 *     http://www.gnu.org/copyleft/lesser.html                        *
 **********************************************************************/
package org.nightlabs.eclipse.ui.pdfviewer;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Event fired by the {@link PDFViewer} (indirectly via {@link PropertyChangeListener}s) whenever
 * the user moves the mouse or clicks it. See the following property constants for more details:
 * <ul>
 * <li>{@link PDFViewer#PROPERTY_MOUSE_CLICKED}</li>
 * <li>{@link PDFViewer#PROPERTY_MOUSE_DRAGGED}</li>
 * <li>{@link PDFViewer#PROPERTY_MOUSE_MOVED}</li>
 * <li>{@link PDFViewer#PROPERTY_MOUSE_PRESSED}</li>
 * <li>{@link PDFViewer#PROPERTY_MOUSE_RELEASED}</li>
 * </ul>
 *
 * @version $Revision: 653 $ - $Date: 2011-08-19 14:18:39 +0200 (Fr, 19 Aug 2011) $
 * @author marco schulze - marco at nightlabs dot de
 */
public class MouseEvent
{
	private static final Logger logger = LoggerFactory.getLogger(MouseEvent.class);

	private java.awt.Point pointRelativeToComponent;
	private Point2D pointInRealCoordinate;
	private MouseButton button;
	private java.awt.event.MouseEvent awtMouseEvent;

	/**
	 * Constructor for a <code>MouseEvent</code>. Never call this yourself! Instances
	 * of this class are created inside the {@link PDFViewer} and propagated in
	 * property change events.
	 */
	public MouseEvent(
			final java.awt.Point pointRelativeToPanel, final Point2D pointInRealCoordinate,
			final java.awt.event.MouseEvent awtMouseEvent
	)
	{
		this.pointRelativeToComponent = pointRelativeToPanel;
		this.pointInRealCoordinate = pointInRealCoordinate;
		this.awtMouseEvent = awtMouseEvent;

		MouseButton button;
		switch (awtMouseEvent.getButton()) {
			case java.awt.event.MouseEvent.NOBUTTON:
				button = MouseButton.none;
				break;
			case java.awt.event.MouseEvent.BUTTON1:
				button = MouseButton.button1;
				break;
			case java.awt.event.MouseEvent.BUTTON2:
				button = MouseButton.button3;
				break;
			case java.awt.event.MouseEvent.BUTTON3:
				button = MouseButton.button3;
				break;
			default: {
				logger.warn("java.awt.event.MouseEvent.getButton() returned invalid value: " + awtMouseEvent.getButton(), new IllegalStateException("MouseEvent.getButton() returned invalid value: " + awtMouseEvent.getButton())); //$NON-NLS-1$ //$NON-NLS-2$
				button = MouseButton.none;
			}
		}

		this.button = button;
	}

	/**
	 * Get the mouse point in display coordinates relative to the left top corner
	 * of the container composite (the view panel).
	 *
	 * @return the mouse point in coordinates relative to the view panel - never <code>null</code>.
	 */
	public java.awt.Point getPointRelativeToComponent() {
		return pointRelativeToComponent;
	}

	/**
	 * Get the mouse point in real coordinates - i.e. relative to the {@link PDFDocument}
	 * in use (the point (0,0) is the left top corner of the <code>PdfDocument</code>).
	 *
	 * @return the mouse point in real coordinates - never <code>null</code>.
	 */
	public Point2D getPointInRealCoordinate() {
		return pointInRealCoordinate;
	}

	/**
	 * Get the button that was causing this <code>MouseEvent</code>. If not applicable
	 * (e.g. in a mouse-move-event), it's {@link MouseButton#none}.
	 *
	 * @return the button of this event - never <code>null</code>.
	 */
	public MouseButton getButton() {
		return button;
	}

	/**
	 * Get the original {@link java.awt.event.MouseEvent} generated by AWT.
	 *
	 * @return the original mouse event.
	 *
	 * @deprecated It is highly recommended not to use this method, because
	 * the PDF viewer's API should be independent from AWT. We only provide it
	 * for extraordinary and temporary use cases (so that you can achieve your
	 * goals already before we extended this wrapping <code>MouseEvent</code>
	 * with whatever functionality is missing to you).
	 */
	@Deprecated
	public java.awt.event.MouseEvent getAwtMouseEvent() {
		return awtMouseEvent;
	}
}
