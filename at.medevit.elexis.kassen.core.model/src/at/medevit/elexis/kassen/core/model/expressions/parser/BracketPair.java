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

public class BracketPair {
	Bracket start;
	Bracket end;
	
	ExpressionStringParser parent;
	
	public BracketPair(Bracket start, ExpressionStringParser parent) {
		this.start = start;
		this.parent = parent;
	}
	
	public void setEnd(Bracket end) {
		this.end = end;
	}
	
	public String getParameters() {
		return parent.content.substring(start.index + 1, end.index);
	}
	
	public String getExpressionName() {
		String search = parent.content.substring(0, start.index);
		int spaceIdx = search.lastIndexOf(' ');
		int bracketIdx = search.lastIndexOf('(');
		if(spaceIdx != -1 && bracketIdx != -1) {
			int idx = Math.max(spaceIdx, bracketIdx);
			return search.substring(idx, search.length());
		} else if(spaceIdx != -1) {
			return search.substring(spaceIdx, search.length());
		} else if(bracketIdx != -1) {
			return search.substring(bracketIdx, search.length());
		} else {
			return search.substring(0, search.length());
		}
	}
}
