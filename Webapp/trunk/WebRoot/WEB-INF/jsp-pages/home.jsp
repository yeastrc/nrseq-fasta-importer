<%@page import="org.yeastrc.nrseq_fasta_importer.constants.FileUploadConstants"%>
<%@ include file="/WEB-INF/jsp-includes/pageEncodingDirective.jsp" %>

<%@ include file="/WEB-INF/jsp-includes/strutsTaglibImport.jsp" %>
<%@ include file="/WEB-INF/jsp-includes/jstlTaglibImport.jsp" %>

<c:set var="headerAdditions" >

  <style>
  
    /*  Start classes for progress bar  */
    .progress_bar_container {
        width: 300px;
    }
    .progress_outer {
        border: 1px solid #000;
    }
    .progress {
        width: 00%;
        background: #DEDEDE;
        height: 20px;  
    }
    </style>

</c:set>

<%@ include file="/WEB-INF/jsp-includes/header_main.jsp" %>

    
<script type="text/javascript" >

  var maxFileUploadSize =           <%= FileUploadConstants.get_MAX_FILE_UPLOAD_SIZE() %>;
  var maxFileUploadSizeFormatted = "<%= FileUploadConstants.get_MAX_FILE_UPLOAD_SIZE_FORMATTED() %>";
</script>
    
<script type="text/javascript" src="${ contextPath }/js/handleServicesAJAXErrors.js"></script>
    
<script type="text/javascript" src="${ contextPath }/js/uploadFASTAFileUsingAJAX.js"></script>


<div >

	<h3>
		Update a new FASTA file
	</h3>
       
    <div >
		Choose a FASTA file to import:	<input type="file" id="fasta_file_field" onchange="progressBarClear();checkFile();"/>
		<div style="font-size: 80%;">
			(Max filesize: <%= FileUploadConstants.get_MAX_FILE_UPLOAD_SIZE_FORMATTED() %> bytes)
		</div>
	</div>
		

    <div style="margin-top: 12px;" >
		Enter a description: 
		<input type="text" id="fasta_file_description" maxlength="255" style="width: 220px;"
			onchange="descriptionChanged(this)" oninput="descriptionChanged(this)" onkeyup="descriptionChanged(this)" >
		<%-- 
		<span style="font-size: 80%">(required)</span>    
		--%>
	</div>
	
	<c:if test="${ send_email_configured }">

	  <div style="margin-top: 12px;" >        
		email address: <input type="text" id="email" maxlength="255" style="width: 220px;">
	  </div>         
    </c:if>
        
	<div style="margin-top: 12px;" >
	
	  <div style="position:relative;"> <%-- outer div to support overlay div when button disabled --%>
		<input type="button" value="Import File" style=""
			onclick="uploadFile()" disabled="disabled"  id="import_file_button">
					<%-- overlay div to provide tooltip for button --%>
		<div id="import_file_button_disabled_overlay"
					style="position:absolute;left:0;right:0;top:0;bottom:0;" 
					title="Import File. Enabled when file and description are populated." ></div>
			</div>
    </div>   

        <div id="uploading_message_and_progress" style="margin-top: 10px; display: none;" > <%-- style="display: none;" --%>
	        
        	Uploading file: <span id="uploaded_filename"></span>
        	
		    <div class="progress_bar_container"  style="margin-top: 10px;" >        
		        <div class="progress_outer">
		            <div id="progressBar" class="progress"></div>
		        </div>
		    </div>
	    </div>
	</div>
<br>
<br>
    <div class="" style=" " >
    
		<input type="hidden" id="file_import_id" value="<c:out value="${ param.id }"></c:out>">     
<%-- 
    	file_import_id:
		<input type="text" id="file_import_id" value="<c:out value="${ param.id }"></c:out>">     
    	user_entered_file_import_id:
		<input type="text" id="user_entered_file_import_id" value="">     
