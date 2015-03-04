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
package at.medevit.elexis.befuem.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.ui.netserviceconsumer.NetServiceConsumer;

public class ShowClientMessage extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		INetClient client = NetServiceConsumer.getConfiguredClientImplementation();
		
		String message = client.getStatusMessage();
		if(message != null)
			new MessageTooltip(message);
		
		return null;
	}

	private class MessageTooltip {
		// Implement a "fake" tooltip
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Text label = (Text) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDoubleClick:
					shell.dispose();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};
		
		MessageTooltip(String message) {
			Display display = Display.getDefault();
		 	Shell tip = new Shell (display, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
		 	tip.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
		 	
		 	FillLayout layout = new FillLayout ();
		 	layout.marginWidth = 2;
		 	tip.setLayout (layout);
		 	Text text = new Text (tip, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		 	text.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
		 	text.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
		 	text.setText (message);
		 	text.addListener (SWT.MouseExit, labelListener);
		 	text.addListener (SWT.MouseDoubleClick, labelListener);
		 	// position the tooltip
		 	Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
		 	Point pt = display.getCursorLocation();
		 	Rectangle screen = display.getBounds();
		 	if((pt.x + size.x) > screen.width) {
		 		pt.x = screen.width - size.x;
		 	}
		 	tip.setBounds (pt.x, pt.y, size.x, size.y);
		 	tip.setVisible (true);
		}
	}
}
