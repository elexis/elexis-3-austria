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
package at.medevit.elexis.kassen.core.model.expressions;

import java.text.ParseException;
import java.util.List;

import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PointsRange;
import at.medevit.elexis.kassen.core.model.PositionRange;
import at.medevit.elexis.kassen.core.model.expressions.internal.AndExpression;
import at.medevit.elexis.kassen.core.model.expressions.internal.InPointRangeExpression;
import at.medevit.elexis.kassen.core.model.expressions.internal.MandantSpecialityIsExpression;
import at.medevit.elexis.kassen.core.model.expressions.internal.NotExpression;
import at.medevit.elexis.kassen.core.model.expressions.internal.OrExpression;
import at.medevit.elexis.kassen.core.model.expressions.internal.VerrechenbarInGroupExpression;
import at.medevit.elexis.kassen.core.model.expressions.parser.ExpressionDefinition;
import at.medevit.elexis.kassen.core.model.expressions.parser.ExpressionStringParser;

public class KassenExpressionFactory implements IKassenExpressionFactory {
	private static int NO_WEIGHT = 0;
	private static int GROUP_WEIGHT = 1;
	private static int POINTRANGE_WEIGHT = 1;
	private static int SPECIAL_WEIGHT = 100;
	
	private static KassenExpressionFactory instance;
	
	public static KassenExpressionFactory getInstance(){
		if (instance == null)
			instance = new KassenExpressionFactory();
		return instance;
	}
	
	@Override
	public AbstractKassenExpression getExpressionForString(String definition,
		Class<? extends KassenLeistung> clazz) throws ParseException{
		ExpressionStringParser parser = new ExpressionStringParser(definition);
		ExpressionDefinition[] roots = parser.getExpressionTree();
		return buildExpressionTree(roots, clazz);
	}
	
	AbstractKassenExpression buildExpressionTree(ExpressionDefinition[] roots,
		Class<? extends KassenLeistung> clazz) throws ParseException{
		
		AndExpression and = null;
		OrExpression or = null;
		AbstractKassenExpression left = null;
		
		for (int i = 0; i < roots.length; i++) {
			AbstractKassenExpression expression = buildExpression(roots[i], clazz);
			if (expression instanceof AndExpression) {
				and = (AndExpression) expression;
				and.add(left);
			} else if (expression instanceof OrExpression) {
				or = (OrExpression) expression;
				or.add(left);
			} else {
				if (and != null) {
					and.add(expression);
					left = and;
					and = null;
				} else if (or != null) {
					or.add(expression);
					left = or;
					or = null;
				} else {
					left = expression;
				}
			}
		}
		// if the last expression was a composite
		// that composite is our left
		if (and != null)
			left = and;
		else if (or != null)
			left = or;
		return left;
	}
	
	AbstractKassenExpression buildExpression(ExpressionDefinition def,
		Class<? extends KassenLeistung> clazz) throws ParseException{
		String name = def.getName().trim();
		if (ExpressionTagNames.AND.equalsIgnoreCase(name)) {
			AndExpression ret = new AndExpression();
			if (def.getChildren() != null) {
				AbstractKassenExpression expression = buildExpressionTree(def.getChildren(), clazz);
				ret.add(expression);
			}
			return ret;
		} else if (ExpressionTagNames.OR.equalsIgnoreCase(name)) {
			OrExpression ret = new OrExpression();
			if (def.getChildren() != null) {
				AbstractKassenExpression expression = buildExpressionTree(def.getChildren(), clazz);
				ret.add(expression);
			}
			return ret;
		} else if (ExpressionTagNames.NOT.equalsIgnoreCase(name)) {
			AbstractKassenExpression expression = buildExpressionTree(def.getChildren(), clazz);
			return new NotExpression(expression);
		} else if (ExpressionTagNames.POSITIONINGROUP.equalsIgnoreCase(name)) {
			String[] rangeParameters = getRangeParameters(def.getParamters());
			PositionRange range;
			if (rangeParameters.length == 1) {
				range = new PositionRange(getGroupById(rangeParameters[0], clazz));
			} else if (rangeParameters.length == 2) {
				range =
					new PositionRange(getGroupById(rangeParameters[0], clazz), getGroupById(
						rangeParameters[1], clazz));
			} else {
				throw new ParseException("Invalid parameters " + def.getParamters() + " in "
					+ clazz, 0);
			}
			return new VerrechenbarInGroupExpression(range);
		} else if (ExpressionTagNames.MANDANTSPECIALITYIS.equalsIgnoreCase(name)) {
			KassenCodes.SpecialityCode code =
				KassenCodes.SpecialityCode.valueOf(def.getParamters().trim());
			return new MandantSpecialityIsExpression(code);
		} else if (ExpressionTagNames.POINTRANGE.equalsIgnoreCase(name)) {
			String[] parameters = getMultiParamters(def.getParamters());
			if (parameters.length == 2) {
				String[] rangeParameters = getRangeParameters(parameters[0]);
				PointsRange pr = new PointsRange(rangeParameters[0], rangeParameters[1]);
				DateRange dr = DateRange.getAbsolutRange(parameters[1]);
				return new InPointRangeExpression(pr, dr);
			} else {
				throw new ParseException("Invalid parameters " + def.getParamters() + " in "
					+ clazz, 0);
			}
		} else {
			throw new ParseException("Unknown expression [" + name + "]", 0);
		}
	}
	