--%>    


    	<h3>
    		Existing Submitted files
    	</h3>
    
    	<div id="submitted_fasta_files" >
    	
    	
    	</div>
    	
    	<div id="file_list_table_template" style=" display: none;">
    	
    		<table style="text-align: left; width: 100% "  >
    		 <thead>
    		  <tr>
    		 	<th style="white-space: nowrap; width: 1px;">
    		 		
    		 	</th>
    		 	<th style="white-space: nowrap; width: 1px;">
    		 		status
    		 	</th>
    		 	<th style="white-space: nowrap; width: 1px;">
    		 		
    		 	</th>
    		 	<th style="white-space: nowrap; padding-left: 10px;">
    		 		filename
    		 	</th>
    		 	<th style="white-space: nowrap; padding-left: 10px;">
    		 		description
    		 	</th>
    		  </tr>
    		 </thead>
    		 <tbody>
    		 
    		 
    		 
    		 </tbody>
    		
    		
    		</table>
    	</div>
    	
    	<table id="file_list_entry_template"  style="display: none;">
    	
    	  <tbody>
    	  
    	   <tr class=" file_entry_row_jq " data-record-id="{{item.id}}"
    	   		
    	   		>
    	   		
    	   	<td >
    	   		<img src="${contextPath}/images/icon-expand-small.png"   onclick="showFileDetails(this)"
								class=" icon_expand_jq " style="cursor: pointer;">
    	   		<img src="${contextPath}/images/icon-collapse-small.png"   onclick="showFileDetails(this)"
								class=" icon_collapse_jq " style="display: none; cursor: pointer;">
    	   	</td>
    	   	
    	  	<td style="white-space: nowrap; padding-right: 20px;" class=" status_cell_jq " id="item_status_id_{{item.id}}">
    	  		<span data-item-status-holder="true">
    	  			{{item.status}}
    	  		</span>
    	  	</td>
    	  	
    	  	<td style="white-space: nowrap; padding-left: 10px;">
    	  		<a href="taxonomyIdMappingDisplay.do?fastaImportTrackingId={{item.id}}" class=" mapping_details_link_jq " >
    	  			<%--  Add  target="_blank"  if displaying the file in the browser instead of downloading it --%>
    	  			view taxonomy mapping details
    	  		</a>
    	  	</td>
    	  	
    	  	<td style="white-space: nowrap; padding-left: 10px;">
    	  		{{item.filename}}
    	  	</td>
    	  	
    	  	<td style=" padding-left: 10px;">
    	  		{{item.description}}
    	  	</td>
    	   </tr>
    	  </tbody>
    	
    	</table>
    	
	

    	<table id="file_list_details_entry_template"  style="display: none;">
    	
    	 <tbody>
    	  <tr  class=" file_list_details_row_jq " style="display: none;">
    	   <td  class=" file_list_details_holder_jq " colspan="5" style="padding-left: 10px;">  <%--  colspan matches # columns in main table --%>
    	   
    	   </td>
    	  </tr>
    	 </tbody>
    	</table>
    	
    	
    	<%--  The block used for display info on a single file.
    			
    			There is only one copy of this block and it is moved around to show the details for a particular file.
    	 --%>
    	
      <div id="single_file_info_block">
      
        <input type="hidden" id="prev_item_status" value="">     
      
           
      	<div id="import_complete_status_block"  style="display: none; margin-top: 15px;">
      	
      		This file is successfully imported
      	</div> 
      
      	<div id="queued_status_block"  style="display: none; margin-top: 15px;">
      	
      		This file is queued for processing
      	</div>

    	<div id="processing_count_block" style="display: none;">
    		Currently Processing <span id="current_processing_count"></span> 
    		of <span id="total_count" ></span> sequences.
    	</div>

<%-- 
	private int id;
	private String filename;
	private String description;
	private String status;
	private String sha1sum;
	private String tempFilename;
	private Date uploadDateTime;
	private Date lastUpdatedDateTime;
