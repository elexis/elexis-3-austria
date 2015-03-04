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

import java.util.ArrayList;

public class NetClientPreferenceDescritpion {
	
	public enum PreferenceFieldType { STRING, FILE }
	
	private ArrayList<KeyValue> preferences = new ArrayList<KeyValue>();
	
	public void addDescription(String prefFieldConstant, String labelText, PreferenceFieldType fieldType) {
			
		preferences.add(new KeyValue(prefFieldConstant, labelText, fieldType));
	}

	public int size() {
		return preferences.size();
	}
	
	public String getFieldConstantAt(int index) {
		return preferences.get(index).getKey();
	}
	
	public PreferenceFieldType getFieldTypeAt(int index) {
		return preferences.get(index).getType();
	}
	
	public String getLabelTextAt(int index) {
		return preferences.get(index).getText();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for(KeyValue kv : preferences) {
			buf.append("Const: " + kv.getKey() + " text: " + kv.getText() + " type: " + kv.getType() + " ");
		}
		
		return buf.toString();
	}

	private class KeyValue {
		private String key;
		private String text;
		private PreferenceFieldType type;
		
		public KeyValue(String prefFieldConstant, String labelText, PreferenceFieldType fieldType) {
			key = prefFieldConstant;
			type = fieldType;
			text = labelText;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getText() {
			return text;
		}
		
		public PreferenceFieldType getType() {
			return type;
		}
	}
}
