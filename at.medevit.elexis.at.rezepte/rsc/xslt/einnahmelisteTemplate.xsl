<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:str="http://exslt.org/strings">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/">
		<fo:root font-family="FreeSans">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="21cm" page-height="29.7cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body />
					<fo:region-after />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<!-- XSL Stylesheet Medikamentenliste -->
			<!-- (c) MEDEVIT OG 2011; All rights reserved -->
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="40%" />
						<fo:table-column column-width="60%" />
						<fo:table-body font-weight="bold" font-size="12pt"
							border-top-style="solid" border-top-width="0.1pt"
							border-bottom-style="solid" border-bottom-width="0.1pt">
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-weight="bold">MEDIKAMENTENLISTE</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="right" font-weight="bold">
										<fo:inline color="#800000">
											HERZ | KREISLAUF | Praxis Dr.
											Thomas Wolber
										</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

					<fo:block space-before="15mm" />

					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="100%" />
						<fo:table-body font-size="12pt" border-top-style="solid"
							border-top-width="0.1pt" border-bottom-style="solid"
							border-bottom-width="0.1pt">
							<fo:table-row>
								<fo:table-cell font-weight="bold" padding-top="1mm"
									padding-bottom="1mm">
									<fo:block>
										<xsl:value-of select="RezeptAT/Datum" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

					<fo:block space-before="10mm" font-size="12pt">
						<xsl:value-of select="RezeptAT/PatientName" />
					</fo:block>

					<fo:block space-before="10mm" />

					<!-- List der Medikamente -->
					<fo:table table-layout="fixed" width="100%" border-style="solid"
						border-width="0.1mm">
						<fo:table-column column-width="43%" />
						<fo:table-column column-width="8%"
							border-start-style="solid" border-width="0.1mm" />
						<fo:table-column column-width="8%"
							border-start-style="solid" border-width="0.1mm" />
						<fo:table-column column-width="8%"
							border-start-style="solid" border-width="0.1mm" />
						<fo:table-column column-width="8%"
							border-start-style="solid" border-width="0.1mm" />
						<fo:table-column column-width="25%"
							border-start-style="solid" border-width="0.1mm" />
						<fo:table-header border-after-style="solid"
							border-width="0.1mm">
							<!-- <fo:table-row> -->
							<!-- <fo:table-cell> -->
							<!-- <fo:block>Medikament</fo:block> -->
							<!-- </fo:table-cell> -->
							<!-- <fo:table-cell number-columns-spanned="4" -->
							<!-- text-align="center"> -->
							<!-- <fo:block>Einnahme</fo:block> -->
							<!-- </fo:table-cell> -->
							<!-- <fo:table-cell> -->
							<!-- <fo:block>Bemerkungen</fo:block> -->
							<!-- </fo:table-cell> -->
							<!-- </fo:table-row> -->
							<fo:table-row>
								<fo:table-cell>
									<fo:block></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt" text-align="center">Morgen
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt" text-align="center">Mittag
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt" text-align="center">Abend
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-size="10pt" text-align="center">Nacht
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block />
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body font-weight="normal" font-size="12pt">
							<!-- ====================== -->
							<xsl:for-each select="RezeptAT/Verschreibungen/Verschreibung">
								<fo:table-row border-after-style="solid"
									border-width="0.1mm">
									<fo:table-cell padding-left="1mm" padding-top="0.75mm">
										<fo:block>
											<xsl:value-of select="Artikelname" />
										</fo:block>

										<xsl:if test="Einnahmevorschrift != ''">
											<fo:block font-size="10pt" font-style="italic">
												<xsl:value-of select="Einnahmevorschrift" />
											</fo:block>
										</xsl:if>

									</fo:table-cell>
									<xsl:variable name="Dosage" select="Dosierung" />
									<xsl:variable name="DosageToken" select="str:tokenize($Dosage,'-')" />
									<fo:table-cell text-align="center" padding-top="0.75mm">
										<fo:block>
<!-- 											<xsl:if test="count($DosageToken) > 3"> -->
<!-- 												blabla -->
<!-- 											</xsl:if> -->
											<xsl:value-of select="$DosageToken[1]" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell text-align="center" padding-top="0.75mm">
										<fo:block>
											<xsl:value-of select="$DosageToken[2]" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="center" padding-top="0.75mm">
											<xsl:value-of select="$DosageToken[3]" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="center" padding-top="0.75mm">
											<xsl:value-of select="$DosageToken[4]" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-left="0.5mm" padding-top="0.75mm">
										<fo:block>
											<xsl:if test="Bemerkung != ''">
												<fo:block font-size="10pt">
													<xsl:value-of select="Bemerkung" />
												</fo:block>
											</xsl:if>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>

								<!-- COND Einnahmevorschrift <xsl:if test="Einnahmevorschrift != 
									''"> <fo:table-row> <fo:table-cell> <fo:block></fo:block> </fo:table-cell> 
									<fo:table-cell> <fo:block font-size="10pt"> <fo:inline font-style="italic">Einnahmevorschrift: 
									</fo:inline> <xsl:value-of select="Einnahmevorschrift" /> </fo:block> </fo:table-cell> 
									<fo:table-cell number-columns-spanned="5"> <fo:block></fo:block> </fo:table-cell> 
									</fo:table-row> </xsl:if> -->

								<!-- COND Bemerkung -->
								<!-- <xsl:if test="Bemerkung != ''"> <fo:table-row> <fo:table-cell 
									> <fo:block></fo:block> </fo:table-cell> <fo:table-cell> <fo:block></fo:block> 
									</fo:table-cell> <fo:table-cell> <fo:block font-size="10pt"> <fo:inline font-style="italic">Bemerkung: 
									</fo:inline> <xsl:value-of select="Bemerkung" /> </fo:block> </fo:table-cell> 
									<fo:table-cell number-columns-spanned="3"> <fo:block></fo:block> </fo:table-cell> 
									</fo:table-row> </xsl:if> -->

							</xsl:for-each>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>