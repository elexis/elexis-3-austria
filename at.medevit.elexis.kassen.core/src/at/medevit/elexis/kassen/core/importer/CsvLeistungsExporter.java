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

import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;

public class CsvLeistungsExporter {

	private static Method[] methods;
	@SuppressWarnings("rawtypes")
	private static Class clazz;
	
	public static void writeLeistungenAsCsvToStream(List<KassenLeistung> leistungen, OutputStreamWriter writer) {
		ArrayList<LeistungBean> beans = new ArrayList<LeistungBean>();
		for(KassenLeistung leistung : leistungen) {
			beans.add(leistung.getBeanForLeistung());
		}
		internalWriteLeistungenAsCsvToStream(beans, writer);
	}
	
	protected static void  internalWriteLeistungenAsCsvToStream(List<LeistungBean> beans, OutputStreamWriter writer) {
		try {
			// init reflection
			buildMethods(beans.get(0), CsvLeistungsImporter.configHeader);
			// write the header line
			writer.write(getCsvHeaderString(CsvLeistungsImporter.configHeader));
			// do the actual work
			for(LeistungBean bean : beans) {
				writer.write(getCsvStringViaReflection(bean));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	private static String getCsvHeaderString(String names) {
		String[] methnames = names.split(",");

		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < methnames.length; i++) {
			if(methnames.length > (i + 1))
				sb.append("\"" + methnames[i] + "\";");
			else
				sb.append("\"" + methnames[i] + "\"");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	@SuppressWarnings({"unchecked" })
	private static void buildMethods(Object bean, String names) throws SecurityException, NoSuchMethodException {
		clazz = bean.getClass();
		String[] methnames = names.split(",");
		methods = new Method[methnames.length];
		for(int i = 0; i < methnames.length; i++) {
			String methName = "get" + methnames[i];
			methods[i] = clazz.getMethod(methName);
		}		
	}
	
	private static String getCsvStringViaReflection(Object bean) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < methods.length; i++) {
			Method getter = methods[i];
			String value = (String) getter.invoke(bean, new Object[0]);
			if(methods.length > (i + 1))
				sb.append("\"" + value + "\";");
			else
				sb.append("\"" + value + "\"");
		}
		sb.append("\n");
		return sb.toString();
	}
}
