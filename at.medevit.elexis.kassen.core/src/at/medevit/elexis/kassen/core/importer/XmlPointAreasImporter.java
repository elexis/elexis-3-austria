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
package at.medevit.elexis.kassen.core.importer;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import at.medevit.elexis.kassen.core.importer.model.PointArea;
import at.medevit.elexis.kassen.core.importer.model.PointAreas;
import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.IPointsArea;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PointsAreaFactory;

public class XmlPointAreasImporter {
	
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yy");
	
	public static PointAreas getPointAreas(InputStream is){
		PointAreas ret = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(PointAreas.class);
			Unmarshaller u = jc.createUnmarshaller();
			ret = (PointAreas) u.unmarshal(is);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void initializePointAreas(PointAreas areas, Class<? extends KassenLeistung> clazz){
		List<IPointsArea> existing = PointsAreaFactory.getInstance().getPointsAreasForClass(clazz);
		if (existing.size() == 0) {
			createNewPointAreas(areas, clazz);
		} else {
			// get all not user defined areas
			List<IPointsArea> notUser = filterUserCreatedAreas(existing);
			if (notUser.size() == 0) {
				// create new point areas if only user defined exist
				createNewPointAreas(areas, clazz);
				return;
			}
			// get all not user defined and not closed areas and
			// have not the same valid from date
			List<IPointsArea> notUserClosed = filterNotClosedAreas(notUser);
			List<IPointsArea> notMatching =
				filterNotMatchingValidFromAreas(notUserClosed, getDateForString(areas.validfrom));
			if (notMatching.size() > 0) {
				// close the existing areas and create new ones
				closePointAreas(notMatching, getDateForString(areas.validfrom));
				createNewPointAreas(areas, clazz);
				return;
			}
			
			// check if date of existing not user and not closed matches,
			// if so just update with new areas
			List<IPointsArea> notUserNotClosed = filterClosedAreas(notUser);
			if (notUserNotClosed.get(0).getValidFromDate()
				.equals(getDateForString(areas.validfrom))) {
				createNewPointAreas(areas, clazz);
				return;
			}
			
			// if date does not match, set toDate of existing areas
			// to valid date of new areas and create new areas
			if (notUserNotClosed.size() > 0) {
				closePointAreas(notUserNotClosed, getDateForString(areas.validfrom));
				createNewPointAreas(areas, clazz);
				return;
			}
			
			// if date matches do not create new areas
		}
	}
	
	private static void closePointAreas(List<IPointsArea> notMatching, Date date){
		for (IPointsArea area : notMatching) {
			area.setValidToDate(date);
		}
	}
	
	private static void createNewPointAreas(PointAreas areas, Class<? extends KassenLeistung> clazz){
		for (PointArea area : areas.pointarea) {
			PointsAreaFactory.getInstance().getPointsAreaForString(area.toString(),
				getDateRangeForString(areas.validfrom), false, clazz);
		}
	}
	
	private static List<IPointsArea> filterUserCreatedAreas(List<IPointsArea> areas){
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		for (IPointsArea area : areas) {
			if (!area.isUserDefined())
				ret.add(area);
		}
		return ret;
	}
	
	private static List<IPointsArea> filterNotClosedAreas(List<IPointsArea> areas){
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		for (IPointsArea area : areas) {
			if (area.getValidToDate() != null)
				ret.add(area);
		}
		return ret;
	}
	
	private static List<IPointsArea> filterClosedAreas(List<IPointsArea> areas){
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		for (IPointsArea area : areas) {
			if (area.getValidToDate() == null)
				ret.add(area);
		}
		return ret;
	}
	
	private static List<IPointsArea> filterNotMatchingValidFromAreas(List<IPointsArea> areas,
		Date validFrom){
		ArrayList<IPointsArea> ret = new ArrayList<IPointsArea>();
		for (IPointsArea area : areas) {
			if (area.getValidFromDate().equals(validFrom))
				ret.add(area);
		}
		return ret;
	}
	
	private static DateRange getDateRangeForString(String from){
		return new DateRange(getDateForString(from));
	}
	
	private static Date getDateForString(String dateString){
		if (dateString != null) {
			try {
				return dateformat.parse(dateString);
			} catch (ParseException pe) {
				// if the date is not correct return default now date
			}
		}
		return new Date();
	}
}
