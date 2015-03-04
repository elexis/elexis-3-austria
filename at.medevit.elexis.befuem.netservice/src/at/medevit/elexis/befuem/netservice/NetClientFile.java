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
package at.medevit.elexis.befuem.netservice;

import java.io.File;
import java.util.Date;

public class NetClientFile {
	private File file;
	private Date reception;
	
	public NetClientFile(File file, Date reception) {
		this.file = file;
		this.reception = reception;
	}
	
	public File getFile() {
		return file;
	}
	
	public Date getReceptionDate() {
		return reception;
	}
	
//	public String getReceptionString(String format) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
//		
//		return dateFormat.format(reception);
//	}
}
