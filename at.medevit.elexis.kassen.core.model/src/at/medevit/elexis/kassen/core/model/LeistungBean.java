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
package at.medevit.elexis.kassen.core.model;

public class LeistungBean {
	private String gruppeId;
	private String positionGruppenId;
	private String positionId;
	private String positionneuId;
	private String validFromDate;
	private String validToDate;
	private String positionTitle;
	private String positionHinweis;
	private String positionAusFach;
	private String positionFachgebiete;
	private String positionPunktWert;
	private String positionGeldWert;
	private String positionZusatz;
	private String positionLogik;
	
	public String getGruppeId() {
		return gruppeId;
	}
	public void setGruppeId(String gruppeId) {
		this.gruppeId = gruppeId.trim();
	}
	public String getPositionGruppenId() {
		return positionGruppenId;
	}
	public void setPositionGruppenId(String positionGruppenId) {
		this.positionGruppenId = positionGruppenId.trim();
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId.trim();
	}
	public String getPositionNeuId() {
		return positionneuId;
	}
	public void setPositionNeuId(String positionneuId) {
		this.positionneuId = positionneuId.trim();
	}
	public String getValidFromDate() {
		return validFromDate != null ? validFromDate : "";
	}
	public void setValidFromDate(String validFromDate) {
		this.validFromDate = validFromDate.trim();
	}
	public String getValidToDate() {
		return validToDate != null ? validToDate : "";
	}
	public void setValidToDate(String validToDate) {
		this.validToDate = validToDate.trim();
	}
	public String getPositionTitle() {
		return positionTitle;
	}
	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle.trim();
	}
	public String getPositionHinweis() {
		return positionHinweis;
	}
	public void setPositionHinweis(String positionHinweis) {
		this.positionHinweis = positionHinweis.trim();
	}
	public String getPositionAusFach() {
		if(positionAusFach.equalsIgnoreCase("true"))
			return "1";
		if(positionAusFach.equalsIgnoreCase("false"))
			return "0";
		
		return positionAusFach;
	}
	public void setPositionAusFach(String positionAusFach) {
		if(positionAusFach.trim().equalsIgnoreCase("true")) {
			this.positionAusFach = "1";
			return;
		}
		if(positionAusFach.trim().equalsIgnoreCase("false")) {
			this.positionAusFach = "0";
			return;
		}
		this.positionAusFach = positionAusFach.trim();
	}
	public String getPositionFachgebiete() {
		return positionFachgebiete;
	}
	public void setPositionFachgebiete(String positionFachgebiete) {
		this.positionFachgebiete = positionFachgebiete.trim();
	}
	public String getPositionPunktWert() {
		return positionPunktWert;
	}
	public void setPositionPunktWert(String positionPunktWert) {
		this.positionPunktWert = positionPunktWert.trim();
	}
	public String getPositionGeldWert() {
		return positionGeldWert;
	}
	public void setPositionGeldWert(String positionGeldWert) {
		this.positionGeldWert = positionGeldWert.trim();
	}
	public String getPositionZusatz() {
		return positionZusatz;
	}
	public void setPositionZusatz(String positionZusatz) {
		this.positionZusatz = positionZusatz.trim();
	}
	public String getPositionLogik() {
		return positionLogik;
	}
	public void setPositionLogik(String positionLogik) {
		this.positionLogik = positionLogik.trim();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----- LEISTUNG -----\n");
		sb.append("gruppeId [" + gruppeId + "]\n");
		sb.append("positionGruppenId [" + positionGruppenId + "]\n");
		sb.append("positionId [" + positionId + "]\n");
		sb.append("positionneuId [" + positionneuId + "]\n");
		sb.append("validFromDate [" + validFromDate + "]\n");
		sb.append("validToDate [" + validToDate + "]\n");
		sb.append("positionTitle [" + positionTitle + "]\n");
		sb.append("positionHinweis [" + positionHinweis + "]\n");
		sb.append("positionAusFach [" + positionAusFach + "]\n");
		sb.append("positionFachgebiete [" + positionFachgebiete + "]\n");
		sb.append("positionPunktWert [" + positionPunktWert + "]\n");
		sb.append("positionGeldWert [" + positionGeldWert + "]\n");
		sb.append("positionZusatz [" + positionZusatz + "]\n");
		sb.append("positionLogik [" + positionLogik + "]\n");
		return sb.toString();
	}
}
