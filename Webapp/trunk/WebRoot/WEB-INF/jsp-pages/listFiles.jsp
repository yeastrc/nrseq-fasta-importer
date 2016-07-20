<%@page import="org.yeastrc.nrseq_fasta_importer.constants.FileUploadConstants"%>
<%@ include file="/WEB-INF/jsp-includes/pageEncodingDirective.jsp" %>

<%@ include file="/WEB-INF/jsp-includes/strutsTaglibImport.jsp" %>
<%@ include file="/WEB-INF/jsp-includes/jstlTaglibImport.jsp" %>

<c:set var="headerAdditions" >


</c:set>

<%@ include file="/WEB-INF/jsp-includes/header_main.jsp" %>

<script type="text/javascript" src="${ contextPath }/js/handleServicesAJAXErrors.js"></script>
    
<script type="text/javascript" src="${ contextPath }/js/uploadFASTAFileUsingAJAX.js"></script>


<div style="padding-left: 20px; padding-right: 20px;">
	
	
	<div >
		<a href="home.do" >Upload file/Manage file uploads</a>
	
		<h3>
			Files in database
		</h3>
	       
	    <table >
	      <thead>
	        <tr>
		      	<th>name</th>
		      	<th>description</th>
		    </tr>
	      </thead>
	      <tbody>
	      
	       <c:forEach var="entry" items="${ resultList }">
	       
	       	<tr data-id="${ entry.id }">
	       		<td><c:out value="${ entry.name }"></c:out>
	       		<td><c:out value="${ entry.description }"></c:out>
	       	</tr>
	       
	       </c:forEach>
	      
	      
	      </tbody>
	    </table>
	       
	</div>
	
</div>

  </body>
</html>
	       