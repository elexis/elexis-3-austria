//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.09.07 um 07:54:25 AM CEST 
//


package at.medevit.elexis.at.rezepte.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ch.elexis.data.Prescription;

/**
 * <p>Java-Klasse für Verschreibung complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Verschreibung">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Artikelname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Einnahmevorschrift" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Dosierung" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Originalpackung" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Bemerkung" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Verschreibung", propOrder = {
    "artikelname",
    "einnahmevorschrift",
    "dosierung",
    "originalpackung",
    "bemerkung"
})
public class Verschreibung {

    @XmlElement(name = "Artikelname", required = true)
    protected String artikelname;
    @XmlElement(name = "Einnahmevorschrift")
    protected String einnahmevorschrift;
    @XmlElement(name = "Dosierung", required = true)
    protected String dosierung;
    @XmlElement(name = "Originalpackung")
    protected String originalpackung;
    @XmlElement(name = "Bemerkung")
    protected String bemerkung;
    @XmlTransient
    protected Prescription prescription;

    /**
     * Ruft den Wert der artikelname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtikelname() {
        return artikelname;
    }

    /**
     * Legt den Wert der artikelname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtikelname(String value) {
        this.artikelname = value;
    }

    /**
     * Ruft den Wert der einnahmevorschrift-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEinnahmevorschrift() {
        return einnahmevorschrift;
    }

    /**
     * Legt den Wert der einnahmevorschrift-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEinnahmevorschrift(String value) {
        this.einnahmevorschrift = value;
    }

    /**
     * Ruft den Wert der dosierung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDosierung() {
        return dosierung;
    }

    /**
     * Legt den Wert der dosierung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDosierung(String value) {
        this.dosierung = value;
    }

    /**
     * Ruft den Wert der originalpackung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalpackung() {
        return originalpackung;
    }

    /**
     * Legt den Wert der originalpackung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalpackung(String value) {
        this.originalpackung = value;
    }

    /**
     * Ruft den Wert der bemerkung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBemerkung() {
        return bemerkung;
    }

    /**
     * Legt den Wert der bemerkung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBemerkung(String value) {
        this.bemerkung = value;
    }

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public Prescription getPrescription() {
		return prescription;
	}

}
