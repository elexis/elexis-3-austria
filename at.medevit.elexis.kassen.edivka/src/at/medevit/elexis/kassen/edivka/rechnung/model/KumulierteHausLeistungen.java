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
 *         &lt;element ref="{}SummeHausLeistungenBrutto" minOccurs="0"/>
 *         &lt;element ref="{}SummeHausLeistungenNetto" minOccurs="0"/>
 *         &lt;element ref="{}SummeHausLeistungenUmsatzsteuer" minOccurs="0"/>
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
    "summeHausLeistungenBrutto",
    "summeHausLeistungenNetto",
    "summeHausLeistungenUmsatzsteuer"
})
@XmlRootElement(name = "KumulierteHausLeistungen")
public class KumulierteHausLeistungen {

    @XmlElement(name = "PositionsNummer")
    protected String positionsNummer;
    @XmlElement(name = "SummeHausLeistungenBrutto")
    protected SummeHausLeistungenBrutto summeHausLeistungenBrutto;
    @XmlElement(name = "SummeHausLeistungenNetto")
    protected SummeHausLeistungenNetto summeHausLeistungenNetto;
    @XmlElement(name = "SummeHausLeistungenUmsatzsteuer")
    protected SummeHausLeistungenUmsatzsteuer summeHausLeistungenUmsatzsteuer;

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
     * Gets the value of the summeHausLeistungenBrutto property.
     * 
     * @return
     *     possible object is
     *     {@link SummeHausLeistungenBrutto }
     *     
     */
    public SummeHausLeistungenBrutto getSummeHausLeistungenBrutto() {
        return summeHausLeistungenBrutto;
    }

    /**
     * Sets the value of the summeHausLeistungenBrutto property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummeHausLeistungenBrutto }
     *     
     */
    public void setSummeHausLeistungenBrutto(SummeHausLeistungenBrutto value) {
        this.summeHausLeistungenBrutto = value;
    }

    /**
     * Gets the value of the summeHausLeistungenNetto property.
     * 
     * @return
     *     possible object is
     *     {@link SummeHausLeistungenNetto }
     *     
     */
    public SummeHausLeistungenNetto getSummeHausLeistungenNetto() {
        return summeHausLeistungenNetto;
    }

    /**
     * Sets the value of the summeHausLeistungenNetto property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummeHausLeistungenNetto }
     *     
     */
    public void setSummeHausLeistungenNetto(SummeHausLeistungenNetto value) {
        this.summeHausLeistungenNetto = value;
    }

    /**
     * Gets the value of the summeHausLeistungenUmsatzsteuer property.
     * 
     * @return
     *     possible object is
     *     {@link SummeHausLeistungenUmsatzsteuer }
     *     
     */
    public SummeHausLeistungenUmsatzsteuer getSummeHausLeistungenUmsatzsteuer() {
        return summeHausLeistungenUmsatzsteuer;
    }

    /**
     * Sets the value of the summeHausLeistungenUmsatzsteuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummeHausLeistungenUmsatzsteuer }
     *     
     */
    public void setSummeHausLeistungenUmsatzsteuer(SummeHausLeistungenUmsatzsteuer value) {
        this.summeHausLeistungenUmsatzsteuer = value;
    }

}