--%>


		<br>
		
		
    	<div id="general_error_container"  style="display: none;">
    	
    	  <div style="margin-bottom: 10px;">
    		Errors encountered while processing the FASTA file:  
    	  </div>
    	  
	    	<div id="general_errors" >
	    	
	    	</div>
	    	
	    	
	    	
	    </div> <%--  END  <div id="general_error_container" --%>
	    

		<br>
		
		
    	<div id="no_tax_id_container"  style="display: none;">

    	  <div style="margin-bottom: 10px;">
			File:  <span id="single_file_info_block__filename" ></span>

		  </div>
    	
    	  <div style="margin-bottom: 10px;">
    		Unable to determine the taxonomy id for the following headers in the file.
    		Taxonomy ids are required for all the headers listed before the file can be imported.  
    	  </div>

		  <div   id="no_tax_id_loading" >
		  
		  	Loading...
		  </div>

	    	<table id="no_tax_id_table"  style="display: none; width: 100%; border-width: 0px;">
	    	
	    	 <thead>
	    	  <tr>
	    	  <th>
	    	  	<%-- 
	    	  	taxonomy id
	    	  	--%>
	    	  </th>
	    	  <th>
	    	  	header
	    	  </th>
<%-- 
	    	  <th>
	    	  	header name
	    	  </th>
	    	  <th>
	    	  	header description
	    	  </th>
	    	  <th class="header-line-number">
	    	  	header line number
	    	  </th>
--%>	    	  
	    	  <th>
	    	  	message
	    	  </th>
	    	  </tr>
	    	 </thead>
	    	 
	    	 <tbody>
	    	 
	    	 </tbody>
	    	
	    	</table>
	    	
	    	
	    	
			<div style="margin-top: 10px; margin-bottom: 10px; position:relative;"> <%-- outer div to support overlay div when button disabled --%>
				<input onclick="saveTaxonomyIds()" id="save_taxonomy_ids_button"
					class="" type="button" value="Save Taxonomy Ids" 
					disabled>
					
					<%-- overlay div to provide tooltip for button --%>
				<div id="save_taxonomy_ids_button_disabled_overlay"
					style="position:absolute;left:0;right:0;top:0;bottom:0;" 
					title="Save Taxonomy Ids.  Enabled when all rows have valid taxonomy ids" ></div>
			</div>
	    	
	    	
	    	<div id="taxonomy_ids_saved" style="margin-top: 20px; display: none;">
	    		
	    		Taxonomy ids have been saved.  
	    		fasta file taxonomy ids will be checked for again and if all are found the file will be imported.
	    		
	    	</div>
	    	
	    </div> <%--  END  <div id="no_tax_id_container" --%>
	    
	  </div>  <%--  END  <div id="single_file_info_block" --%>
    	
    	
    	
	    	
	    	<div id="general_error_entry_template"  style="display: none;">
	    	
	    	  <div>
	    	  		{{message}}
	    	  </div>
	    	</div>
    	

	    	<table id="no_tax_id_entry_template"  style="display: none;">
	    	
	    	  <tbody>
	    	   <tr id="taxonomy_entry_container_{{item.id}}" class=" taxonomy_entry_container_jq  " 
	    	   		data-record-id="{{item.id}}" data-header-line-number="{{item.headerLineNumber}}">
	    	  	
	    	  	<td style="padding-top: 5px; padding-bottom: 5px;">
	    	  	
	    	  	  <div style="">
	    	  	  	<div style="font-weight: bold;">taxonomy id:</div>
	    	  	  	
	    	  		<input type="text" class=" taxonomy_id_from_user_jq " style="width: 80px;" maxlength="14" 
	    	  			value="{{item.userAssignedTaxId}}"
	    	  			onchange="taxonomyIdChangedUserInputInTextField(this)" oninput="taxonomyIdChangedUserInputInTextField(this)" onkeyup="taxonomyIdChangedUserInputInTextField(this)" > <%-- --%>
	    	  		<span class="taxonomy_id_organism_jq"></span>
	    	  		
	    	  		<span class=" taxonomy_id_must_be_integer_jq " style="display: none; color: red;">
	    	  			Taxonomy id must be an integer.</span>
	    	  		<span class="taxonomy_id_not_found_jq" style="display: none; color: red;">Organism not found for taxonomy id</span>
	    	  	  </div>
	    	  	  
	    	  	  <div style="margin-top: 3px;">
	    	  	  	<%-- 
	    	  			<select class=" taxonomy_suggestion_jq "  style="display: none; " onchange="taxonomySuggestionChanged(this)" >
	    	  				<option value="">Suggestions</option>	
	    	  			</select>
	    	  		--%>
	    	  		
	    	  		<div class=" suggestions_holder_jq " style="display: none;">
	    	  			Suggestions:
	    	  			<div class=" suggestions_group_jq ">
	    	  			
	    	  			</div>
	    	  		
	    	  		</div>
