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
package at.medevit.elexis.befuem.contextservice.finding;

import ch.elexis.data.Kontakt;
import ch.elexis.data.Patient;
import ch.elexis.data.Person;

public class ContactInfo {
	String lastname;
	String firstname;
	String street1;
	String street2;
	String city;
	String zip;
	String email;
	String menumber;
	String birthdate;
	String gender;
	String tel;
	String insurancenumber;

	public void setPatient(Patient pat) {
		setBirthdate(pat.getGeburtsdatum());
		setCity(pat.get(Patient.FLD_PLACE));
		setFirstname(pat.getVorname());
		setGender(pat.getGeschlecht());
		setLastname(pat.getName());
		setStreet1(pat.get(Patient.FLD_STREET));
		setZip(pat.get(Patient.FLD_ZIP));
	}
	
	/**
	 * Change the information contained by this Object to
	 * the Information provided by the {@link Kontakt} or its subclasses.
	 * 
	 * @param kon
	 */
	public void setKontakt(Kontakt kon) {
		if(kon.istOrganisation()) {
			setZip(kon.get(Kontakt.FLD_ZIP));
			setCity(kon.get(Kontakt.FLD_PLACE));
			setStreet1(kon.get(Kontakt.FLD_STREET));
			setLastname(kon.get(Kontakt.FLD_NAME2));
			setFirstname(kon.get(Kontakt.FLD_NAME1));
		} else if (kon.istPerson()) {
			setZip(kon.get(Person.FLD_ZIP));
			setCity(kon.get(Person.FLD_PLACE));
			setStreet1(kon.get(Person.FLD_STREET));
			setLastname(kon.get(Person.FLD_NAME1));
			setFirstname(kon.get(Person.FLD_NAME2));
			setBirthdate(kon.get(Person.BIRTHDATE));
			setGender(kon.get(Person.SEX));
		} else {
			setZip(kon.get(Kontakt.FLD_ZIP));
			setCity(kon.get(Kontakt.FLD_PLACE));
			setStreet1(kon.get(Kontakt.FLD_STREET));
			setLastname(kon.get(Kontakt.FLD_NAME1));
			setFirstname(kon.get(Kontakt.FLD_NAME2));			
		}
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMenumber() {
		return menumber;
	}
	public void setMenumber(String menumber) {
		this.menumber = menumber;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getInsurancenumber() {
		return insurancenumber;
	}
	public void setInsurancenumber(String inumber) {
		insurancenumber = inumber;
	}
	@Override
	public String toString() {
		return "ContactInfo: " + firstname + " " + lastname + " " + city + " " + zip + " " + menumber;
	}
}
