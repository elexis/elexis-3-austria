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
package at.medevit.elexis.befuem.ui.model;

import java.util.List;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public class OutboxFindingsModelProvider {
	private static OutboxFindingsModelProvider content;
	private List<NetClientFinding> findings;
	
	private OutboxFindingsModelProvider() {
		updateModel();
	}
	
	public static synchronized OutboxFindingsModelProvider getInstance() {
		if (content != null ) {
			return content;
		}
			content = new OutboxFindingsModelProvider();
			return content;
	}
	
	public List<NetClientFinding> getOutboxFindings() {
		updateModel();
		return findings;
	}
	
	public void updateModel() {
//		INetClient client = NetServiceConsumer.getConfiguredClientImplementation();
//		if(findings == null)
//		{
//			findings = new ArrayList<NetClientFinding>();
//			List<File> files = client.getFindings(Location.OUTBOX);
//			for(File file : files) {
//				findings.add(new NetClientFinding(new FindingSource(file), Location.OUTBOX));
//			}
//			ContextServiceConsumer.fillClientFindingHeader(findings);
//		} else {
//			findings.clear();
//			List<File> files = client.getFindings(Location.OUTBOX);
//			for(File file : files) {
//				findings.add(new NetClientFinding(new FindingSource(file), Location.OUTBOX));
//			}
//			ContextServiceConsumer.fillClientFindingHeader(findings);
//		}
	}
}
