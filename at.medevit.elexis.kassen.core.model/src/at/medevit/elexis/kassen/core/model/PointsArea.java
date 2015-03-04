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
package at.medevit.elexis.kassen.core.model;

import java.text.ParseException;
import java.util.Date;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionFactory;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;
import at.medevit.elexis.kassen.core.model.expressions.VerrechenbarResolver;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.data.PersistentObject;

/**
 * Represents an area of {@link KassenLeistung} which have the same multiplier. The multiplier is
 * used to calculate the money value from the points which are defined in the {@link KassenLeistung}
 * .
 * 
 * @author thomas
 * 
 */
public class PointsArea implements IPointsArea {
	
	private PPointsArea persistence;
	
	double value;
	int weight = -1;
	boolean userDefined = false;
	boolean enabled = true;
	
	DateRange validRange = null;
	Expression expression = null;
	
	String definition;
	
	/**
	 * Create a new PointsArea with defined valid range and userDefined value. Should only be used
	 * by {@link PointsAreaFactory}.
	 * 
	 * @param areas
	 * @param moneyValue
	 */
	public PointsArea(String definition, Expression expression, double moneyValue, DateRange valid,
		boolean isUserDefined){
		this.definition = definition;
		this.expression = expression;
		value = moneyValue;
		validRange = valid;
		userDefined = isUserDefined;
	}
	
	/**
	 * Create a new PointsArea which is valid from the creation date and is not user defined. Should
	 * only be used by {@link PointsAreaFactory}.
	 * 
	 * @param areas
	 * @param moneyValue
	 */
	public PointsArea(String definition, Expression expression, double moneyValue){
		this.definition = definition;
		this.expression = expression;
		value = moneyValue;
		validRange = new DateRange(new Date());
	}
	
	@Override
	public boolean includesPosition(KassenLeistung position, Date date){
		return includesByDate(date) && includesByExpression(position);
	}
	
	@Override
	public double getMoneyValue(){
		return value;
	}
	
	@Override
	public void setMoneyValue(double moneyValue){
		value = moneyValue;
		persistence.setValueAsDouble(moneyValue);
	}
	
	@Override
	public String getLabel(){
		return getAreaDefinition() + " " + getValue();
	}
	
	@Override
	public String getAreaDefinition(){
		return definition;
	}
	
	@Override
	public void setAreaDefinition(String definition, Class<? extends KassenLeistung> clazz)
		throws ParseException{
		// create the expression representing the areas
		expression =
			KassenExpressionFactory.getInstance().getExpressionForString(definition, clazz);
		weight = KassenExpressionFactory.getInstance().getWeightForString(definition);
		persistence.setAreaDefinition(definition);
	}
	
	@Override
	public String getValue(){
		return KassenLeistung.getStringForDouble(value);
	}
	
	private boolean includesByDate(Date date){
		return validRange.isIncluded(date);
	}
	
	/**
	 * Group the area test results by area class. And only return true if an area of each class
	 * matches.
	 * 
	 * @param position
	 * @return true if an area of each class matches
	 */
	private boolean includesByExpression(KassenLeistung position){
		// setup the context for evaluation
		IVariableResolver[] resolvers =
			{
				new MandantResolver(CoreHub.actMandant),
				new VerrechenbarResolver(position, position.getClass())
			};
		EvaluationContext context = new EvaluationContext(null, "", resolvers);
		EvaluationResult result = EvaluationResult.FALSE;
		try {
			result = expression.evaluate(context);
		} catch (CoreException e) {
			// TODO inform user about the status
			e.printStackTrace();
		}
		return result == EvaluationResult.TRUE;
	}
	
	@Override
	public int getWeight(){
		return weight;
	}
	
	@Override
	public void setWeight(int weight){
		this.weight = weight;
		PointsAreaFactory.getInstance().clearCache();
	}
	
	@Override
	public Date getValidFromDate(){
		return validRange.getFromDate();
	}
	
	@Override
	public Date getValidToDate(){
		return validRange.getToDate();
	}
	
	@Override
	public boolean isUserDefined(){
		return userDefined;
	}
	
	@Override
	public void setUserDefined(boolean isUser){
		userDefined = isUser;
		persistence.setIsUserDefined(userDefined);
		PointsAreaFactory.getInstance().clearCache();
	}
	
	@Override
	public void setValidFromDate(Date validFrom){
		validRange.setFromDate(validFrom);
		persistence.setValidRange(validRange);
		PointsAreaFactory.getInstance().clearCache();
	}
	
	@Override
	public void setValidToDate(Date validTo){
		validRange.setToDate(validTo);
		persistence.setValidRange(validRange);
		PointsAreaFactory.getInstance().clearCache();
	}
	
	@Override
	public void setIsEnabled(boolean value){
		enabled = value;
		if (persistence != null) {
			persistence.setIsEnabled(enabled);
			PointsAreaFactory.getInstance().clearCache();
		}
	}
	
	@Override
	public boolean isEnabled(){
		if (persistence != null)
			enabled = persistence.getIsEnabled();
		return enabled;
	}
	
	@Override
	public void setPersistence(PersistentObject persi){
		this.persistence = (PPointsArea) persi;
	}
	
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof PointsArea))
			return false;
		PointsArea other = (PointsArea) obj;
		boolean sameDefinition = getAreaDefinition().equalsIgnoreCase(other.getAreaDefinition());
		boolean sameValidRange = validRange.equals(other.validRange);
		
		return sameDefinition && sameValidRange;
	}
}
