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
import java.util.ArrayList;
import java.util.List;

public class ExpressionStringParser {
	String content;
	List<BracketPair> parameters;
	
	public ExpressionStringParser(String expression) throws ParseException {
		content = expression;
		init();
	}
	
	public ExpressionDefinition[] getExpressionTree() throws ParseException {
		ExpressionDefinition[] roots = getExpressions();
		
		if(roots == null)
			throw new ParseException("No root nodes found", 0);
		
		if(roots.length > 0) {
			parseChildren(roots);
		}
		return roots;
	}
	
	ExpressionDefinition[] getExpressions() throws ParseException {
		// get all bracket pairs with depth 0
		BracketPair[] rootBrackets = getRootBrackets();
		// return null if there are no expressions
		if(rootBrackets.length == 0)
			return null;

		
		ArrayList<ExpressionDefinition> ret = new ArrayList<ExpressionDefinition>();
		
		for(int rootIdx = 0; rootIdx < rootBrackets.length; rootIdx++) {
			ret.add(new ExpressionDefinition(rootBrackets[rootIdx].getExpressionName(),	rootBrackets[rootIdx].getParameters()));
			// if there is another expression in the list the expression composite
			if(rootIdx + 1 < rootBrackets.length) {
				if(isComposite(rootBrackets[rootIdx], rootBrackets[rootIdx + 1])) {
					ret.add(getCompositeExpression(
						rootBrackets[rootIdx], rootBrackets[rootIdx + 1]));
				}
			}
		}
		return ret.toArray(new ExpressionDefinition[0]);
	}
	
	void parseChildren(ExpressionDefinition[] roots) throws ParseException {
		for(ExpressionDefinition def : roots) {
			ExpressionStringParser parser = new ExpressionStringParser(def.parameters);
			def.children = parser.getExpressions();
			if(def.children != null && def.children.length > 0)
				parseChildren(def.children);
		}
	}
	
	private boolean isComposite(BracketPair left, BracketPair right) {
		// get the string between the pairs
		String search = content.substring(left.end.index + 1, right.start.index);
		// split by spaces
		String[] parts = search.split(" ");

		// count parts with content
		int cnt = 0;
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].length() > 0)
				cnt++;
		}
		
		if(cnt == 2)
			return true;
		return false;
	}
	
	private ExpressionDefinition getCompositeExpression(BracketPair left, BracketPair right) throws ParseException {
		// get the string between the pairs
		String search = content.substring(left.end.index + 1, right.start.index);
		// split by spaces
		String[] parts = search.split(" ");
		// first part with content is the composite expression
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].length() > 0)
				return new ExpressionDefinition(parts[1], null);
		}
		throw new ParseException("Composite syntax error", left.end.index);
	}
	
	private BracketPair[] getRootBrackets() {
		ArrayList<BracketPair> ret = new ArrayList<BracketPair>();
		for(BracketPair bp : parameters) {
			if(bp.start.depth == 0)
				ret.add(bp);
		}
		return ret.toArray(new BracketPair[0]);
	}
	
	/**
	 * Build an index and pair the brackets.
	 * 
	 * @throws ParseException
	 */
	private void init() throws ParseException {
		// if there are no parameters just init with an empty array
		if(content == null) {
			parameters = new ArrayList<BracketPair>();
			return;
		}
		// create a list of all brackets of the string
		List<Bracket> brackets = new ArrayList<Bracket>();
		Bracket bracket = null;
		do {
			bracket = Bracket.getNextBracket(bracket, content);
			if(bracket != null)
				brackets.add(bracket);
		} while (bracket != null);
		// create a list of bracket pairs from the brackets
		parameters = getParamters(brackets);
	}
	
	private List<BracketPair> getParamters(List<Bracket> brackets) {
		List<BracketPair> ret = new ArrayList<BracketPair>();
		for(Bracket bracket : brackets) {
			// create new pair if open and search for last open on close
			if(bracket.isOpen) {
				ret.add(new BracketPair(bracket, this));
			} else {
				// search last open pair and set end bracket
				for(int i = ret.size() - 1; i > -1; i--) {
					if(ret.get(i).end == null) {
						ret.get(i).setEnd(bracket);
						break;
					}
				}
			}
		}
		return ret;
	}
}
