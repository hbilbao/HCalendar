<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:output method="xml" version="1.0" omit-xml-declaration="no"
		indent="yes" />
	<xsl:param name="versionParam" select="'1.0'" />
	<!-- ========================= -->
	<!-- root element: WorkInputsDTO -->
	<!-- ========================= -->
	<xsl:template match="WorkInputsDTO">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="20pt" font-weight="bold" space-after="10mm"
						text-align="center">
						Informe de imputaciones
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold" space-after="2mm">
						Nombre del perfil:
						<xsl:value-of select="profileName" />
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold" space-after="5mm">
						Año:
						<xsl:value-of select="year" />
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold" space-after="5mm">
						Mostrando resultados desde
						<xsl:value-of select="fromFilter" />
						a
						<xsl:value-of select="toFilter" />
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold" space-after="5mm">
						Días trabajados:
					</fo:block>
					<fo:block font-size="8pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate" border-style="solid" border-width="0.2mm">
							<fo:table-column column-width="23%" />
							<fo:table-column column-width="23%" />
							<fo:table-column column-width="54%" />
							<fo:table-header>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Fecha</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Horas</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Comentarios</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="workInput" />
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold" space-before="3mm">
						Resumen por mes:
					</fo:block>
					<fo:block font-size="8pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate" border-style="solid" border-width="0.2mm">
							<fo:table-column column-width="50%" />
							<fo:table-column column-width="50%" />
							<fo:table-header>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Mes</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Horas</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="monthresume" />
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="8pt" space-after="3mm">
						Total horas: <xsl:value-of select="totalHours" />
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold"
						space-before="3mm" space-after="3mm">
						Vacaciones:
					</fo:block>
					<fo:block font-size="8pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate" border-style="solid" border-width="0.2mm">
							<fo:table-column column-width="30%" />
							<fo:table-column column-width="70%" />
							<fo:table-header>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Fecha</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Comentarios</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="holiday" />
							</fo:table-body>
						</fo:table>
					</fo:block>
					<fo:block font-size="10pt" font-weight="bold"
						space-before="3mm" space-after="3mm">
						Días libres:
					</fo:block>
					<fo:block font-size="8pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate" border-style="solid" border-width="0.2mm">
							<fo:table-column column-width="30%" />
							<fo:table-column column-width="70%" />
							<fo:table-header>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Fecha</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.2mm">
									<fo:block font-weight="bold">Comentarios</fo:block>
								</fo:table-cell>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="freeday" />
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: workInput -->
	<!-- ========================= -->
	<xsl:template match="workInput">
		<fo:table-row>
			<xsl:if test="function = 'lead'">
				<xsl:attribute name="font-weight">bold</xsl:attribute>
			</xsl:if>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="date" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="hours" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="description" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: holiday -->
	<!-- ========================= -->
	<xsl:template match="holiday">
		<fo:table-row>
			<xsl:if test="function = 'lead'">
				<xsl:attribute name="font-weight">bold</xsl:attribute>
			</xsl:if>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="date" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="comment" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: freeday -->
	<!-- ========================= -->
	<xsl:template match="freeday">
		<fo:table-row>
			<xsl:if test="function = 'lead'">
				<xsl:attribute name="font-weight">bold</xsl:attribute>
			</xsl:if>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="date" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="comment" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<!-- ========================= -->
	<!-- child element: monthresume -->
	<!-- ========================= -->
	<xsl:template match="monthresume">
		<fo:table-row>
			<xsl:if test="function = 'lead'">
				<xsl:attribute name="font-weight">bold</xsl:attribute>
			</xsl:if>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="monthName" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block>
					<xsl:value-of select="monthHours" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>
