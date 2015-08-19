
//   listSubmittedFiles.js




//////////////////////////////////

// JavaScript directive:   all variables have to be declared with "var", maybe other things

"use strict";




$(document).ready(function()  { 
	
		
		initPage();
		
});
	




/////////////

function initPage() {
	
	
	updateFileList();
		
}

///////////////

function updateFileList( params ) {
	
	
	//  First move single_file_info_block div out from under 
	//     submitted_fasta_files div before before emptying it 
	
	var $single_file_info_block_holder = $("#single_file_info_block_holder");

	var $single_file_info_block = $("#single_file_info_block");
	
	$single_file_info_block.detach();
	
	$single_file_info_block.appendTo( $single_file_info_block_holder );

	
	
	$("#submitted_fasta_files").empty();
	
	var statusArray = null;
	var callAfterUpdate = null;
	
	if ( params ) {
		statusArray = params.statusArray;
		
		callAfterUpdate = params.callAfterUpdate;
	}
	
	
	if ( ! statusArray ) {
		
		statusArray = [];
	}

	var _URL = contextPathJSVar + "/services/submittedFiles/list";


	var ajaxRequestData = {

			status : statusArray
	};


	$.ajax({
		cache: false,
		type: "GET",
		url: _URL,
		dataType: "json",
		data: ajaxRequestData,  //  The data sent as params on the URL

		traditional: true,  //  Force traditional serialization of the data sent
		//   One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
		//   So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			updateFileListProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData, callAfterUpdate : callAfterUpdate });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function updateFileListProcessResponse( params ) {
	
	updateStatusTimerObject.cancelUpdateStatusTimers();
	
	var ajaxResponseData = params.ajaxResponseData;
	var callAfterUpdate = params.callAfterUpdate;
	
	if ( ajaxResponseData && ajaxResponseData.length > 0 ) {
		
		var fileList = ajaxResponseData;

		//   Copy the overall table to the display div
		
		var file_list_table_template_html = $("#file_list_table_template").html();
		$("#submitted_fasta_files").html( file_list_table_template_html );

		//  select the table within the div
		var $submitted_fasta_files_table = $("#submitted_fasta_files table");
		
		//  Get the Handlebars template for an entry and compile it
		var $file_list_entry_template = $("#file_list_entry_template tbody");
		var source = $file_list_entry_template.html();
		var template = Handlebars.compile(source);
		
		var $file_list_details_entry_template = $("#file_list_details_entry_template tbody");
		var file_list_details_entry_template__html = $file_list_details_entry_template.html();
		
		//  process the file list
		for ( var fileListIndex = 0; fileListIndex < fileList.length; fileListIndex++ ) {
			
			var fileEntry = fileList[ fileListIndex ];

			var context = fileEntry;

			var html = template(context);

			var $file_list_entry = $(html).appendTo( $submitted_fasta_files_table );
			
			var $file_list_details_entry = $(file_list_details_entry_template__html).appendTo( $submitted_fasta_files_table );

			var statusData = fileEntry.statusData;
			
			if ( ! ( statusData.importComplete || statusData.userInputRequired ) ) {

				//  Hide link for details since file may not exist or is incomplete
				
				var $mapping_details_link_jq =  $file_list_entry.find(".mapping_details_link_jq");
				$mapping_details_link_jq.hide();
			}

			processStatus( { statusData : fileEntry.statusData , file_import_id : fileEntry.item.id, updateOtherFields : false } );
				
		}
	}
	
//	if ( callAfterUpdate ) {
//		
//		callAfterUpdate();
//	
//	} 
	
	
	showFileDetailsForIdInHiddenField();
}


