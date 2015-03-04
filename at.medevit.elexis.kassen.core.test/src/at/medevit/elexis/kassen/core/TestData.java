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
package at.medevit.elexis.kassen.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TestData {
	private static String pointareas =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<pointareas validfrom=\"01.07.2009\">\n" + 
		"	<pointarea>" +
		"		<area value=\"positioningroup(1.1-1.2) or positioningroup(1.4-1.5)\"/>" +
		"		<multiplier value=\"0,8768\"/>" +
		"	</pointarea>" +
		"	<pointarea>" +
		"		<area value=\"positioningroup(1.7-1.10)\"/>" + 
		"		<multiplier value=\"0,9999\"/>" +
		"	</pointarea>" +
		"</pointareas>";

	public static InputStream getPointAreas() {
		try {
			return new ByteArrayInputStream(pointareas.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
