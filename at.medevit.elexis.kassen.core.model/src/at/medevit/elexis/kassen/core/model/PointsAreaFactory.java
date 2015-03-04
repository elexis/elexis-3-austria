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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.expressions.Expression;

import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionFactory;

public class PointsAreaFactory extends DAOPersistenceFactory<IPointsArea> {
	
	private static PointsAreaFactory instance = null;
	
	/**
	 * Creating IPointsAreas for the Expressions is expensive!
	 */
	private static HashMap<Class<?>, List<IPointsArea>> factoryCache =
		new HashMap<Class<?>, List<IPointsArea>>();
	
	public static PointsAreaFactory getInstance(){
		if (instance == null)
			instance = new PointsAreaFactory();
		return instance;
	}
	
	private PointsAreaFactory(){}
	
	/**
	 * Get an IPointsArea object for a String. It can define area(s) separated by a space character
	 * and ends with the value for these areas.
	 * 
	 * <p>
	 * Example: "positioningroup(1) or positioningroup(3-8) 1,2345"
	 * </p>
	 * 
	 * @param defWithValue
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	public IPointsArea getPointsAreaForString(String defWithValue,
		Class<? extends KassenLeistung> clazz){
		checkDefinition(defWithValue);
		// get the value
		double value = getValue(defWithValue);
		// get the areas
		String definition = getDefinition(defWithValue);
		Expression expression = null;
		int weight = 0;
		try {
			expression =
				KassenExpressionFactory.getInstance().getExpressionForString(definition, clazz);
			weight = KassenExpressionFactory.getInstance().getWeightForString(definition);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Definition " + defWithValue + " not correct", e);
		}
		IPointsArea ret = new PointsArea(definition, expression, value);
		ret.setWeight(weight);
		return getPersistent(ret, clazz);
	}
	
	/**
	 * Get an IPointsArea object for a String. It can define area(s) separated by a space character
	 * and ends with the value for these areas.
	 * 
	 * <p>
	 * Example: "positioningroup(1) or positioningroup(3-8) 1,2345"
	 * </p>
	 * 
	 * @param defWithValue
	 * @param valid
	 * @param isUserDefined
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	public IPointsArea getPointsAreaForString(String defWithValue, DateRange valid,
		boolean isUserDefined, Class<? extends KassenLeistung> clazz){
		checkDefinition(defWithValue);
		// get the value
		double value = getValue(defWithValue);
		// get the areas
		String definition = getDefinition(defWithValue);
		Expression expression = null;
		int weight = 0;
		try {
			expression =
				KassenExpressionFactory.getInstance().getExpressionForString(definition, clazz);
			weight = KassenExpressionFactory.getInstance().getWeightForString(definition);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Definition " + defWithValue + " not correct", e);
		}
		IPointsArea ret = new PointsArea(definition, expression, value, valid, isUserDefined);
		ret.setWeight(weight);
		return getPersistent(ret, clazz);
	}
	
	/**
	 * Create a list of all PointsAreas for the code class from the persistence back end including
	 * DAOs.
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<IPointsArea> getPointsAreasForClass(Class<? extends KassenLeistung> clazz){
		updateCache(clazz);
		return cache;
	}
	
	/**
	 * Create a list of all current PointsAreas for the code class from the persistence back end
	 * including DAOs.
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<IPointsArea> getCurrentPointsAreasForClass(Class<? extends KassenLeistung> clazz){
		updateCache(clazz);
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		for (IPointsArea area : cache) {
			if (area.getValidToDate() == null)
				ret.add(area);
		}
		return ret;
	}
	
	/**
	 * Create a list of enabled PointsAreas for the code class from the persistence back end
	 * including DAOs.
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<IPointsArea> getEnabledPointsAreasForClass(Class<? extends KassenLeistung> clazz){
		updateCache(clazz);
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		for (IPointsArea area : cache) {
			if (area.isEnabled())
				ret.add(area);
		}
		return ret;
	}
	
	/**
	 * Return all available IPointsArea definitions for the codesystem specified by clazz
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	private List<IPointsArea> getAllPointsAreasForClass(Class<? extends KassenLeistung> clazz){
		List<PPointsArea> res = PPointsArea.getPersistentPointsAreasForClass(clazz);
		
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		if (res.size() > 0) {
			for (PPointsArea persi : res) {
				// get the value
				double value = getDoubleValue(persi.getValue());
				// get the areas
				String definition = persi.getAreaDefinition();
				Expression expression = null;
				int weight = 0;
				try {
					expression =
						KassenExpressionFactory.getInstance().getExpressionForString(definition,
							clazz);
					weight = KassenExpressionFactory.getInstance().getWeightForString(definition);
				} catch (ParseException e) {
					throw new IllegalArgumentException("Definition " + definition + " not correct",
						e);
				}
				// create the new PointsArea
				PointsArea area =
					new PointsArea(definition, expression, value, persi.getValidRange(),
						persi.getIsUserDefined());
				area.setWeight(weight);
				area.setPersistence(persi);
				ret.add(area);
			}
		}
		return ret;
	}
	
	private String getDefinition(String definition){
		String trim = definition.trim();
		int endIdx = trim.lastIndexOf(' ');
		return trim.substring(0, endIdx);
	}
	
	private double getValue(String definition){
		String[] parts = definition.split(" ");
		
		return getDoubleValue(parts[parts.length - 1]);
	}
	
	private void checkDefinition(String definition){
		if (definition == null)
			throw new IllegalArgumentException("Definition can not be null");
		
		String[] parts = definition.split(" ");
		
		if (parts == null || parts.length == 0)
			throw new IllegalArgumentException("Definition " + definition + " not correct");
		if (parts.length == 1)
			throw new IllegalArgumentException("Definition " + definition
				+ " missing area or value");
	}
	
	private double getDoubleValue(String value){
		try {
			return KassenLeistung.getDoubleForString(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Point area missing a value");
		}
	}
	
	/**
	 * Refresh the cache with the current PointsAreas for the codesystem specified by clazz
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void updateCache(Class<?> clazz){
		List<IPointsArea> cachedList = factoryCache.get(clazz);
		if (cachedList != null) {
			cache = cachedList;
		} else {
			cache = getAllPointsAreasForClass((Class<? extends KassenLeistung>) clazz);
			factoryCache.put(clazz, cache);
		}
	}
	
	protected void clearCache(){
		factoryCache.clear();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void createPersistent(IPointsArea obj, Class<?> clazz){
		obj.setPersistence(new PPointsArea(obj, (Class<? extends KassenLeistung>) clazz));
	}
}