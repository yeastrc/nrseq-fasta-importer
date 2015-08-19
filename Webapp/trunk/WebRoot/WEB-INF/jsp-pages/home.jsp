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


<div style="padding-left: 20px; padding-right: 20px;">
	
	
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
	    		Submitted files
	    	</h3>
	    
	    	<div id="submitted_fasta_files" class="submitted-fasta-files-root" >
	    	
	    	
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
	    	  
	    	   <tr class=" file_entry_row_jq " data-record-id="{{item.id}}" onclick="showFileDetails(this)"
	    		   style="cursor: pointer;"
	    	   		>
	    	   		
	    	  	<td style="white-space: nowrap; padding-left: 10px;">
	    	  		<a href="javascript:" >details</a>
	    	  	</td>
	    	   	
	    	  	<td style="white-space: nowrap; padding-right: 20px;" class=" status_cell_jq " id="item_status_id_{{item.id}}">
	    	  		<span data-item-status-holder="true">
	    	  			{{item.status}}
	    	  		</span>
	    	  	</td>
	    	  	
	    	  	<td style="white-space: nowrap; padding-left: 10px;" >
	    	  		<span class=" filename_jq ">{{item.filename}}</span>
	    	  	</td>
	    	  	
	    	  	<td style=" padding-left: 10px;">
	    	  		{{item.description}}
	    	  	</td>
	    	   </tr>
	    	  </tbody>
	    	
	    	</table>
	    	
		</div>
	
	
	
	
		<%--  File Upload Details Overlay Background --%>
		
		<div id="file_upload_details_modal_dialog_overlay_background" class="file-upload-details-modal-dialog-overlay-background" style="display: none;"  >
		
		</div>
		
		<%--  File Upload Details Overlay Div --%>
		
		<div id="file_upload_details_overlay_div" class=" file-upload-details-overlay-div overlay-outer-div" style="display: none; "  >
		
		
			<div id="file_upload_details_overlay_header" class="file-upload-details-overlay-header" style="width:100%; " >
				<h1 id="file_upload_details_overlay_X_for_exit_overlay" class="file-upload-details-overlay-X-for-exit-overlay" >X</h1>
				<h1 id="file_upload_details_overlay_header_text" class="file-upload-details-overlay-header-text" >File Import Details</h1>
			</div>
			<div id="file_upload_details_overlay_body" class="file-upload-details-overlay-body" >
		
	    	
	      <div id="single_file_info_block" >
	      
	        <input type="hidden" id="prev_item_status" value="">     
	      
	
	    	  <div style="margin-bottom: 10px; font-size: 150%;">
	
				<%-- 
				<span style="font-weight: bold;" >
					File:  
				</span>
				--%>
				
				<span id="upload_details_overlay_filename" style="" ></span>
	
			  </div>
			      	  	
	    	  <div style="margin-bottom: 10px;">
	    	  
	    	  		<span id="upload_details_overlay_status"></span>

			  </div>
			  			      	  	
	    	  <div style="margin-bottom: 10px;">
	    	  		<a href="" id="mapping_details_link"
	    	  			data-root-url="taxonomyIdMappingDisplay.do?fastaImportTrackingId=">
	    	  			<%--  Add  target="_blank"  if displaying the file in the browser instead of downloading it --%>
	    	  			view taxonomy mapping details
	    	  		</a>
			  </div>
	           
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
	
			<br>
			
			
	    	<div id="general_error_container"  style="display: none;">
	    	
	    	  <div style="margin-bottom: 10px;">
	    		Errors encountered while processing the FASTA file:  
	    	  </div>
	    	  
		    	<div id="general_errors" >
		    	
		    	</div>
		    	
		    	
		    	
		    </div> <%--  END  <div id="general_error_container" --%>
		    
	
			<br>
			
			
	    	<div id="no_tax_id_container" class="file-import-details-root" style="display: none;">
	
	    	
	    	  <div style="margin-bottom: 10px;">
	    		Unable to determine the taxonomy id for the following headers in the file.
	    		Taxonomy ids are required for all the headers listed before the file can be imported.  
	    	  </div>
	
			  <div   id="no_tax_id_loading" >
			  
			  	Loading...
			  </div>
	
		    	<table id="no_tax_id_table"  style="display: none; width: 100%; border-width: 0px;">
		    	
		    	 <thead>
		    	  <tr >
		    	  <th style="border-width: 0px; padding-bottom: 10px; font-size: 120%">
		    	  	<%-- 
		    	  	taxonomy id
		    	  	--%>
		    	  </th>
		    	  <th style="border-width: 0px; padding-bottom: 10px; font-size: 120%">
		    	  	header
		    	  </th>
		  
		    	  <th style="border-width: 0px; padding-bottom: 10px; font-size: 120%">
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
	    	
	   </div>   <%--  END  <div id="file-upload-details-overlay-div" > --%>
	   
	    	
		    	
		   <%--  Templates  --%>
		    	
		    	<div id="general_error_entry_template"  style="display: none;">
		    	
		    	  <div>
		    	  		{{message}}
		    	  </div>
		    	</div>
	    	
	
		    	<table id="no_tax_id_entry_template"  style="display: none;">
		    	
		    	  <tbody>
		    	   <tr id="taxonomy_entry_container_{{item.id}}" class=" taxonomy_entry_container_jq  " 
		    	   		data-record-id="{{item.id}}" data-header-line-number="{{item.headerLineNumber}}">
		    	  	
		    	  	<td style="padding-top: 5px; padding-bottom: 5px; border-right-width: 0px;">
		    	  	
		    	  	  <div style="">
		    	  	  	<div style="font-weight: bold;">Taxonomy ID</div>
		    	  	  	
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
		    	  		
		    	  		<div class=" suggestions_holder_jq " style="display: none; margin-top: 5px;">
		    	  			Suggestions
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
		    	  	<td style="padding: 0px; ">
	
		    	  	  <table style="width: 100%;border-width: 0px;">
	
		    	  	   <tr>
		    	  	   	<td style="width: 1px; white-space: nowrap; font-weight: bold; border-left-width: 0px; border-top-width: 0px;">
		    	  	   		Name
		    	  	   	</td>
		    	  	   	<td style="padding-top: 5px; padding-bottom: 5px; font-weight: normal; border-right-width: 0px; border-top-width: 0px;">
		    	  	   		{{item.headerName}}
		    	  	   	</td>
		    	  	   </tr>
	
		    	  	   <tr>
		    	  	   	<td style="width: 1px;white-space: nowrap; font-weight: bold;border-left-width: 0px; ">
		    	  	   		Description
		    	  	   	</td>
		    	  	   	<td style="padding-top: 5px; padding-bottom: 5px; padding-left: 30px; border-right-width: 0px; ">
		    	  	   		{{item.headerDescription}}
		    	  	   	</td>
		    	  	   </tr>
		    	  	  
		    	  	   <tr>
		    	  	   	<td style="width: 1px;white-space: nowrap; font-weight: bold;border-left-width: 0px; ">
		    	  	   		Line Number
		    	  	   	</td>
		    	  	   	<td style="padding-top: 5px; padding-bottom: 5px;padding-left: 30px; border-right-width: 0px; ">
		    	  	   		{{item.headerLineNumber}}
		    	  	   	</td>
		    	  	   </tr>
		    	  	  
		    	  	   <tr>
		    	  	   	<td style="border-left-width: 0px; border-bottom-width: 0px;">
		    	  	   		
		    	  	   	</td>
		    	  	   	<td style="padding-top: 5px; padding-bottom: 5px; border-right-width: 0px; border-bottom-width: 0px; ">
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
		    	  	<td style="border-left-width: 0px;">
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
		    	
	
	    	
	    </div>

	</div>

	<script type="text/javascript" src="${ contextPath }/js/libs/handlebars-v2.0.0.min.js"></script>
	
	<script type="text/javascript" src="${ contextPath }/js/localDataCaches.js"></script>
	
	<script type="text/javascript" src="${ contextPath }/js/timersManagement.js"></script>

	<script type="text/javascript" src="${ contextPath }/js/listSubmittedFiles.js"></script>

	<script type="text/javascript" src="${ contextPath }/js/showFileOnHomePage.js"></script>


  </body>
</html>
