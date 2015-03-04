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
package at.medevit.elexis.befuem.contextservice.header;

import java.io.IOException;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Format;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

abstract class AbstractHeaderParser implements IHeaderParser {
	
	@Override
	public abstract Format isFileFormat(NetClientFinding finding) throws IOException;

	@Override
	public abstract void fillClientFindingHeader(NetClientFinding finding) throws IOException;

}
