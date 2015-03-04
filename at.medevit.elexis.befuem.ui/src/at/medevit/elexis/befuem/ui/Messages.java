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
package at.medevit.elexis.befuem.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "at.medevit.elexis.befuem.ui.messages"; //$NON-NLS-1$
	public static String BefuemView_AddressbookTabText;
	public static String BefuemView_ArchivTabText;
	public static String BefuemView_FilterLabelText;
	public static String BefuemView_FindingDateColumnText;
	public static String BefuemView_FindingPatientColumnText;
	public static String BefuemView_FindingReceiverColumnText;
	public static String BefuemView_FindingSenderColumnText;
	public static String BefuemView_FindingSizeColumnText;
	public static String BefuemView_FindingTypColumnText;
	public static String BefuemView_InboxTabText;
	public static String BefuemView_ParticipantCityColumnText;
	public static String BefuemView_ParticipantContactColumnText;
	public static String BefuemView_ParticipantEMailColumnText;
	public static String BefuemView_ParticipantFirstnameColumnText;
	public static String BefuemView_ParticipantHVNbColumnText;
	public static String BefuemView_ParticipantLastnameColumnText;
	public static String ClientFindingImportDialog_FindingsNotResolvedError;
	public static String ClientFindingImportDialog_LabResultNonUniqueWarning;
	public static String ClientFindingImportDialog_Message;
	public static String ClientFindingImportDialog_NoPatientSelectedError;
	public static String ClientFindingImportDialog_NoSenderSelectedError;
	public static String ClientFindingImportDialog_NotSelectedErrorDialogDescription;
	public static String ClientFindingImportDialog_NotSelectedErrorDialogTitle;
	public static String ClientFindingImportDialog_ResolveActionAutoCreate;
	public static String ClientFindingImportDialog_ResolveActionCreate;
	public static String ClientFindingImportDialog_ResolveActionEdit;
	public static String ClientFindingImportDialog_SelectPatientButton;
	public static String ClientFindingImportDialog_SelectPatientDescription;
	public static String ClientFindingImportDialog_SelectPatientTitle;
	public static String ClientFindingImportDialog_SelectSenderButton;
	public static String ClientFindingImportDialog_SelectSenderDescription;
	public static String ClientFindingImportDialog_SelectSenderTitle;
	public static String ClientFindingImportDialog_Title;
	public static String FindingPatientLabelProvider_UnknownPatient;
	public static String FindingTypLabelProvider_TypLab;
	public static String FindingTypLabelProvider_TypText;
	public static String FindingTypLabelProvider_TypReq;
	public static String FindingTypLabelProvider_TypUnknown;
	public static String LabItemMapDialog_LabItemDescription;
	public static String LabItemMapDialog_LabItemDescriptionColumnText;
	public static String LabItemMapDialog_LabItemParameters;
	public static String LabItemMapDialog_LabItemRefM;
	public static String LabItemMapDialog_LabItemRefMColumnText;
	public static String LabItemMapDialog_LabItemRefW;
	public static String LabItemMapDialog_LabItemRefWColumnText;
	public static String LabItemMapDialog_LabItemShortDescription;
	public static String LabItemMapDialog_LabItemShortDescriptionColumnText;
	public static String LabItemMapDialog_LabItemUnit;
	public static String LabItemMapDialog_LabItemUnitColumnText;
	public static String LabItemMapDialog_Message;
	public static String LabItemMapDialog_NonUniqueLabResultError;
	public static String LabItemMapDialog_NotResolvedError;
	public static String LabItemMapDialog_Title;
	public static String SetParticipantKontakt_SelectorDescription;
	public static String SetParticipantKontakt_SelectorTitle;
	public static String SyncClient_SyncJobFailure;
	public static String SyncClient_SyncJobClientFailure;
	public static String SyncClient_SyncJobTitle;
	public static String OpenClientImport_DocumentFailedMessage;
	public static String OpenClientImport_DocumentPDFFailedMessage;
	public static String OpenClientImport_FailedMessage;
	public static String OpenClientImport_UnknownFindingTyp;
	public static String NetClientPreferencePage_Network;
	public static String NetParticiantImporter_Description;
	public static String NetParticiantImporter_ErrorFileNotFound;
	public static String NetParticiantImporter_ErrorIO;
	public static String NetParticiantImporter_ErrorNoFilename;
	public static String NetParticiantImporter_ErrorUnsupportedEncoding;
	public static String NetParticiantImporter_ImportJobTitle;
	public static String NetParticiantImporter_Title;
	public static String UpdateNetClientFindingsJob_UpdateJobTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
