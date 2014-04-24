<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"></xsl:output>

<xsl:template match="/">
<html>
<head>
<title>Index</title>
<link rel="stylesheet" type="text/css" href="template/tree.css"/>
<script type="text/javascript" src="template/tree.js"></script>
<script type="text/javascript" src="template/tree.js"></script>
<script type="text/javascript" src="template/jquery.tools.min.js"></script>
<script type="text/javascript" src="template/issues.js"></script>
<script type="text/javascript" src="template/jquery.json-2.2.min.js"></script>
<script src="template/jquery-ui-1.8.14.custom.min.js" type="text/javascript"></script>
</head>
<xsl:apply-templates/>
</html>
</xsl:template>

<xsl:template match="LOG">
<body onload="showLogDetails();expandProblemIfNeeded();">
<xsl:apply-templates/>
<script type="text/javascript" src="template/structure.js"></script>
</body>
</xsl:template>

<xsl:template match="MSG">
<div class="trigger">
	<img name="issue_img" src="template/fold_clear.gif"/>
    <xsl:for-each select="ancestor::MSG">
        <xsl:choose>
        <xsl:when test="following-sibling::MSG">
            <img src="template/fold_line.gif"/>
        </xsl:when>
        <xsl:otherwise>
            <img src="template/fold_clear.gif"/>
        </xsl:otherwise>
        </xsl:choose>
    </xsl:for-each>
    <xsl:choose>   
        <xsl:when test="MSG"> 
            <img src="template/fold_closed.gif">
                <xsl:attribute name="id">I<xsl:value-of select="@id"/></xsl:attribute>
                <xsl:attribute name="onClick">toggleBranch('<xsl:value-of select="@id"/>');return false;</xsl:attribute>
            </img>
        </xsl:when>
        <xsl:otherwise>
         <img>
            <xsl:attribute name="src">
                <xsl:choose>
                    <xsl:when test="position()=last()">template/fold_end.gif</xsl:when>
                    <xsl:otherwise>template/fold_cross.gif</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </img>
        </xsl:otherwise>
    </xsl:choose> 
    
    
    <xsl:variable name="status">
	    <xsl:choose>
	       <xsl:when test="descendant-or-self::MSG[@status='error']">Failed</xsl:when>
	       <xsl:when test="descendant-or-self::MSG[@status='warn']">Warning</xsl:when>
	       <xsl:otherwise>Passed</xsl:otherwise>
	     </xsl:choose>
	</xsl:variable>
        
    <img>
        <xsl:attribute name="src">
            <xsl:choose>
            <xsl:when test="$status='Failed'">template/level_error.gif</xsl:when>
            <xsl:when test="$status='Warning'">template/level_warn.gif</xsl:when>
            <xsl:otherwise>template/level_info.gif</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </img>    
    <span class="title">
        <xsl:attribute name="id">T<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:attribute name="onClick">showDetails('<xsl:value-of select="@id"/>');highlightMessage(this);return false;</xsl:attribute>
        <xsl:value-of select="title" disable-output-escaping="yes"/>
    </span>
    <span style="display:none">
        <xsl:attribute name="id">M<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:value-of select="message" disable-output-escaping="yes"/>
    </span>
    <xsl:if test="@snapshot"><img src="template/snapshot_exist.gif" class="snapshot"/>
    <span style="display:none">
        <xsl:attribute name="id">S<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:value-of select="@snapshot" disable-output-escaping="yes"/>
    </span>
    </xsl:if>
    <span style="display:none">
        <xsl:attribute name="id">ST<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:value-of select="$status"/>
    </span>
    <span style="display:none">
        <xsl:attribute name="id">TM<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:value-of select="@t" disable-output-escaping="yes"/>
    </span>
    <span style="display:none">
        <xsl:attribute name="id">D<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:value-of select="self::MSG/TIME/@t" disable-output-escaping="yes"/> second(s)
    </span>
    <span style="display:none">
        <xsl:attribute name="id">SUBs<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:value-of select="count(MSG)"/>
    </span>
</div>
<xsl:if test="MSG"> 
    <div class="branch">
        <xsl:attribute name="id">B<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:apply-templates select="MSG"/>
    </div>
</xsl:if>
</xsl:template>

<!-- avoid output of text node with default template -->
<!--xsl:template match="title"/-->
<xsl:template match="logName">
<div class="log_name" id="log_name" onclick="showLogDetails();" >
<xsl:attribute name="msg"><xsl:if test="parent::LOG/logDescription"><xsl:value-of select="parent::LOG/logDescription" disable-output-escaping="yes" /></xsl:if></xsl:attribute>
<xsl:attribute name="snap"><xsl:if test="parent::LOG/spanshot"><xsl:value-of select="parent::LOG/spanshot" /></xsl:if></xsl:attribute><!-- снапшот для лога. Писать в конце секции лог  -->
<xsl:attribute name="s"><xsl:if test="parent::LOG/status"><xsl:value-of select="parent::LOG/status/@s" /></xsl:if></xsl:attribute>
<xsl:attribute name="d"><xsl:if test="parent::LOG/@date"><xsl:value-of select="parent::LOG/@date" /></xsl:if> </xsl:attribute>
<xsl:attribute name="t"><xsl:if test="parent::LOG"><xsl:value-of select="parent::LOG/@t" /></xsl:if>  </xsl:attribute>
<xsl:attribute name="et"><xsl:if test="parent::LOG/TIME/@t"><xsl:value-of select="parent::LOG/TIME/@t" /></xsl:if><xsl:value-of select="parent::MSG/TIME/@t" /> </xsl:attribute>

<xsl:attribute name="passed_count"><xsl:value-of select="count(parent::LOG/descendant::*/MSG[@status = 'info'])" /></xsl:attribute>
<xsl:attribute name="fail_count"><xsl:value-of select="count(parent::LOG/descendant::*/MSG[@status = 'error'])" /></xsl:attribute>
<xsl:attribute name="warning_count"><xsl:value-of select="count(parent::LOG/descendant::*/MSG[@status = 'warn'])" /></xsl:attribute>

<xsl:value-of select="." disable-output-escaping="yes"/>
</div>
</xsl:template>




</xsl:stylesheet>
