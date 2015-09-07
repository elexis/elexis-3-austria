<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/">
		<fo:root font-family="FreeSans">
			<fo:layout-master-set>
				<fo:simple-page-master margin-top="107.0mm"
					margin-bottom="37.0mm" margin-left="28.0mm" margin-right="1.0mm"
					page-width="99.994861mm" page-height="209.990972mm" master-name="rezept">

					<fo:region-body>
						<xsl:attribute name="background-image"><xsl:value-of
							select="RezeptAT/@bgimg" /></xsl:attribute>
						<xsl:attribute name="background-repeat">no-repeat</xsl:attribute>
					</fo:region-body>
					<fo:region-after />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<!-- XSL Stylesheet Rezept -->
			<!-- (c) MEDEVIT OG 2011; All rights reserved -->
			<fo:page-sequence master-reference="rezept">
				<fo:static-content flow-name="xsl-region-after">
					<!-- Patient/in -->
					<fo:block-container absolute-position="fixed"
						top="47mm" left="5mm" width="55mm" height="10mm" font-size="10.5pt">
						<fo:block>
							<xsl:value-of select="RezeptAT/PatientName" />
						</fo:block>
					</fo:block-container>
					<!-- LF. Nr -->
					<fo:block-container absolute-position="fixed"
						top="47mm" left="75mm" width="20mm" font-size="10.5pt" text-align="right">
						<fo:block>
							<xsl:value-of select="RezeptAT/PatientVersicherungsnummer" />
						</fo:block>
					</fo:block-container>
					<!-- Anschrift -->
					<fo:block-container absolute-position="fixed"
						top="63mm" left="5mm" width="90mm" font-size="10.5pt">
						<fo:block>
							<xsl:value-of select="RezeptAT/PatientAnschrift" />
						</fo:block>
					</fo:block-container>
					<!-- Datum -->
					<fo:block-container absolute-position="fixed"
						top="100mm" left="75mm" width="20mm" font-size="10.5pt"
						text-align="right">
						<fo:block>
							<xsl:value-of select="RezeptAT/Datum" />
						</fo:block>
					</fo:block-container>
					<!-- Signatur -->
					<fo:block-container absolute-position="fixed"
						top="174mm" left="50mm" width="47mm" font-size="8pt" text-align="center">
						<fo:block><xsl:value-of select="RezeptAT/ArztMENummer" /></fo:block>
						<fo:block><xsl:value-of select="RezeptAT/ArztName" /></fo:block>
						<fo:block><xsl:value-of select="RezeptAT/ArztZeile3" /></fo:block>
						<fo:block><xsl:value-of select="RezeptAT/ArztAnschrift" /></fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="xsl-region-body">
					<!-- Dynamische Aufzählung der Medikamente -->
					<fo:block-container>
						<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="7mm" />
							<fo:table-column column-width="46mm" />
							<fo:table-column column-width="14mm" />
							<fo:table-header font-weight="bold" font-size="10pt"
								border-style="solid" border-width="0.1mm">
								<fo:table-row>
									<fo:table-cell>
										<fo:block text-align="center">OP</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block>Sig</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-footer border-after-style="solid"
								border-width="0.1mm">
								<fo:table-row>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
									<fo:table-cell>
										<fo:block />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-footer>
							<fo:table-body font-weight="normal" font-size="9pt"
								border-start-style="solid" border-end-style="solid"
								border-width="0.1mm">
								<xsl:for-each select="RezeptAT/Verschreibungen/Verschreibung">
									<fo:table-row border-before-style="solid" border-width="0.1mm" >
										<fo:table-cell>
											<fo:block text-align="center">
												<xsl:value-of select="Originalpackung" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-start-style="solid" border-width="0.1mm" padding-left="0.5mm">
											<fo:block>
												<xsl:value-of select="Artikelname" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-start-style="solid" border-width="0.1mm" padding-left="0.5mm">
											<fo:block>
												<xsl:value-of select="Dosierung" />
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<xsl:if test="Einnahmevorschrift != ''">
										<fo:table-row keep-with-previous="always">
											<fo:table-cell>
												<fo:block></fo:block>
											</fo:table-cell>
											<fo:table-cell border-start-style="solid" border-width="0.1mm" padding-left="0.5mm">
												<fo:block font-style="italic">
													<xsl:value-of select="Einnahmevorschrift" />
												</fo:block>
											</fo:table-cell>
											<fo:table-cell border-start-style="solid" border-width="0.1mm" padding-left="0.5mm">
												<fo:block></fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</xsl:for-each>
							</fo:table-body>
						</fo:table>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>