<%-- 	    	  		
					<div style="margin-top: 3px;">
	    	  	  		<a href="blastSequence.do?id={{item.id}}" target="_blank" >blast sequence</a>
	    	  	  	</div>
--%>	    	  	  	
	    	  	  </div>
	    	  	</td>
	    	  	<td style="padding: 0px;border-width: 0px; ">

	    	  	  <table style="width: 100%">

	    	  	   <tr>
	    	  	   	<td style="width: 1px; white-space: nowrap; font-weight: bold;">
	    	  	   		Name:
	    	  	   	</td>
	    	  	   	<td style="padding-top: 5px; padding-bottom: 5px; font-weight: normal;">
	    	  	   		{{item.headerName}}
	    	  	   	</td>
	    	  	   </tr>

	    	  	   <tr>
	    	  	   	<td style="border-top-width: 0px; width: 1px;white-space: nowrap; font-weight: bold;">
	    	  	   		Description:
	    	  	   	</td>
	    	  	   	<td style="padding-top: 5px; padding-bottom: 5px; padding-left: 30px;">
	    	  	   		{{item.headerDescription}}
	    	  	   	</td>
	    	  	   </tr>
	    	  	  
	    	  	   <tr>
	    	  	   	<td style="width: 1px;white-space: nowrap; font-weight: bold;">
	    	  	   		Line Number:
	    	  	   	</td>
	    	  	   	<td style="padding-top: 5px; padding-bottom: 5px;padding-left: 30px;">
	    	  	   		{{item.headerLineNumber}}
	    	  	   	</td>
	    	  	   </tr>
	    	  	  
	    	  	   <tr>
	    	  	   	<td>
	    	  	   		
	    	  	   	</td>
	    	  	   	<td style="padding-top: 5px; padding-bottom: 5px;">
	    	  	   		<a href="blastSequence.do?id={{item.id}}" target="_blank" >blast sequence</a>
	    	  	   	</td>
	    	  	   </tr>
	    	  	   	    	
	    	  	  
	    	  	  </table>
	    	  	
	    	  	
	    	  	</td>

<%-- 
	    	  	<td>
	    	  		{{item.headerName}}
	    	  	</td>
	    	  	<td>
	    	  		{{item.headerDescription}}
	    	  	</td>
	    	  	<td class="header-line-number">
	    	  		{{item.headerLineNumber}}
	    	  	</td>
--%>	    	  	
	    	  	<td>
	    	  		{{item.message}}
	    	  	</td>
	    	   </tr>
	    	  </tbody>
	    	
	    	</table>
	    	
	    	<div id="suggestion_button_template" style="display: none;">
	    	
	    		<div class=" suggestion_button_container_jq " style="margin-top: 2px; margin-bottom: 2px;" >
	    			<input type="button" value="{{taxonomyId}}" class=" suggestion_button_jq " data-taxonomy-id="{{taxonomyId}}" 
	    				onclick="taxonomySuggestionChosen(this)" >
	    		</div>
	    	
	    	</div>
	    	

	    	<table id="spacer_row_template"  style="display: none;">
	    	
	    	  <tbody>
	    	   <tr >
	    	  	<td colspan="3" style="height: 10px; border-width: 0px;">
	    	  		&nbsp;
	    	  	</td>
	    	   </tr>
	    	  </tbody>
	    	</table>    	
	    	
	    	
	    <div id="single_file_info_block_holder" style="display: none;">
	    
	    </div>    	
    	
    </div>

	<script type="text/javascript" src="${ contextPath }/js/libs/handlebars-v2.0.0.min.js"></script>
	
	<script type="text/javascript" src="${ contextPath }/js/localDataCaches.js"></script>
	
	<script type="text/javascript" src="${ contextPath }/js/timersManagement.js"></script>

	<script type="text/javascript" src="${ contextPath }/js/listSubmittedFiles.js"></script>

	<script type="text/javascript" src="${ contextPath }/js/showFileOnHomePage.js"></script>


  </body>
</html>
