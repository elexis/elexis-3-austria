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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;choice>
 *           &lt;element name="Stelle">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;extension base="{}tpcStelle">
 *                   &lt;attribute name="Eigenschaft" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="Prioritaet" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;/extension>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Person">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;extension base="{}tpcPerson">
 *                   &lt;attribute name="Eigenschaft" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="Prioritaet" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;/extension>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
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
    "stelleOrPerson"
})
@XmlRootElement(name = "Beziehungen")
public class Beziehungen {

    @XmlElements({
        @XmlElement(name = "Person", type = Beziehungen.Person.class),
        @XmlElement(name = "Stelle", type = Beziehungen.Stelle.class)
    })
    protected List<Object> stelleOrPerson;

    /**
     * Gets the value of the stelleOrPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stelleOrPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStelleOrPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Beziehungen.Person }
     * {@link Beziehungen.Stelle }
     * 
     * 
     */
    public List<Object> getStelleOrPerson() {
        if (stelleOrPerson == null) {
            stelleOrPerson = new ArrayList<Object>();
        }
        return this.stelleOrPerson;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}tpcPerson">
     *       &lt;attribute name="Eigenschaft" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="Prioritaet" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Person
        extends TpcPerson
    {

        @XmlAttribute(name = "Eigenschaft", required = true)
        protected String eigenschaft;
        @XmlAttribute(name = "Prioritaet")
        protected BigInteger prioritaet;

        /**
         * Gets the value of the eigenschaft property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEigenschaft() {
            return eigenschaft;
        }

        /**
         * Sets the value of the eigenschaft property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEigenschaft(String value) {
            this.eigenschaft = value;
        }

        /**
         * Gets the value of the prioritaet property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPrioritaet() {
            return prioritaet;
        }

        /**
         * Sets the value of the prioritaet property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPrioritaet(BigInteger value) {
            this.prioritaet = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}tpcStelle">
     *       &lt;attribute name="Eigenschaft" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="Prioritaet" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Stelle
        extends TpcStelle
    {

        @XmlAttribute(name = "Eigenschaft", required = true)
        protected String eigenschaft;
        @XmlAttribute(name = "Prioritaet")
        protected BigInteger prioritaet;

        /**
         * Gets the value of the eigenschaft property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEigenschaft() {
            return eigenschaft;
        }

        /**
         * Sets the value of the eigenschaft property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEigenschaft(String value) {
            this.eigenschaft = value;
        }

        /**
         * Gets the value of the prioritaet property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPrioritaet() {
            return prioritaet;
        }

        /**
         * Sets the value of the prioritaet property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPrioritaet(BigInteger value) {
            this.prioritaet = value;
        }

    }

}
