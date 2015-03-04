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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ConsoleClient implements INetClient {

	protected State currentState;
	
	protected String message;
	
	protected void runBlocking(String[] command) throws IOException {
		currentState = State.RUNNING;
		
		ProcessBuilder pb = new ProcessBuilder(command);
		pb = pb.redirectErrorStream(true);
		// change the directory of the process to the dir of the command we are RUNNING
		int dirIdx = command[0].lastIndexOf(File.separator);
		if(dirIdx != -1) {
			String dir = command[0].substring(0, dirIdx);
			pb = pb.directory(new File(dir));
		}
		Process proc;
		try {
			proc = pb.start();

			if(proc != null) {
				StringBuilder msg = new StringBuilder();
				
				InputStream is = proc.getInputStream();
			    InputStreamReader isr = new InputStreamReader(is);
			    BufferedReader br = new BufferedReader(isr);
			    String line;
			    while ((line = br.readLine()) != null) {
			      msg.append(line + "\n");
			    }
	
				message = msg.toString();
				try {
					if(proc.waitFor() != 0) {
						currentState = State.ERROR;
					} else {
						currentState = State.SUCCESS;
					}
				} catch (InterruptedException ie) {
					// set state to error and continue
					currentState = State.ERROR;
				}
			} else {
				message = "";
				currentState = State.ERROR;
			}
		} catch (IOException e) {
			currentState = State.ERROR;
			throw e;
		}
		
	}

	@Override
	public State getState() {
		return currentState;
	}

	@Override
	public String getStatusMessage() {
		return message;
	}
}
