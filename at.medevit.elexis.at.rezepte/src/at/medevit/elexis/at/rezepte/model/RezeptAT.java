//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.09.07 um 07:54:25 AM CEST 
//


package at.medevit.elexis.at.rezepte.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PatientName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PatientVersicherungsnummer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PatientAnschrift" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Datum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArztMENummer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ArztName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ArztZeile3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ArztAnschrift" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Verschreibungen">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Verschreibung" type="{http://www.medevit.at/RezeptAT}Verschreibung" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="bgimg" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "patientName",
    "patientVersicherungsnummer",
    "patientAnschrift",
    "datum",
    "arztMENummer",
    "arztName",
    "arztZeile3",
    "arztAnschrift",
    "verschreibungen"
})
@XmlRootElement(name = "RezeptAT")
public class RezeptAT {

    @XmlElement(name = "PatientName", required = true)
    protected String patientName;
    @XmlElement(name = "PatientVersicherungsnummer", required = true)
    protected String patientVersicherungsnummer;
    @XmlElement(name = "PatientAnschrift", required = true)
    protected String patientAnschrift;
    @XmlElement(name = "Datum", required = true)
    protected String datum;
    @XmlElement(name = "ArztMENummer")
    protected String arztMENummer;
    @XmlElement(name = "ArztName", required = true)
    protected String arztName;
    @XmlElement(name = "ArztZeile3")
    protected String arztZeile3;
    @XmlElement(name = "ArztAnschrift", required = true)
    protected String arztAnschrift;
    @XmlElement(name = "Verschreibungen", required = true)
    protected RezeptAT.Verschreibungen verschreibungen;
    @XmlAttribute(name = "bgimg")
    protected String bgimg;

    /**
     * Ruft den Wert der patientName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Legt den Wert der patientName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientName(String value) {
        this.patientName = value;
    }

    /**
     * Ruft den Wert der patientVersicherungsnummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientVersicherungsnummer() {
        return patientVersicherungsnummer;
    }

    /**
     * Legt den Wert der patientVersicherungsnummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientVersicherungsnummer(String value) {
        this.patientVersicherungsnummer = value;
    }

    /**
     * Ruft den Wert der patientAnschrift-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientAnschrift() {
        return patientAnschrift;
    }

    /**
     * Legt den Wert der patientAnschrift-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientAnschrift(String value) {
        this.patientAnschrift = value;
    }

    /**
     * Ruft den Wert der datum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Legt den Wert der datum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatum(String value) {
        this.datum = value;
    }

    /**
     * Ruft den Wert der arztMENummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArztMENummer() {
        return arztMENummer;
    }

    /**
     * Legt den Wert der arztMENummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArztMENummer(String value) {
        this.arztMENummer = value;
    }

    /**
     * Ruft den Wert der arztName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArztName() {
        return arztName;
    }

    /**
     * Legt den Wert der arztName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArztName(String value) {
        this.arztName = value;
    }

    /**
     * Ruft den Wert der arztZeile3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArztZeile3() {
        return arztZeile3;
    }

    /**
     * Legt den Wert der arztZeile3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArztZeile3(String value) {
        this.arztZeile3 = value;
    }

    /**
     * Ruft den Wert der arztAnschrift-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArztAnschrift() {
        return arztAnschrift;
    }

    /**
     * Legt den Wert der arztAnschrift-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArztAnschrift(String value) {
        this.arztAnschrift = value;
    }

    /**
     * Ruft den Wert der verschreibungen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RezeptAT.Verschreibungen }
     *     
     */
    public RezeptAT.Verschreibungen getVerschreibungen() {
    	if (verschreibungen == null) {
            verschreibungen = new RezeptAT.Verschreibungen();
        }
        return verschreibungen;
    }

    /**
     * Legt den Wert der verschreibungen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RezeptAT.Verschreibungen }
     *     
     */
    public void setVerschreibungen(RezeptAT.Verschreibungen value) {
        this.verschreibungen = value;
    }

    /**
     * Ruft den Wert der bgimg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgimg() {
        return bgimg;
    }

    /**
     * Legt den Wert der bgimg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgimg(String value) {
        this.bgimg = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Verschreibung" type="{http://www.medevit.at/RezeptAT}Verschreibung" maxOccurs="unbounded"/>
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
        "verschreibung"
    })
    public static class Verschreibungen {

        @XmlElement(name = "Verschreibung", required = true)
        protected List<Verschreibung> verschreibung;

        /**
         * Gets the value of the verschreibung property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the verschreibung property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVerschreibung().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Verschreibung }
         * 
         * 
         */
        public List<Verschreibung> getVerschreibung() {
            if (verschreibung == null) {
                verschreibung = new ArrayList<Verschreibung>();
            }
            return this.verschreibung;
        }

    }

}
