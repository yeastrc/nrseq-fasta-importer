

<%--  header_main.jsp    /WEB-INF/jsp-includes/header_main.jsp 

--%>


<%@ include file="/WEB-INF/jsp-includes/strutsTaglibImport.jsp" %>
<%@ include file="/WEB-INF/jsp-includes/jstlTaglibImport.jsp" %>


<%--  Default title --%>

<%-- 
	<c:if test="${ empty pageTitle }" >

		<c:set var="pageTitle" value="XXXXXXXXXXXXXXx" ></c:set>
	
	</c:if>
--%>

<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
response.addHeader("Cache-control", "no-store"); // tell proxy not to cache
response.addHeader("Cache-control", "max-age=0"); // stale right away
%>

<%--
	HTML5 DOCTYPE
	
	The DOCTYPE is partially put in to make IE not go into quirks mode (the default when there is no DOCTYPE).

--%>

<!DOCTYPE html>

<html class="no-js"> <!--  Modernizr will change "no-js" to "js" if Javascript is enabled -->


<head>

 <%@ include file="/WEB-INF/jsp-includes/head_section_include_every_page.jsp" %>

	<title>Import FASTA File - <c:out value="${ pageTitle }" ></c:out></title>

	
	<style >
	
		/* This depends on the JQueryUI ui-lightness theme being included in the web app  */
		/*
		body.page-main .modal-dialog-overlay-background { background: #666 url(${ contextPath }/css/jquery-ui-1.10.2-Themes/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png) 50% 50% repeat; }
		*/ 	
	</style>

	<%--  Output the contents of "headerAdditions" here --%>
	<c:out value="${ headerAdditions }" escapeXml="false" ></c:out>



	<link rel="stylesheet" href="${ contextPath }/css/global.css" type="text/css" media="print, projection, screen" />


</head>
 

<body class="page-main <c:out value="${ pageBodyClass }" ></c:out>">

<%-- 
 <div class="page-main-outermost-div">  --%> <%--  This div is closed in footer_main.jsp --%>


		