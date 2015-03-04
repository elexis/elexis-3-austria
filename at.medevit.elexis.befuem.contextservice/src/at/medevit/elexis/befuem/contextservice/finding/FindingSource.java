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
package at.medevit.elexis.befuem.contextservice.finding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FindingSource {
	private static final int HEADER_SAMPLE_SIZE = 256;
	
	// umlaut heuristic to detect the char encoding
	// ä, ö, ü, Ä, Ö, Ü
	private final String ENC_cp852 = "Cp852";
	private byte[] umlaut852 = {(byte) 0x84, (byte) 0x94, (byte) 0x81,
			(byte) 0x8E, (byte) 0x99, (byte) 0x9A};
	private final String ENC_iso8859_1 = "ISO-8859-1";
	private byte[] umlaut8859_1 = {(byte) 0xE4, (byte) 0xF6, (byte) 0xFC,
			(byte) 0xC4, (byte) 0xD6, (byte) 0xDC};
	
	private File fileSource;
	private byte[] memSource;
	private String id;
	private String encoding;
	private int size;
	private byte[] header;
	
	public FindingSource(File source) {
		fileSource = source;
		size = (int) source.length();
		id = source.getAbsolutePath();
	}
	
	public FindingSource(byte[] source, String Id) {
		memSource = source;
		size = source.length;
		id = Id;
	}
	
	/**
	 * Get the character encoding that should be used for this source
	 * 
	 * @return
	 */
	public String getEncoding() {
		if(encoding == null) {
			if(header != null) {
				encoding = guessContentCharEncoding(header);
			} else {
				try {
					getHeaderAsByteArray();
				} catch (IOException e) {
					// we cant do anything here if the file does not exist ...
					// just return a default encoding
					return ENC_iso8859_1;
				}
				encoding = guessContentCharEncoding(header);
			}
		}
		return encoding;
	}
	
	public InputStream getContentAsStream() throws IOException {
		if(fileSource != null) {
			return new FileInputStream(fileSource);
		} else if(memSource != null) {
			return new ByteArrayInputStream(memSource);
		} else {
			return null;
		}
	}
	
	public byte[] getContentAsByteArray() throws IOException {
		if (fileSource != null) {
			byte[] content = new byte[size];
			byte[] buffer = new byte[2048];
			FileInputStream fis = new FileInputStream(fileSource);
			ByteArrayOutputStream bos = new ByteArrayOutputStream((int) size);
			int length = 0;
			while ((length = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
			bos.flush();
			content = bos.toByteArray();
			fis.close();
			return content;
		} else if(memSource != null) {
			return memSource;
		} else {
			return null;
		}
	}
	
	/**
	 * Get the first 256 bytes of the source as an InputStream
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getHeaderAsStream() throws IOException {
		
		if (fileSource != null) {
			header = readFileHeader();
			return new ByteArrayInputStream(header);
		} else if(memSource != null) {
			if(header == null)
				header = new byte[HEADER_SAMPLE_SIZE];
			System.arraycopy(memSource, 0, header, 0, HEADER_SAMPLE_SIZE);
			return new ByteArrayInputStream(header);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the first 256 bytes of the source as a byte array
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] getHeaderAsByteArray() throws IOException {
		if (fileSource != null) {
			header = readFileHeader();
			return header;
		} else if(memSource != null) {
			if(header == null)
				header = new byte[HEADER_SAMPLE_SIZE];
			System.arraycopy(memSource, 0, header, 0, HEADER_SAMPLE_SIZE);
			return header;
		} else {
			return null;
		}
	}
	
	public String getId() {
		return id;
	}
	
	public int getSize() {
		return size;
	}
	
	public byte[] getMD5MessageDigest() {
		// compute and fill in the message digest
        MessageDigest md = null;
        try {
			md = MessageDigest.getInstance("MD5");
			
			InputStream is = getContentAsStream();
			byte[] buffer = new byte[1024];
			int numRead;
			do {
				numRead = is.read(buffer);
				if (numRead > 0) {
					md.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			is.close();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(md != null)
			return md.digest();
		else
			return null;
	}
	
	private String guessContentCharEncoding(byte[] chars) {
		int umlaut852 = 0;
		int umlaut8859_1 = 0;
		
//		System.out.println("FINDING: " + id);
		for(int i = 0; i < chars.length; i++) {
			// if this is not a simpl printable char
			// look in the heuristic arrays
			if((chars[i] < 0x20 || chars[i] > 0x7E) &&
					chars[i] != 0x0A && chars[i] != 0x0D) {
				if(is852Umlaut(chars[i]))
					umlaut852++;
				else if(is8859_1Umlaut(chars[i]))
					umlaut8859_1++;
			}
		}
//		System.out.println("FINDING: ENC 852 " + umlaut852 + " 8859-1 " + umlaut8859_1);
		// currently ISO is default ...
		if(umlaut852 > 0 && umlaut852 > umlaut8859_1)
			return ENC_cp852;
		else
			return ENC_iso8859_1;
	}
	
	private boolean is852Umlaut(byte ch) {
		for(byte umlaut : umlaut852) {
			if(ch == umlaut)
				return true;
		}
		return false;
	}
	
	private boolean is8859_1Umlaut(byte ch) {
		for(byte umlaut : umlaut8859_1) {
			if(ch == umlaut)
				return true;
		}
		return false;
	}
	
	private byte[] readFileHeader() throws IOException {
		byte[] headerBytes = null;
		if (fileSource != null) {
			FileInputStream fis = new FileInputStream(fileSource);
			headerBytes = new byte[HEADER_SAMPLE_SIZE]; 
			fis.read(headerBytes);
			fis.close();
		}
		return headerBytes;
	}
}
