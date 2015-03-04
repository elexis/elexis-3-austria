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
package at.medevit.elexis.kassen.core.model.expressions.parser;

import java.text.ParseException;

public class Bracket {
	boolean isOpen;
	int index;
	int depth;
	
	public Bracket(boolean isOpen, int index, int depth) {
		this.isOpen = isOpen;
		this.index = index;
		this.depth = depth;
	}
	
	public static Bracket getNextBracket(Bracket last, String string) throws ParseException {
		int offset = 0;
		if(last != null)
			offset = last.index + 1;
		
		String search = string.substring(offset);
		int nextOpen = search.indexOf('(');
		int nextClose = search.indexOf(')');
		if(nextOpen != -1 || nextClose != -1) {
			if(nextOpen != -1 && nextClose != -1) {
				// found both bracket types look for nearest
				if(nextOpen < nextClose)
					return new Bracket(true, offset + nextOpen, getNewDepth(true, last));
				else
					return new Bracket(false, offset + nextClose, getNewDepth(false, last));						
			} else if (nextClose != -1) {
				// found last closing bracket
				return new Bracket(false, offset + nextClose, getNewDepth(false, last));
			} else { // if (nextOpen != -1) {
				// found an open bracket with no closing so this case should never happen
				throw new ParseException("Bracket syntax error", string.length());
			}
		}  else {
			return null;
		}
	}
	
	private static int getNewDepth(boolean isOpen, Bracket last) {
		if(last == null)
			return 0;
		if(isOpen && last.isOpen)
			return last.depth + 1;
		else if(isOpen && !last.isOpen)
			return last.depth;
		else if(!isOpen && last.isOpen)
			return last.depth;
		else // if(!isOpen && !last.isOpen)
			return last.depth - 1;
	}
}
