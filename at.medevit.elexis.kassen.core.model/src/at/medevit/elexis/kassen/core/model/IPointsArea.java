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

/**
 * Definition of an area that maps to a specific money value.
 * 
 * @author thomas
 */
public interface IPointsArea extends IDAOPersistentObject {

	/**
	 * Get the weight of this point area. The value is calculated by the amount
	 * and types of tests defining the area. Areas with greater weight 
	 * should overrule other areas.
	 * 
	 * @return
	 */
	int getWeight();
	
	void setWeight(int weight);

	/**
	 * Test if the specified position is part of the area.
	 * 
	 * @param position
	 * @return
	 */
	boolean includesPosition(KassenLeistung position, Date date);

	/**
	 * Get the money value for the area.
	 * 
	 * @return
	 */
	double getMoneyValue();

	/**
	 * Set the money value of the area.
	 */
	void setMoneyValue(double value);

	/**
	 * Get the Date this area starts to be valid.
	 * Always returns a Date object.
	 * 
	 * @return
	 */
	Date getValidFromDate();

	void setValidFromDate(Date validFrom);
	
	/**
	 * Get the Date this area stopped to be valid.
	 * Returns null if the area is still valid.
	 * 
	 * @return
	 */
	Date getValidToDate();

	void setValidToDate(Date validTo);
	
	/**
	 * Test if the area was created by the user. This is important to determine
	 * which areas to invalidate when doing an update.
	 * 
	 * @return
	 */
	boolean isUserDefined();
	
	void setUserDefined(boolean isUser);

	/**
	 * Get a readable String representation of the area.
	 * 
	 * @return
	 */
	String getLabel();

	/**
	 * Get the definition of the area as string.
	 * 
	 * @return
	 */
	String getAreaDefinition();

	void setAreaDefinition(String text, Class<? extends KassenLeistung> clazz) throws ParseException;
	
	/**
	 * Get the value of the area as string.
	 * 
	 * @return
	 */
	String getValue();
	
	/**
	 * Enable or disable the PointsArea.
	 */
	void setIsEnabled(boolean b);
	
	boolean isEnabled();
}
