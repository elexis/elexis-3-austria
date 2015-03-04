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
package at.medevit.elexis.befuem.netservice.gnv;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.medevit.elexis.befuem.netservice.ConsoleClient;
import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.NetClientFile;
import at.medevit.elexis.befuem.netservice.NetClientPreferenceDescritpion;

public class GNVClient extends ConsoleClient implements INetClient {

	private static INetClient instance;
	protected String progLocation;
	protected String inboxLocation;
	protected String outboxLocation;
	protected String archiveLocation;
	
	protected NetClientPreferenceDescritpion preferences = new NetClientPreferenceDescritpion();
	
	private GNVClient() {
		preferences.addDescription(AT_MEDEVIT_ELEXIS_BEFUEM_GNVCLIENT_PROGLOCATION, "Client location", NetClientPreferenceDescritpion.PreferenceFieldType.FILE);
	}
	
	public static INetClient getInstance() {
		if(instance == null)
			instance = new GNVClient();
		return instance;
	}
	
	@Override
	public String getNetName() {
		return "Gesundheitsnetz Vorarlberg";
	}

	@Override
	public String getNetShortName() {
		return "GNV";
	}

	@Override
	public List<NetClientFile> getFindings(Location loc) throws IOException {
		ArrayList<NetClientFile> findings = null;
		if(loc == Location.INBOX)
			findings = getFindingsFromInbox();
		else if(loc == Location.OUTBOX)
			findings = getFindingsFromOutbox();
		else if(loc == Location.ARCHIVE)
			findings = getFindingsFromArchive();
		
		if(findings != null)
			return findings;
		else
			return new ArrayList<NetClientFile>();
	}

	@Override
	public String getLastConnectionTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean synchronizeFindings() throws IOException {
		String[] command = new String[1];
		command[0] = progLocation;

		runBlocking(command);

		// The client is started via a batch file
		// this means the return code is useless for
		// determining successful operation 
		// -> look for "FEHLER: " in the message
		if(message.contains("FEHLER: ")) {
			currentState = State.ERROR;
		}
		
		return getState() != State.ERROR;
	}

	@Override
	public File archiveFinding(File finding) throws IOException {
		// TODO make sure we dont need to manipulate the finding on the filesystem ...
//		return moveFinding(finding, Location.ARCHIVE);
		return finding;
	}

	@Override
	public NetClientPreferenceDescritpion getPreferenceDescription() {
		return preferences;
	}
	
	@Override
	public void setPreferenceValue(String key, String value) {
		if(key.equalsIgnoreCase(AT_MEDEVIT_ELEXIS_BEFUEM_GNVCLIENT_PROGLOCATION)) {
			progLocation = value;
			if((progLocation != null) && (progLocation.length() > 0)) {
				String baseDir;
				
				int sepIdx = progLocation.lastIndexOf(File.separator);
				if(sepIdx != -1)
					baseDir = progLocation.substring(0, sepIdx + 1);
				else
					return;
				
				inboxLocation = baseDir + "archive" + File.separator + "inbox";
				outboxLocation = baseDir + "archive"  + File.separator + "outbox";
				archiveLocation = baseDir + "archive";
			}
		}
	}
	
	protected ArrayList<NetClientFile> getFindingsFromInbox() throws IOException {
		ArrayList<NetClientFile> findings = new ArrayList<NetClientFile>();
		if(inboxLocation != null) {
			File inbox = new File(inboxLocation);
			if(inbox.exists()) {
				addFindingsFromDirectoryRecursive(findings, inbox, Location.INBOX, new ArchiveFileFilter());
			}
		}
		return findings;
	}
	
	protected ArrayList<NetClientFile> getFindingsFromOutbox() throws IOException {
		ArrayList<NetClientFile> findings = new ArrayList<NetClientFile>();
		if(outboxLocation != null) {
			File outbox = new File(outboxLocation);
			if(outbox.exists()) {
				addFindingsFromDirectoryRecursive(findings, outbox, Location.OUTBOX, new ArchiveFileFilter());
			}
		}
		return findings;
	}
	
	/**
	 * The Archive is managed by the extern GCV client implementation.
	 * 
	 * @return
	 */
	protected ArrayList<NetClientFile> getFindingsFromArchive() {
		ArrayList<NetClientFile> findings = new ArrayList<NetClientFile>();
		// TODO make sure we dont want to remember this code ...
//		if(archiveLocation != null) {
//			File archive = new File(archiveLocation);
//			if(archive.exists()) {
//				File[] files = archive.listFiles(new ArchiveFileFilter());
//				for (File file : files) {
//					if(file.isDirectory()) {
//						if(file.getName().equalsIgnoreCase("inbox"))
//							addFindingsFromDirectoryRecursive(findings, file, Location.ARCHIVE_INBOX, new ArchiveFileFilter());
//						else if(file.getName().equalsIgnoreCase("outbox"))
//							addFindingsFromDirectoryRecursive(findings, file, Location.ARCHIVE_OUTBOX, new ArchiveFileFilter());
//						else
//							addFindingsFromDirectoryRecursive(findings, file, Location.ARCHIVE, new ArchiveFileFilter());
//					}
//					else {
//						findings.add(file);
//					}
//				}
//			}
//		}
		return findings;
	}
	
	private void addFindingsFromDirectoryRecursive (
			ArrayList<NetClientFile> findings, File dir, Location loc, FileFilter filter) throws IOException {
		File[] files;
		if(filter != null)
			files = dir.listFiles(filter);
		else
			files = dir.listFiles();
		
		if(files == null)
			throw new IOException(dir.getAbsolutePath() + " is not a directory, or an I/O error occured.");
		
		for (File file : files) {
			if(file.isDirectory()) {
				addFindingsFromDirectoryRecursive(findings, file, loc, filter);
			}
			else {
				// TODO for better information on reception date and the file over all, parser xml files in inbox ...
	
				// at this moment the reception date encoded in the directory name is good enough ...
				// path is expected to look like /archive/boxname/yyyy-mm-dd/filename.xxx
				if(file.getParentFile() != null) {
					String dateDir = file.getParentFile().getName();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						findings.add(new NetClientFile(file, dateFormat.parse(dateDir)));
					} catch (ParseException e) {
						// TODO should we log this ?
						findings.add(new NetClientFile(file, new Date()));
					}
				} else {
					findings.add(new NetClientFile(file, new Date()));
				}
			}
		}
	}

	protected File moveFinding(File finding, Location to) throws IOException {
		if(finding.exists()) {
			File dst = null;
			String absFilename = finding.getAbsolutePath();
			String filename = absFilename.substring(absFilename.lastIndexOf(File.separator) + 1, absFilename.length());
			if(to == Location.ARCHIVE)
				dst = new File(archiveLocation + File.separator + filename);
			else if(to == Location.INBOX)
				dst = new File(inboxLocation + File.separator + filename);
			else if(to == Location.OUTBOX)
				dst = new File(outboxLocation + File.separator + filename);
			else
				dst = new File("");
			// now move the file
			FileReader in = new FileReader(finding);
			FileWriter out = new FileWriter(dst);
			int c;

			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();
			finding.delete();

			return dst;
		}
		return null;		
	}
	
	private class ArchiveFileFilter implements FileFilter {
	    @Override
		public boolean accept(File f) {
	        if (f.isDirectory())
	        {
	            if(f.getName().equalsIgnoreCase("index"))
	        		return false;
	        }
	        return true;
	    }
	}
}
