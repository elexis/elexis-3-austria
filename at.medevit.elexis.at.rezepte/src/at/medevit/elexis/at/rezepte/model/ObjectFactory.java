//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.09.07 um 07:54:25 AM CEST 
//


package at.medevit.elexis.at.rezepte.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.medevit.elexis.at.rezepte.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.medevit.elexis.at.rezepte.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RezeptAT }
     * 
     */
    public RezeptAT createRezeptAT() {
        return new RezeptAT();
    }

    /**
     * Create an instance of {@link RezeptAT.Verschreibungen }
     * 
     */
    public RezeptAT.Verschreibungen createRezeptATVerschreibungen() {
        return new RezeptAT.Verschreibungen();
    }

    /**
     * Create an instance of {@link Verschreibung }
     * 
     */
    public Verschreibung createVerschreibung() {
        return new Verschreibung();
    }

}
