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
 *         &lt;element name="KommElement" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="KommunikationsMittel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="WoErreichbar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AdresseNummer" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "kommElement"
})
@XmlRootElement(name = "Kommunikation")
public class Kommunikation {

    @XmlElement(name = "KommElement", required = true)
    protected List<Kommunikation.KommElement> kommElement;

    /**
     * Gets the value of the kommElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kommElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKommElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Kommunikation.KommElement }
     * 
     * 
     */
    public List<Kommunikation.KommElement> getKommElement() {
        if (kommElement == null) {
            kommElement = new ArrayList<Kommunikation.KommElement>();
        }
        return this.kommElement;
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
     *         &lt;element name="KommunikationsMittel" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="WoErreichbar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AdresseNummer" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    public static class KommElement {

        @XmlElement(name = "KommunikationsMittel", required = true)
        protected String kommunikationsMittel;
        @XmlElement(name = "WoErreichbar")
        protected String woErreichbar;
        @XmlElement(name = "AdresseNummer", required = true)
        protected String adresseNummer;

        /**
         * Gets the value of the kommunikationsMittel property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKommunikationsMittel() {
            return kommunikationsMittel;
        }

        /**
         * Sets the value of the kommunikationsMittel property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKommunikationsMittel(String value) {
            this.kommunikationsMittel = value;
        }

        /**
         * Gets the value of the woErreichbar property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWoErreichbar() {
            return woErreichbar;
        }

        /**
         * Sets the value of the woErreichbar property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWoErreichbar(String value) {
            this.woErreichbar = value;
        }

        /**
         * Gets the value of the adresseNummer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdresseNummer() {
            return adresseNummer;
        }

        /**
         * Sets the value of the adresseNummer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdresseNummer(String value) {
            this.adresseNummer = value;
        }

    }

}
