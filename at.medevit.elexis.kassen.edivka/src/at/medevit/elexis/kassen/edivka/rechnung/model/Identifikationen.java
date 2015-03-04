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

import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="Identifikation" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="Typ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Bereich" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Wert" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "identifikation"
})
@XmlRootElement(name = "Identifikationen")
public class Identifikationen {

    @XmlElement(name = "Identifikation", required = true)
    protected List<Identifikationen.Identifikation> identifikation;

    /**
     * Gets the value of the identifikation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifikation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifikation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Identifikationen.Identifikation }
     * 
     * 
     */
    public List<Identifikationen.Identifikation> getIdentifikation() {
        if (identifikation == null) {
            identifikation = new ArrayList<Identifikationen.Identifikation>();
        }
        return this.identifikation;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="Typ" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Bereich" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Wert" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class Identifikation {

        @XmlElement(name = "Typ", required = true)
        protected String typ;
        @XmlElement(name = "Bereich")
        protected String bereich;
        @XmlElement(name = "Wert", required = true)
        protected String wert;

        /**
         * Gets the value of the typ property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTyp() {
            return typ;
        }

        /**
         * Sets the value of the typ property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTyp(String value) {
            this.typ = value;
        }

        /**
         * Gets the value of the bereich property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBereich() {
            return bereich;
        }

        /**
         * Sets the value of the bereich property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBereich(String value) {
            this.bereich = value;
        }

        /**
         * Gets the value of the wert property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWert() {
            return wert;
        }

        /**
         * Sets the value of the wert property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWert(String value) {
            this.wert = value;
        }

    }

}
