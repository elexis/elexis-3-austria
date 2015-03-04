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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.19 at 11:02:34 AM MESZ 
//


package at.medevit.elexis.kassen.edivka.rechnung.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}RollenArt"/>
 *         &lt;element ref="{}BetriebsStelleKey"/>
 *         &lt;element ref="{}GesamterName"/>
 *         &lt;element ref="{}Adresse" minOccurs="0"/>
 *         &lt;element ref="{}Kommunikation" minOccurs="0"/>
 *         &lt;element ref="{}UIDNummer" minOccurs="0"/>
 *         &lt;element ref="{}DVRNummer" minOccurs="0"/>
 *         &lt;element ref="{}FirmenbuchNummer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rollenArt",
    "betriebsStelleKey",
    "gesamterName",
    "adresse",
    "kommunikation",
    "uidNummer",
    "dvrNummer",
    "firmenbuchNummer"
})
@XmlRootElement(name = "DokumentEmpfaenger")
public class DokumentEmpfaenger {

    @XmlElement(name = "RollenArt", required = true)
    protected RollenArt rollenArt;
    @XmlElement(name = "BetriebsStelleKey", required = true)
    protected String betriebsStelleKey;
    @XmlElement(name = "GesamterName", required = true)
    protected String gesamterName;
    @XmlElement(name = "Adresse")
    protected TpcAdresse adresse;
    @XmlElement(name = "Kommunikation")
    protected Kommunikation kommunikation;
    @XmlElement(name = "UIDNummer")
    protected String uidNummer;
    @XmlElement(name = "DVRNummer")
    protected String dvrNummer;
    @XmlElement(name = "FirmenbuchNummer")
    protected String firmenbuchNummer;

    /**
     * Gets the value of the rollenArt property.
     * 
     * @return
     *     possible object is
     *     {@link RollenArt }
     *     
     */
    public RollenArt getRollenArt() {
        return rollenArt;
    }

    /**
     * Sets the value of the rollenArt property.
     * 
     * @param value
     *     allowed object is
     *     {@link RollenArt }
     *     
     */
    public void setRollenArt(RollenArt value) {
        this.rollenArt = value;
    }

    /**
     * Gets the value of the betriebsStelleKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBetriebsStelleKey() {
        return betriebsStelleKey;
    }

    /**
     * Sets the value of the betriebsStelleKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBetriebsStelleKey(String value) {
        this.betriebsStelleKey = value;
    }

    /**
     * Gets the value of the gesamterName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGesamterName() {
        return gesamterName;
    }

    /**
     * Sets the value of the gesamterName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGesamterName(String value) {
        this.gesamterName = value;
    }

    /**
     * Gets the value of the adresse property.
     * 
     * @return
     *     possible object is
     *     {@link TpcAdresse }
     *     
     */
    public TpcAdresse getAdresse() {
        return adresse;
    }

    /**
     * Sets the value of the adresse property.
     * 
     * @param value
     *     allowed object is
     *     {@link TpcAdresse }
     *     
     */
    public void setAdresse(TpcAdresse value) {
        this.adresse = value;
    }

    /**
     * Gets the value of the kommunikation property.
     * 
     * @return
     *     possible object is
     *     {@link Kommunikation }
     *     
     */
    public Kommunikation getKommunikation() {
        return kommunikation;
    }

    /**
     * Sets the value of the kommunikation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Kommunikation }
     *     
     */
    public void setKommunikation(Kommunikation value) {
        this.kommunikation = value;
    }

    /**
     * Gets the value of the uidNummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUIDNummer() {
        return uidNummer;
    }

    /**
     * Sets the value of the uidNummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUIDNummer(String value) {
        this.uidNummer = value;
    }

    /**
     * Gets the value of the dvrNummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDVRNummer() {
        return dvrNummer;
    }

    /**
     * Sets the value of the dvrNummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDVRNummer(String value) {
        this.dvrNummer = value;
    }

    /**
     * Gets the value of the firmenbuchNummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirmenbuchNummer() {
        return firmenbuchNummer;
    }

    /**
     * Sets the value of the firmenbuchNummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirmenbuchNummer(String value) {
        this.firmenbuchNummer = value;
    }

}
