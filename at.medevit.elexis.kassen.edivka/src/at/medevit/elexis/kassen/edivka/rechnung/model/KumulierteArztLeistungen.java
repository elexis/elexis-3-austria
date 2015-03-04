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
 *         &lt;element ref="{}PositionsNummer" minOccurs="0"/>
 *         &lt;element ref="{}SummeArztLeistungenBrutto" minOccurs="0"/>
 *         &lt;element ref="{}SummeArztLeistungenNetto" minOccurs="0"/>
 *         &lt;element ref="{}SummeArztLeistungenUmsatzsteuer" minOccurs="0"/>
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
    "positionsNummer",
    "summeArztLeistungenBrutto",
    "summeArztLeistungenNetto",
    "summeArztLeistungenUmsatzsteuer"
})
@XmlRootElement(name = "KumulierteArztLeistungen")
public class KumulierteArztLeistungen {

    @XmlElement(name = "PositionsNummer")
    protected String positionsNummer;
    @XmlElement(name = "SummeArztLeistungenBrutto")
    protected SummeArztLeistungenBrutto summeArztLeistungenBrutto;
    @XmlElement(name = "SummeArztLeistungenNetto")
    protected SummeArztLeistungenNetto summeArztLeistungenNetto;
    @XmlElement(name = "SummeArztLeistungenUmsatzsteuer")
    protected SummeArztLeistungenUmsatzsteuer summeArztLeistungenUmsatzsteuer;

    /**
     * Gets the value of the positionsNummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPositionsNummer() {
        return positionsNummer;
    }

    /**
     * Sets the value of the positionsNummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPositionsNummer(String value) {
        this.positionsNummer = value;
    }

    /**
     * Gets the value of the summeArztLeistungenBrutto property.
     * 
     * @return
     *     possible object is
     *     {@link SummeArztLeistungenBrutto }
     *     
     */
    public SummeArztLeistungenBrutto getSummeArztLeistungenBrutto() {
        return summeArztLeistungenBrutto;
    }

    /**
     * Sets the value of the summeArztLeistungenBrutto property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummeArztLeistungenBrutto }
     *     
     */
    public void setSummeArztLeistungenBrutto(SummeArztLeistungenBrutto value) {
        this.summeArztLeistungenBrutto = value;
    }

    /**
     * Gets the value of the summeArztLeistungenNetto property.
     * 
     * @return
     *     possible object is
     *     {@link SummeArztLeistungenNetto }
     *     
     */
    public SummeArztLeistungenNetto getSummeArztLeistungenNetto() {
        return summeArztLeistungenNetto;
    }

    /**
     * Sets the value of the summeArztLeistungenNetto property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummeArztLeistungenNetto }
     *     
     */
    public void setSummeArztLeistungenNetto(SummeArztLeistungenNetto value) {
        this.summeArztLeistungenNetto = value;
    }

    /**
     * Gets the value of the summeArztLeistungenUmsatzsteuer property.
     * 
     * @return
     *     possible object is
     *     {@link SummeArztLeistungenUmsatzsteuer }
     *     
     */
    public SummeArztLeistungenUmsatzsteuer getSummeArztLeistungenUmsatzsteuer() {
        return summeArztLeistungenUmsatzsteuer;
    }

    /**
     * Sets the value of the summeArztLeistungenUmsatzsteuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummeArztLeistungenUmsatzsteuer }
     *     
     */
    public void setSummeArztLeistungenUmsatzsteuer(SummeArztLeistungenUmsatzsteuer value) {
        this.summeArztLeistungenUmsatzsteuer = value;
    }

}
