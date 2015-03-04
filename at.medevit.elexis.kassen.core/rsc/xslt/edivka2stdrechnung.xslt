<?xml version="1.0" encoding="UTF-8"?>
<!-- 
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
-->
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:output method="xml" version="1.0" omit-xml-declaration="no"
		indent="yes" encoding="UTF-8"/>
	<xsl:param name="versionParam" select="'1.0'"/>

	<!-- ========================= -->
	<!-- root element: PKVRechnung -->
	<!-- ========================= -->
	<xsl:template match="PKVRechnung">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body margin-bottom="2cm"/>
					<fo:region-after extent="2cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="simpleA4">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block width="100%" text-align-last="justify" font-size="10pt" padding-top="15mm">
						<fo:leader leader-pattern="space"/>
						Seite
						<fo:page-number/>
						von
						<fo:page-number-citation ref-id="last-page"/>
					</fo:block>
				</fo:static-content>
			
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="10pt">
						<fo:block width="100%" space-after="5mm">
							<xsl:apply-templates select="DokumentKopfInformationen"/>
						</fo:block>
						
						<fo:block width="100%" space-after="5mm">
							<xsl:apply-templates select="DokumentEmpfaenger"/>
						</fo:block>
	
						<fo:block width="100%" font-size="16pt" font-weight="bold" text-align="center">
							HONORARNOTE
						</fo:block>
						<fo:block width="100%" space-after="5mm" text-align="center">
							Rechnungsnummer: <xsl:value-of select="DokumentKopfInformationen/Leistender/Dokument/DokumentenKey"/>
						</fo:block>
	
						<fo:block width="100%" space-after="5mm">
							<xsl:apply-templates select="Patient"/>
						</fo:block>
						
						<fo:block width="100%" space-after="5mm">
							<xsl:apply-templates select="KlinischeInformationDiagnosen"/>
						</fo:block>
	
						<fo:block width="100%" space-after="10mm">
							<xsl:apply-templates select="LeistungenDetails"/>
						</fo:block>
	
						<fo:block width="100%" space-after="5mm" keep-together="always">
							<xsl:apply-templates select="DokumentAussteller"/>
						</fo:block>

						<fo:footnote>
							<fo:inline></fo:inline>
							<fo:footnote-body>
								<fo:block width="100%" keep-together="always">
									<xsl:call-template name="Rueckforderung"/>
								</fo:block>				
							</fo:footnote-body>
						</fo:footnote>
						<fo:block id="last-page"/>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: DokumentKopfInformationen -->
	<!-- ========================= -->
	<xsl:template match="DokumentKopfInformationen">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="70%"/>
			<fo:table-column column-width="30%"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>
							<xsl:value-of select="Leistender/Stelle/GesamterName"/>
						</fo:block>
						<fo:block>
						    <xsl:for-each select="Leistender/Stelle/Identifikationen/Identifikation">
						    	<xsl:if test="Typ = 'FachgebietName'">
						        	<xsl:value-of select="Wert"/>
						    	</xsl:if>
						    </xsl:for-each>
						</fo:block>
						<fo:block>
							<xsl:value-of select="Leistender/Stelle/Adresse/Strasse"/>
							<fo:character character="&#x20;"/>
							<xsl:value-of select="Leistender/Stelle/Adresse/HausNummer"/>
						</fo:block>
						<fo:block>
							<xsl:value-of select="Leistender/Stelle/Adresse/PostLeitzahl"/>
							<fo:character character="&#x20;"/>
							<xsl:value-of select="Leistender/Stelle/Adresse/Ort"/>
						</fo:block>
						<fo:block>
						    <xsl:for-each select="Leistender/Stelle/Identifikationen/Identifikation">
						    	<xsl:if test="Typ = 'FachgebietCode'">
						        	<xsl:value-of select="Wert"/>
						    	</xsl:if>
						    </xsl:for-each>
						    <fo:character character="&#x2F;"/>
						    <xsl:for-each select="Leistender/Stelle/Identifikationen/Identifikation">
						    	<xsl:if test="Typ = 'HauptverbandVPN'">
						        	<xsl:value-of select="Wert"/>
						    	</xsl:if>
						    </xsl:for-each>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm" display-align="after">
						<fo:block>
							<xsl:value-of select="Leistender/Stelle/Adresse/Ort"/>
							<fo:character character="&#x2C;"/>
							<fo:character character="&#x20;"/>
							<xsl:value-of select="Leistender/Dokument/DokumentenZeit"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: DokumentEmpfaenger -->
	<!-- ========================= -->
	<xsl:template match="DokumentEmpfaenger">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>
							<xsl:value-of select="GesamterName"/>
						</fo:block>
						<fo:block>
							<xsl:if test="Adresse/Strasse != ''">
								<xsl:value-of select="Adresse/Strasse"/>
								<fo:character character="&#x20;"/>
							</xsl:if>
							<xsl:value-of select="Adresse/HausNummer"/>
						</fo:block>
						<fo:block>
							<xsl:if test="Adresse/PostLeitzahl != ''">
								<xsl:value-of select="Adresse/PostLeitzahl"/>
								<fo:character character="&#x20;"/>
							</xsl:if>
							<xsl:value-of select="Adresse/Ort"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: Patient -->
	<!-- ========================= -->
	<xsl:template match="Patient">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="60%"/>
			<fo:table-column column-width="40%"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>
							Patient:
						</fo:block>
						<fo:block>
							<xsl:if test="TitelBezeichnung != ''">
								<xsl:value-of select="TitelBezeichnung"/>
								<fo:character character="&#x20;"/>
							</xsl:if>
							<xsl:value-of select="VorName"/>
							<fo:character character="&#x20;"/>
							<xsl:value-of select="FamilienName"/>
						</fo:block>
						<fo:block>
							VSNR: <xsl:value-of select="SVNummer"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: KlinischeInformationDiagnosen -->
	<!-- ========================= -->
	<xsl:template match="KlinischeInformationDiagnosen">
		<fo:table table-layout="fixed" width="100%">
 			<fo:table-column column-width="70%"/>
			<fo:table-column column-width="25%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>
							Diagnose(n) bzw. ICD-Code:
							<xsl:value-of select="HauptDiagnose/DiagnoseCode"/>
							<fo:character character="&#x20;"/>
							<xsl:value-of select="HauptDiagnose/DiagnoseBezeichnung"/>
							<xsl:for-each select="NebenDiagnose">
								<fo:character character="&#x2C;"/>
								<fo:character character="&#x20;"/>
								<xsl:value-of select="DiagnoseCode"/>
								<fo:character character="&#x20;"/>
								<xsl:value-of select="DiagnoseBezeichnung"/>
						    </xsl:for-each>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block text-align="right">
							Erste Hilfe
						</fo:block>
					</fo:table-cell>
 					<fo:table-cell padding="1mm">
						<fo:block>
						<fo:table table-layout="fixed" width="100%">
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding="2mm" border-width="thin" border-style="solid">
										<fo:block>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: LeistungenDetails -->
	<!-- ========================= -->
	<xsl:template match="LeistungenDetails">
		<fo:block space-after="5mm">
			Ich erlaube mir folgendes Honorar in Rechnung zu stellen:
		</fo:block>
	
		<fo:table table-layout="fixed" width="100%">
		 	<fo:table-column column-width="7%"/>
			<fo:table-column column-width="16%"/>
			<fo:table-column column-width="7%"/>
			<fo:table-column column-width="10%"/>
			<fo:table-column column-width="50%"/>
			<fo:table-column column-width="10%"/>
			
			<fo:table-header text-align="left">
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>Zeile</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>Datum</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>Anz.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>Pos.</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>Leistungstext</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>Betrag</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			
			<fo:table-body>
				<xsl:for-each select="Leistung">
					<fo:table-row>
						<fo:table-cell padding="1mm">
							<fo:block>
								<xsl:value-of select="PositionsNummer"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="1mm">
							<fo:block>
								<xsl:value-of select="LeistungsDatumZeitVon"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="1mm">
							<fo:block>
								<xsl:value-of select="MengeLeistung"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="1mm">
							<fo:block>
								<xsl:value-of select="IdentifikationLeistung/LeistungsIdentifikation"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="1mm">
							<fo:block>
								<xsl:value-of select="IdentifikationLeistung/LeistungsIdentifikationsBezeichnung"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell padding="1mm">
							<fo:block>
								<xsl:value-of select="PreisLeistung"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>
							Rechnungsbetrag in Euro
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block>
							<xsl:value-of select="/PKVRechnung/EndsummenRechnung/EndsummeBrutto"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: DokumentAussteller -->
	<!-- ========================= -->
	<xsl:template match="DokumentAussteller">
		<fo:block>
		Bitte um Überweisung auf folgendes Konto:
		</fo:block>
		<fo:block>
		<xsl:value-of select="BankName"/>
		<fo:character character="&#x20;"/>
		BLZ
		<fo:character character="&#x20;"/>
		<xsl:value-of select="BankLeitzahl"/>
		<fo:character character="&#x20;"/>
		Kto. Nr.
		<fo:character character="&#x20;"/>
		<xsl:value-of select="KontoNummer"/>
		</fo:block>
	</xsl:template>
	
	<!-- ========================= -->
	<!-- child element: DokumentAussteller -->
	<!-- ========================= -->
	<xsl:template name="Rueckforderung">
		<fo:table table-layout="fixed" width="100%">
 			<fo:table-column column-width="48%"/>
			<fo:table-column column-width="4%"/>
			<fo:table-column column-width="48%"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell padding="1mm">
						<fo:block>
						Bitte ausfüllen:
						</fo:block>
						<fo:block>
						Ich ersuche um Überweisung auf folgendes Konto:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						Bank:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						Kto. Nr.:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						BLZ:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						IBAN:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						BIC/SWIFT Code:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						Kontoinhaber:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						VSNR:
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						Datum:
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block space-after="1cm">
							Stempel und Unterschrift des Arztes:
						</fo:block>
						<fo:block>
							Anmerkung des Versicherten:
						</fo:block>
						<fo:block padding-top="1cm" padding-bottom="1cm" border-width="thin" border-style="solid" space-after="5mm">
						</fo:block>
						<fo:block border-bottom-width="thin" border-bottom-style="solid">
						Unterschrift:
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>
