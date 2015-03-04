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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.csv.CSVBinding;
import org.milyn.csv.CSVBindingType;
import org.milyn.csv.CSVReaderConfigurator;
import org.milyn.payload.JavaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.medevit.elexis.kassen.core.model.LeistungBean;


public class CsvLeistungsImporter {
	
	private static Logger log = LoggerFactory.getLogger(CsvLeistungsImporter.class);
	
	protected static final String configHeader = "GruppeId,PositionGruppenId,PositionId,PositionNeuId,ValidFromDate,ValidToDate,PositionTitle,PositionHinweis,PositionAusFach,PositionFachgebiete,PositionPunktWert,PositionGeldWert,PositionZusatz,PositionLogik";
	
	/**
	 * Read the Leistungen from a InputStreamReader containing CSV data.
	 * 
	 * @param stream
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public static List<LeistungBean> getLeistungenFromCsvStream(InputStreamReader stream) {
		JavaResult result = null;
        // Instantiate Smooks with the config ...
    	Smooks smooks = null;
		try {
			smooks = new Smooks();
			smooks.setClassLoader(LeistungBean.class.getClassLoader());

			// done with header parsing reset smooks for content
			smooks = new Smooks();
			CSVReaderConfigurator contentConf = new CSVReaderConfigurator(configHeader);
			contentConf.setSeparatorChar(';');
			contentConf.setBinding(new CSVBinding("leistungen", LeistungBean.class, CSVBindingType.LIST));
			contentConf.setStrict(false);
			contentConf.setSkipLineCount(1);
			smooks.setReaderConfig(contentConf);

			// Create an exec context - no profiles ...
			ExecutionContext executionContext = smooks.createExecutionContext();
			
			result = new JavaResult();			
			
			smooks.filterSource(executionContext, new StreamSource(stream), result);
		} catch (SmooksException e) {
			log.error("Error reading stream ", e);
			throw new RuntimeException(e);
		} finally {
			if (smooks != null)
				smooks.close();
		}
        
        return (List<LeistungBean>) result.getBean("leistungen");
	}
	
	/**
	 * Read the Leistungen from a UTF-8 encoded file containing CSV.
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws RuntimeException
	 */
	public static List<LeistungBean> getLeistungenFromCsvFile(String filename) throws FileNotFoundException {
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			return getLeistungenFromCsvStream(isr);
		} catch (UnsupportedEncodingException e) {
			log.error("Error reading stream from file ", e);
			throw new RuntimeException(e);
		}
	}
}
