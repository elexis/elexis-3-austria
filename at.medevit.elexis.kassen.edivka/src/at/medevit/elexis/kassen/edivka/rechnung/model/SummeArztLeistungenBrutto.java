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

import java.math.BigDecimal;

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
 *         &lt;element ref="{}SummeLeistungenArztBrutto"/>
 *         &lt;element ref="{}UmsatzsteuerSL" minOccurs="0"/>
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
    "summeLeistungenArztBrutto",
    "umsatzsteuerSL"
})
@XmlRootElement(name = "SummeArztLeistungenBrutto")
public class SummeArztLeistungenBrutto {

    @XmlElement(name = "SummeLeistungenArztBrutto", required = true)
    protected BigDecimal summeLeistungenArztBrutto;
    @XmlElement(name = "UmsatzsteuerSL")
    protected String umsatzsteuerSL;

    /**
     * Gets the value of the summeLeistungenArztBrutto property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSummeLeistungenArztBrutto() {
        return summeLeistungenArztBrutto;
    }

    /**
     * Sets the value of the summeLeistungenArztBrutto property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSummeLeistungenArztBrutto(BigDecimal value) {
        this.summeLeistungenArztBrutto = value;
    }

    /**
     * Gets the value of the umsatzsteuerSL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUmsatzsteuerSL() {
        return umsatzsteuerSL;
    }

    /**
     * Sets the value of the umsatzsteuerSL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUmsatzsteuerSL(String value) {
        this.umsatzsteuerSL = value;
    }

}
