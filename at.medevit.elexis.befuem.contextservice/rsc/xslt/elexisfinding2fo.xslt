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
	<!-- root element: finding -->
	<!-- ========================= -->
	<xsl:template match="finding">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body/>
					<fo:region-after/>
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="simpleA4">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block font-size="10pt" text-align-last="justify"
						width="100%" border-top-style="solid" border-top-width="thin"
						border-bottom-width="thin" border-bottom-style="solid" padding="1mm">
						Elexis Austria
						<fo:leader leader-pattern="space"/>
						<xsl:value-of select="/finding/patient/lastname"/>
						<fo:character character="&#x20;"/>
						<xsl:value-of select="/finding/patient/firstname"/>
						<fo:character character="&#x2C;"/>
						<fo:character character="&#x20;"/>
						<xsl:value-of select="/finding/patient/birthdate"/>
						<fo:character character="&#x20;"/>
						<fo:character character="&#x2D;"/>
						<fo:character character="&#x20;"/>
						Page
						<fo:page-number/>
						of
						<fo:page-number-citation ref-id="last-page"/>
					</fo:block>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="10pt" border-top-style="solid"
						border-top-width="thin" border-bottom-width="thin"
						border-bottom-style="solid" space-after="1cm" padding-top="2mm"
						padding-bottom="2mm">
						<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="50%"/>
							<fo:table-column column-width="50%"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-right="5mm">
										<fo:table>
											<fo:table-column/>
											<fo:table-column/>
											<fo:table-body>
												<xsl:apply-templates select="source"/>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
									<fo:table-cell padding-left="5mm">
										<fo:table>
											<fo:table-column/>
											<fo:table-column/>
											<fo:table-body>
												<xsl:apply-templates select="patient"/>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
					<xsl:apply-templates select="content"/>
					<fo:block id="last-page"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- ========================= -->
	<!-- child element: source -->
	<!-- ========================= -->
	<xsl:template match="source">
		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left" font-weight="bold">Absender:
				</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="lastname"/>
					<fo:character character="&#x20;"/>
					<xsl:value-of select="firstname"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">ME Nummer:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="menumber"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">Strasse:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="street1"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">PLZ/Ort:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="zip"/>
					<fo:character character="&#x2F;"/>
					<xsl:value-of select="city"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">Datum:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="/finding/date"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

	</xsl:template>

	<!-- ========================= -->
	<!-- child element: patient -->
	<!-- ========================= -->
	<xsl:template match="patient">
		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left" font-weight="bold">Patient:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="lastname"/>
					<fo:character character="&#x20;"/>
					<xsl:value-of select="firstname"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">Geburtsdatum:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="birthdate"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">Strasse:</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block text-align="right" white-space="pre">
					<xsl:value-of select="street1"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<fo:table-row>
			<fo:table-cell>
				<fo:block text-align="left">PLZ/Ort:</fo:block>
			</fo:table-cell>
			<fo:table-cell text-align="right" white-space="pre">
				<fo:block>
					<xsl:value-of select="zip"/>
					<fo:character character="&#x2F;"/>
					<xsl:value-of select="city"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>

		<xsl:if test="insurancenumber">
			<fo:table-row>
				<fo:table-cell>
					<fo:block text-align="left">SV Nummer:</fo:block>
				</fo:table-cell>
				<fo:table-cell text-align="right" white-space="pre">
					<fo:block>
						<xsl:value-of select="insurancenumber"/>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</xsl:if>

	</xsl:template>

	<!-- ========================= -->
	<!-- child element: contentpart -->
	<!-- ========================= -->
	<xsl:template match="contentpart">
		<fo:block font-size="10pt" font-family="monospace"
			linefeed-treatment="preserve" white-space-collapse="false"
			white-space-treatment="preserve" wrap-option="wrap">
			<xsl:value-of select="text"/>
		</fo:block>
		<xsl:apply-templates select="labresult"/>
	</xsl:template>

	<!-- ========================= -->
	<!-- child element: labresult -->
	<!-- ========================= -->
	<xsl:template match="labresult">
		<fo:block font-size="10pt" font-family="monospace"
			space-after="1cm" space-before="1cm">

			<xsl:if test="resultdate">
				<fo:block>
					Resultat Datum:
					<fo:character character="&#x20;"/>
					<xsl:value-of select="resultdate"/>
				</fo:block>
			</xsl:if>

			<xsl:if test="materialdate">
				<fo:block>
					Material Datum:
					<fo:character character="&#x20;"/>
					<xsl:value-of select="materialdate"/>
				</fo:block>
			</xsl:if>

			<xsl:if test="resultinfo">
				<fo:block space-before="1cm">
					Befund Hinweise:
			</fo:block>
				<fo:block space-after="1cm">
					<xsl:value-of select="resultinfo"/>
				</fo:block>
			</xsl:if>


			<xsl:apply-templates select="test"/>

		</fo:block>
	</xsl:template>

	<!-- ========================= -->
	<!-- child element: test -->
	<!-- ========================= -->
	<xsl:template match="test">

		<fo:table table-layout="fixed" width="100%">
			<!-- testid, pathologic, result, resultinfo, unit, reference, referenceinfo -->
			<fo:table-column column-width="10%"
				border-right-style="solid" border-right-color="#ffffff"/>
			<fo:table-column column-width="5%"
				border-right-style="solid" border-right-color="#ffffff"/>
			<fo:table-column column-width="10%"
				border-right-style="solid" border-right-color="#ffffff"/>
			<fo:table-column column-width="25%"
				border-right-style="solid" border-right-color="#ffffff"/>
			<fo:table-column column-width="15%"
				border-right-style="solid" border-right-color="#ffffff"/>
			<fo:table-column column-width="15%"
				border-right-style="solid" border-right-color="#ffffff"/>
			<fo:table-column column-width="20%"/>

			<fo:table-header text-align="center">
				<fo:table-row background-color="#cccccc">
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">ID</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">PC</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">Resultat</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">Info</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">Einheit</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">Normbereich</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1mm">
						<fo:block font-weight="bold">Info</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
				<xsl:for-each select="testresult">
					<xsl:choose>
						<xsl:when test="position() mod 2 = 0">
							<fo:table-row background-color="#dddddd">
								<xsl:call-template name="testresultrow"/>
							</fo:table-row>
						</xsl:when>
						<xsl:otherwise>
							<fo:table-row background-color="#ffffff">
								<xsl:call-template name="testresultrow"/>
							</fo:table-row>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>

	</xsl:template>

	<!-- ========================= -->
	<!-- child element: testresult -->
	<!-- ========================= -->
	<xsl:template name="testresultrow">
		<fo:table-cell padding="1mm">
			<fo:block>
				<!-- <xsl:if test="itemcode"> <fo:character character="&#x5B;" /> <xsl:value-of 
					select="itemcode" /> <fo:character character="&#x5D;" /> <fo:character character="&#x0A;" 
					/> </xsl:if> -->
				<xsl:value-of select="itemshortdesc"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell padding="1mm">
			<fo:block>
				<xsl:value-of select="pathologiccode"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell padding="1mm">
			<fo:block>
				<xsl:value-of select="data"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell padding="1mm">
			<fo:block>
				<xsl:value-of select="datainfo"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell padding="1mm">
			<fo:block>
				<xsl:value-of select="dataunit"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell padding="1mm">
			<fo:block>
				<xsl:value-of select="refmin"/>
				-
				<xsl:value-of select="refmax"/>
			</fo:block>
		</fo:table-cell>
		<fo:table-cell padding="1mm">
			<fo:block>
				<xsl:value-of select="refinfo"/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
</xsl:stylesheet>
