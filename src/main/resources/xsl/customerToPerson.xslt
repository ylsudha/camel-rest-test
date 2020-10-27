<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <person>
            <user>
                <user_first_name><xsl:value-of select="/customer/first_name" /></user_first_name>
                <user_last_name><xsl:value-of select="/customer/last_name" /></user_last_name>
            </user>
            <address>
                <line1><xsl:value-of select="/customer/address/street" /></line1>
                <line2><xsl:value-of select="/customer/address/city" /></line2>
                <country><xsl:value-of select="/customer/address/country" /></country>
                <postcode><xsl:value-of select="/customer/address/zip" /></postcode>
            </address>
        </person>
    </xsl:template>
</xsl:stylesheet>