	/**
	 * Expressions need a weight to make a decision if more than one is true
	 */
	@Override
	public int getWeightForString(String definition) throws ParseException{
		ExpressionStringParser parser = new ExpressionStringParser(definition);
		ExpressionDefinition[] roots = parser.getExpressionTree();
		return getWeightOfExpressionTree(roots);
	}
	
	int getWeightOfExpressionTree(ExpressionDefinition[] roots){
		int ret = 0;
		
		for (int i = 0; i < roots.length; i++) {
			ret += getWeightOfExpression(roots[i]);
		}
		return ret;
	}
	
	int getWeightOfExpression(ExpressionDefinition def){
		String name = def.getName().trim();
		if (ExpressionTagNames.AND.equalsIgnoreCase(name)) {
			if (def.getChildren() != null)
				return getWeightOfExpressionTree(def.getChildren());
			return NO_WEIGHT;
		} else if (ExpressionTagNames.OR.equalsIgnoreCase(name)) {
			if (def.getChildren() != null)
				return getWeightOfExpressionTree(def.getChildren());
			return NO_WEIGHT;
		} else if (ExpressionTagNames.NOT.equalsIgnoreCase(name)) {
			if (def.getChildren() != null)
				return getWeightOfExpressionTree(def.getChildren());
			return NO_WEIGHT;
		} else if (ExpressionTagNames.POSITIONINGROUP.equalsIgnoreCase(name)) {
			return GROUP_WEIGHT;
		} else if (ExpressionTagNames.MANDANTSPECIALITYIS.equalsIgnoreCase(name)) {
			return SPECIAL_WEIGHT;
		} else if (ExpressionTagNames.POINTRANGE.equalsIgnoreCase(name)) {
			return POINTRANGE_WEIGHT;
		} else {
			return NO_WEIGHT;
		}
	}
	
	private String[] getRangeParameters(String definition){
		String trim = definition.replace('(', ' ');
		trim = trim.replace(')', ' ').trim();
		
		String[] values = trim.split("-");
		for (int i = 0; i < values.length; i++)
			values[i] = values[i].trim();
		return values;
	}
	
	private String[] getMultiParamters(String definition){
		String trim = definition.replace('(', ' ');
		trim = trim.replace(')', ' ').trim();
		
		String[] values = trim.split(",");
		for (int i = 0; i < values.length; i++)
			values[i] = values[i].trim();
		return values;
	}
	
	private KassenLeistung getGroupById(String id, Class<? extends KassenLeistung> clazz)
		throws ParseException{
		List<? extends KassenLeistung> list =
			KassenLeistung.getCurrentLeistungenByIds(id, null, null, null, clazz);
		
		if (list.size() == 0)
			throw new ParseException("Could not find group for id [" + id + "] in " + clazz, 0);
		else if (list.size() > 1)
			throw new ParseException("Ambiguous groups for id [" + id + "] in " + clazz, 0);
		
		return list.get(0);
	}
}
