
//   showFileOnHomePage.js




//////////////////////////////////

// JavaScript directive:   all variables have to be declared with "var", maybe other things

"use strict";



var UPDATE_STATUS_DELAY = 1500;  // in milliseconds

var UPDATE_PAGE_DATA_DELAY = 1500;  // in milliseconds

var SHOW_FILE_AFTER_SAVED_TAXONOMY_IDS_DELAY = 1500;  // in milliseconds


var TAXONOMY_ID_LOOKUP_TIMER_DELAY = 1300;  // in milliseconds


//var UPDATE_STATUS_DELAY = 5000;  // in milliseconds
//
//var UPDATE_PAGE_DATA_DELAY = 5000;  // in milliseconds
//
//var SHOW_FILE_AFTER_SAVED_TAXONOMY_IDS_DELAY = 5000;  // in milliseconds


var taxonomyIdLookupTimeoutTimerIdAttr = "taxonomyIdLookupTimeoutTimerId";



var updatePageDataNeeded = true;

var lastHeaderNoTaxonomyId = null;



var updateStatusTimerIds = {};

//  Cached taxonomy string by id
var taxonomyById = { };


//
//$(document).ready(function()  { 
//	
//		
//		initPage();
//		
//});
//	




function add_updateStatusTimerId( params ) {
	
	var updateStatusTimerId = params.updateStatusTimerId;
	var file_import_id = params.file_import_id;

	if ( ! updateStatusTimerIds[ file_import_id ] ) {
		
		updateStatusTimerIds[ file_import_id ] = {};
	}
	
	var updateStatusTimerIds_for__file_import_id = updateStatusTimerIds[ file_import_id ];
	
	updateStatusTimerIds_for__file_import_id[ updateStatusTimerId ] = updateStatusTimerId;
}



function remove_updateStatusTimerId( params ) {
	

	var updateStatusTimerId = params.updateStatusTimerId;
	var file_import_id = params.file_import_id;
	
//	var updateStatusTimerIdsKeysArray = Object.keys( updateStatusTimerIds );
	

	if (  updateStatusTimerIds[ file_import_id ] ) {

		var updateStatusTimerIds_for__file_import_id = updateStatusTimerIds[ file_import_id ];

		delete updateStatusTimerIds_for__file_import_id[ updateStatusTimerId ];
		
		var updateStatusTimerIdsKeysArray = Object.keys( updateStatusTimerIds_for__file_import_id );
		
		if ( updateStatusTimerIdsKeysArray.length === 0  ) {
			
			delete updateStatusTimerIds[ file_import_id ];
		}
	}	

//	var updateStatusTimerIdsKeysArray = Object.keys( updateStatusTimerIds );
	
//	var z = 0;
	
}




function cancel_all_updateStatusTimerId_for__file_import_id( params ) {
	
	var file_import_id = params.file_import_id;
	

	if ( updateStatusTimerIds[ file_import_id ] ) {

		var updateStatusTimerIdsPerFileImportId =  updateStatusTimerIds[ file_import_id ];

		var updateStatusTimerIdsPerFileImportIdKeysArray = Object.keys( updateStatusTimerIdsPerFileImportId );

		for ( var subIndex = 0; subIndex < updateStatusTimerIdsPerFileImportIdKeysArray.length; subIndex++ ) {

			var updateStatusTimerIdsPerFileImportIdProperty = updateStatusTimerIdsPerFileImportIdKeysArray[ subIndex ];

			var updateStatusTimerId = updateStatusTimerIdsPerFileImportId[ updateStatusTimerIdsPerFileImportIdProperty ];

			clearTimeout( updateStatusTimerId );
			
			remove_updateStatusTimerId( { file_import_id : file_import_id, updateStatusTimerId : updateStatusTimerIdsPerFileImportIdProperty } );
		}
	}
}

///////////

function cancelUpdateStatusTimers() {

	var updateStatusTimerIdsRootKeysArray = Object.keys( updateStatusTimerIds );

	for ( var index = 0; index < updateStatusTimerIdsRootKeysArray.length; index++ ) {
		
		var updateStatusTimerIdsRootProperty = updateStatusTimerIdsRootKeysArray[ index ];
		
		var updateStatusTimerIdsPerFileImportId =  updateStatusTimerIds[ updateStatusTimerIdsRootProperty ];

		var updateStatusTimerIdsPerFileImportIdKeysArray = Object.keys( updateStatusTimerIdsPerFileImportId );
		
		for ( var subIndex = 0; subIndex < updateStatusTimerIdsPerFileImportIdKeysArray.length; subIndex++ ) {

			var updateStatusTimerIdsPerFileImportIdProperty = updateStatusTimerIdsPerFileImportIdKeysArray[ subIndex ];

			var updateStatusTimerId = updateStatusTimerIdsPerFileImportId[ updateStatusTimerIdsPerFileImportIdProperty ];

			clearTimeout( updateStatusTimerId );
		}
	}
	
	updateStatusTimerIds = {};
}




/////////////

function getStatusBackgroundClass( statusData ) {

	var colorStatusClassNameToAdd = "";

	if ( statusData.failed ) {

		colorStatusClassNameToAdd = "failed-status";

	} else if ( statusData.userInputRequired ) {

		colorStatusClassNameToAdd = "user-input-required-status";

	} else if ( statusData.importComplete ) {

		colorStatusClassNameToAdd = "complete-status";

	} else if ( statusData.queued ) {

		colorStatusClassNameToAdd = "queued-status";

	} else if ( statusData.validationProcessing
			|| statusData.findTaxIdsProcessing 
			|| statusData.importProcessing ) {

		colorStatusClassNameToAdd = "started-status";

	}

	return colorStatusClassNameToAdd;

}


function showFileDetailsForIdInHiddenField(  ) {
	

	var $file_import_id = $("#file_import_id");
	
	var record_id = $file_import_id.val(  );
	
	if ( record_id === "" ) {
		
		return;
	}

	var $submitted_fasta_files = $("#submitted_fasta_files");
	
	var $file_entry_row_jq = $submitted_fasta_files.find(".file_entry_row_jq");
	
	$file_entry_row_jq.each( function( index ) { 
			
		var $file_entry_row_jq = $( this );
		
		var recordIdFromRow = $file_entry_row_jq.attr("data-record-id");

		if ( record_id === recordIdFromRow ) {
		
			showFileDetailsForRow( $file_entry_row_jq );
			
			return false;
		}
	});
}



function showFileDetails( clickedLinkElement ) {
	
	var $clickedLinkElement = $( clickedLinkElement );
	
	var $file_entry_row_jq = $clickedLinkElement;
	
	if ( ! $clickedLinkElement.hasClass( "file_entry_row_jq" ) ) {
	
		$file_entry_row_jq = $clickedLinkElement.closest(".file_entry_row_jq");
	}
	
	showFileDetailsForRow( $file_entry_row_jq );
}


function showFileDetailsForRow( $file_entry_row_jq ) {

	var $file_list_details_row = $file_entry_row_jq.next();
	
	if ( $file_list_details_row.length === 0 ) {
		throw '$file_list_details_row.length === 0';
	}
	
	if ( ! $file_list_details_row.hasClass("file_list_details_row_jq" ) ) {
		throw '! $file_list_details_row.hasClass("file_list_details_row_jq"';
	}
	
	
	var currentlyShowingDetails = $file_list_details_row.is(":visible");
	
	
	if ( currentlyShowingDetails ) {
		
		//  hide all details rows
		
		$(".file_list_details_row_jq").hide();
		
		
		$(".icon_expand_jq").show();
		$(".icon_collapse_jq").hide();

		return;
	}

	//  Update icons for expand and collapse

	$(".icon_expand_jq").show();
	$(".icon_collapse_jq").hide();

	$file_entry_row_jq.find(".icon_expand_jq").hide();
	$file_entry_row_jq.find(".icon_collapse_jq").show();
	
	//  Hide all details rows
	
	$(".file_list_details_row_jq").hide();
	
	
	//  Start processing for details for clicked row
	
	$file_list_details_row.show();
	
	var $file_list_details_holder_jq = $file_list_details_row.find(".file_list_details_holder_jq");
	
	if ( $file_list_details_holder_jq.length === 0 ) {
		throw '$file_list_details_holder_jq.length === 0';
	}
	
	//  Try to find single_file_info_block under details holder
	
	var $single_file_info_block__under__detailsHolder = $file_list_details_holder_jq.find("#single_file_info_block");
	
	if ( $single_file_info_block__under__detailsHolder.length > 0 ) {
		
		//  found so details block already under details row and populated
		
	} else {
	
		//  not found so move here and populate
	


		var record_id = $file_entry_row_jq.attr("data-record-id");

		var $file_import_id = $("#file_import_id");

		$file_import_id.val( record_id );

		var $single_file_info_block = $("#single_file_info_block");
		
		if ( $single_file_info_block.length === 0 ) {
			throw '$single_file_info_block.length === 0';
		}
		
		$single_file_info_block.detach();
		
		$single_file_info_block.appendTo( $file_list_details_holder_jq );
		
		var $prev_item_status_hidden_Field = $("#prev_item_status");
		
		$prev_item_status_hidden_Field.val( "" );  //  clear prev status value
		


		showFile();
	}
}


function is_numeric(str){
    return /^\d+$/.test(str);
}


/////////////

//function initPage() {
//	
//	
//	showFile();
//		
//
//}



///////////////

function processStatus( params ) {
	
	var statusData = params.statusData;
	var file_import_id = params.file_import_id;
//	var updateOtherFields = params.updateOtherFields;
	
	
	if ( statusData.noRecordFound ) {
		
		//  not found so remove from page

		var $item_status_cell = $("#item_status_id_" + file_import_id );

		if ( $item_status_cell.length === 0 ) {
			
			return;  //  no longer on the page
		}

		
		var $file_entry_row_jq =  $item_status_cell.closest(".file_entry_row_jq");
		
		var $file_list_details_row = $file_entry_row_jq.next();
		
		if ( $file_list_details_row.length === 0 ) {
			throw '$file_list_details_row.length === 0';
		}
		
		if ( ! $file_list_details_row.hasClass("file_list_details_row_jq" ) ) {
			throw '! $file_list_details_row.hasClass("file_list_details_row_jq"';
		}
		
		
		//  First move single_file_info_block div out from under 
		//     $file_list_details_row  before before emptying it 
		

		var $single_file_info_block = $file_list_details_row.find("#single_file_info_block");
		
		if ( $single_file_info_block.length > 0 ) {
			
			//  $single_file_info_block was found under $file_list_details_row so it needs 
			//    to be moved to single_file_info_block_holder before file_list_details_row is removed

			$single_file_info_block.detach();
			
			var $single_file_info_block_holder = $("#single_file_info_block_holder");

			$single_file_info_block.appendTo( $single_file_info_block_holder );
		}
		

		$file_list_details_row.remove();
		
		$file_entry_row_jq.remove();
		
		return;  // EARLY EXIT
	}
	


	if ( ! ( statusData.failed || statusData.importComplete || statusData.systemError
			
			|| statusData.status === 'queued for validation__' //  TODO  'queued for validation__' is temporary
			|| statusData.status ===  'user input required'    //  TODO  'user input required' is temporary
					
					) ) { 
		
		cancel_all_updateStatusTimerId_for__file_import_id( { file_import_id : file_import_id } );

		
		var updateStatusTimerIdForRemove = null;
		
		var updateStatusTimerId = setTimeout( function() {
			
			//  refresh status after delay
			
			remove_updateStatusTimerId( { updateStatusTimerId : updateStatusTimerIdForRemove, file_import_id : file_import_id } );
			
			updateStatus( {  file_import_id : file_import_id } ); 

		}, UPDATE_STATUS_DELAY );
		
		updateStatusTimerIdForRemove = updateStatusTimerId;
		
		add_updateStatusTimerId( { updateStatusTimerId : updateStatusTimerId, file_import_id : file_import_id } );
		
	}
	
	
	
	var $item_status_cell = $("#item_status_id_" + file_import_id );
	
	var $item_status_cell__Spans = $item_status_cell.find("span");
	
	if ( $item_status_cell__Spans.length === 0 ) {
		
		throw "no spans under call for file_import_id: " + file_import_id + ", #item_status_id_" + file_import_id;
	}

	var $item_status_holder_jq = null;

	
	$item_status_cell__Spans.each( function(index) {
		
		var $this = $( this );
		
		var data_item_status_holder = $this.attr( "data-item-status-holder" ) ;
		
		if ( data_item_status_holder === "true" ) {
			
			$item_status_holder_jq = $this;
			
			return false;  //  exit $item_status_cell__Spans.each(...)
		}
	});

	if ( $item_status_holder_jq === null ) {
		
		throw 'no spans with attribute "data-item-status-holder" under call for file_import_id: ' + file_import_id + ", #item_status_id_" + file_import_id;
	}
	
	
	$item_status_holder_jq.text( statusData.status );
	
	var colorStatusClassNameToAdd = getStatusBackgroundClass( statusData );

	//  replace all the classes to remove the existing status coloring
	$item_status_holder_jq.attr("class", colorStatusClassNameToAdd );

//	private String status;
//	
//	private boolean noRecordFound;
//	
//	private boolean queued;
//	private boolean validationProcessing;
//	private boolean findTaxIdsProcessing;
//	private boolean userInputRequired;
//	private boolean importProcessing;
//	private boolean failed;
//	private boolean importComplete;
//	private boolean systemError;
//	
//	private Integer currentProcessCount;
//	private Integer totalCount;
		

	var $file_import_id = $("#file_import_id");
	
	var current_file_import_id_from_field = $file_import_id.val();
	
	var user_entered_file_import_id = $("#user_entered_file_import_id").val();
	
	if ( user_entered_file_import_id === file_import_id ) {
		
		var z = 0;
	}
	
	var file_import_id_String = "";
	
	if ( file_import_id ) {
		
		file_import_id_String = file_import_id.toString();
	}
	

	//  If this row is currently being displayed, then update on status change

	if ( current_file_import_id_from_field === file_import_id_String ) {

		if ( statusData.currentProcessCount === undefined || statusData.currentProcessCount === null ) {

			$("#processing_count_block").hide();

		} else {


			$("#processing_count_block").show();
			
			var currentProcessCountString;
			var totalCountString;
			
			if ( statusData.currentProcessCount.toLocaleString ) {
				
				currentProcessCountString = statusData.currentProcessCount.toLocaleString();
				totalCountString = statusData.totalCount.toLocaleString();
				
			} else {
			
				currentProcessCountString = statusData.currentProcessCount.toString();
				totalCountString = statusData.totalCount.toString();
			}
			
			

			$("#current_processing_count").text( currentProcessCountString );
			$("#total_count").text( totalCountString );

		}

		if ( statusData.queued ) {

			$("#queued_status_block").show();
		} else {
			$("#queued_status_block").hide();
		}
		

		if ( statusData.importComplete ) {

			$("#import_complete_status_block").show();
		} else {
			$("#import_complete_status_block").hide();
		}
		
	}

	
	

	//  If this row is currently being displayed, then update on status change

	if ( current_file_import_id_from_field === file_import_id_String ) {
		
		if ( statusData.failed ) {

			showImportError( );
		}

		if ( statusData.userInputRequired ) {

			showNoTaxonomy( { statusData : statusData } );
		}
	}	
};


///////////////

function showFile( params ) {
	

	$("#no_tax_id_container").hide();
	$("#general_error_container").hide();
	
	lastHeaderNoTaxonomyId = null;
	
	
	var file_import_id = $("#file_import_id").val();

	var _URL = contextPathJSVar + "/services/submittedFiles/get";


	var ajaxRequestData = {

			id : file_import_id
	};


	$.ajax({
		type: "GET",
		url: _URL,
		dataType: "json",
		data: ajaxRequestData,  //  The data sent as params on the URL

		traditional: true,  //  Force traditional serialization of the data sent
		//   One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
		//   So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			showFileProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function showFileProcessResponse( params ) {
	
	var ajaxResponseData = params.ajaxResponseData;
	var ajaxRequestData = params.ajaxRequestData;
	
	if ( ajaxResponseData && ajaxResponseData.item ) {
		
		var file = ajaxResponseData.item;

		$("#filename").text( file.filename );
		
		var statusData = ajaxResponseData.statusData;

		
		processStatus( { statusData : statusData, file_import_id : ajaxRequestData.id,  updateOtherFields : true } );
		
	} else {
		
		noRecordFound();
	}
	
}

///////////////



///////////////

function updateStatus( params ) {

	var file_import_id = params.file_import_id;
		
		
		
//		var file_import_id = $("#file_import_id").val();
	
	
	
	

	var _URL = contextPathJSVar + "/services/submittedFiles/getStatus";


	var ajaxRequestData = {

			id : file_import_id
	};


	$.ajax({
		type: "GET",
		url: _URL,
		dataType: "json",
		data: ajaxRequestData,  //  The data sent as params on the URL

		traditional: true,  //  Force traditional serialization of the data sent
//		One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//		So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			updateStatusProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function updateStatusProcessResponse( params ) {

	var ajaxResponseData = params.ajaxResponseData;
	var ajaxRequestData = params.ajaxRequestData;
	
	if ( ajaxResponseData ) {

		var statusData = ajaxResponseData;
		
		processStatus( { statusData : statusData, file_import_id : ajaxRequestData.id , updateOtherFields : true } );

	} else {

		
	}

}




///////////////

function showNoTaxonomy( params ) {
	
	
	var statusData = params.statusData;
	

	var file_import_id = $("#file_import_id").val();
	
	
//	var $item_status_cell = $("#item_status_id_" + file_import_id );
//	
//	var $prev_item_status_jq = $item_status_cell.find(".prev_item_status_jq");
//	
//	var prev_item_status_val = $prev_item_status_jq.val();
	
	
	var $prev_item_status_hidden_Field = $("#prev_item_status");
	
	var prev_item_status_val = $prev_item_status_hidden_Field.val();
	
	
	if ( prev_item_status_val === statusData.status ) {

		// status has not changed so exit
		
		return;  //  EARLY EXIT
	}
	
	
	$prev_item_status_hidden_Field.val( statusData.status );

	
	

	$("#no_tax_id_container").show();
	
	$("#no_tax_id_loading").show();


//	select the table within the div
	var $no_tax_id_table__tbody = $("#no_tax_id_table tbody");
	
	$no_tax_id_table__tbody.empty();
	

	var _URL = contextPathJSVar + "/services/headerNoTaxonomy/list";


	var ajaxRequestData = {

			fastaImportTrackingId : file_import_id
	};

//	if ( params && params.idGreatThan ) {
//
//		ajaxRequestData.idGreatThan = params.idGreatThan;
//	}
	
	
	if ( lastHeaderNoTaxonomyId ) {
		
		ajaxRequestData.idGreatThan = lastHeaderNoTaxonomyId;
	}


	$.ajax({
		type: "GET",
		url: _URL,
		dataType: "json",
		data: ajaxRequestData,  //  The data sent as params on the URL

		traditional: true,  //  Force traditional serialization of the data sent
//		One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//		So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{
			
			showNoTaxonomyProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function showNoTaxonomyProcessResponse( params ) {

	var ajaxResponseData = params.ajaxResponseData;
	var ajaxRequestData = params.ajaxRequestData;
	
	if ( ajaxResponseData && ajaxResponseData.length > 0 ) {

		var noTaxonomyList = ajaxResponseData;

//		select the table within the div
		var $no_tax_id_table__tbody = $("#no_tax_id_table tbody");

//		Get the Handlebars template for an entry and compile it
		var $no_tax_id_entry_template = $("#no_tax_id_entry_template tbody");
		if ( $no_tax_id_entry_template.length === 0 ) {
			throw '$("#no_tax_id_entry_template tbody") not found';
		}
		var source = $no_tax_id_entry_template.html();
		
		if ( ! source ) {
			throw '$("#no_tax_id_entry_template tbody").html(); returned no html';
		}
		
		var template = Handlebars.compile(source);

//		process the file list
		for ( var noTaxonomyListIndex = 0; noTaxonomyListIndex < noTaxonomyList.length; noTaxonomyListIndex++ ) {

			var fileEntry = noTaxonomyList[ noTaxonomyListIndex ];
			
			lastHeaderNoTaxonomyId = fileEntry.id;  // assume sorted in ascending id

			var context = fileEntry;

			var html = template(context);

			var $no_tax_id_entry = $(html).appendTo( $no_tax_id_table__tbody );
			
			var $taxonomy_id_from_user_jq = $no_tax_id_entry.find(".taxonomy_id_from_user_jq");

			lookupOrganismForTaxonomyId( { $taxonomyFieldHTMLElement : $taxonomy_id_from_user_jq } );

			
		}


		$("#no_tax_id_loading").hide();
		
		$("#no_tax_id_table").show();

		$("#taxonomy_ids_saved").hide();
	}

}






///////////////

//   For "Failed"


function showImportError( params ) {

	var file_import_id = $("#file_import_id").val();
	

//	select the div
	var $general_errors = $("#general_errors");
	
	$general_errors.empty();
	
	

	var _URL = contextPathJSVar + "/services/generalImportError/list";


	var ajaxRequestData = {

			fastaImportTrackingId : file_import_id
	};


	$.ajax({
		type: "GET",
		url: _URL,
		dataType: "json",
		data: ajaxRequestData,  //  The data sent as params on the URL

		traditional: true,  //  Force traditional serialization of the data sent
//		One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//		So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			showImportErrorProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function showImportErrorProcessResponse( params ) {

	var ajaxResponseData = params.ajaxResponseData;

	if ( ajaxResponseData && ajaxResponseData.length > 0 ) {

		var generalImportErrorList = ajaxResponseData;

//		select the div
		var $general_errors = $("#general_errors");
		
		$general_errors.empty();

//		Get the Handlebars template for an entry and compile it
		var $general_error_entry_template = $("#general_error_entry_template");
		if ( $general_error_entry_template.length === 0 ) {
			throw '$("#general_error_entry_template") not found';
		}
		var source = $general_error_entry_template.html();
		
		if ( ! source ) {
			throw '$("#general_error_entry_template").html(); returned no html';
		}

		var template = Handlebars.compile(source);

//		process the file list
		for ( var generalImportErrorListIndex = 0; generalImportErrorListIndex < generalImportErrorList.length; generalImportErrorListIndex++ ) {

			var fileEntry = generalImportErrorList[ generalImportErrorListIndex ];

			var context = fileEntry;

			var html = template(context);

			var $general_error_entry = $(html).appendTo( $general_errors );



		}

		$("#general_error_container").show();
	}

}

/////////////////////////////

//   Save user entered taxonomy ids to database

function saveTaxonomyIds() {
	
	var dataErrorFound = false;
	
	var userProvidedTaxonomyIds = [];
	
	
	var $no_tax_id_table__tbody_tr = $("#no_tax_id_table tbody tr");
	
	$no_tax_id_table__tbody_tr.each( function(  index, element ) {
		
		var $row = $( this );
		
		var noTaxonomyIdRecordIdString = $row.attr("data-record-id");
		
		if ( noTaxonomyIdRecordIdString === "" ) {
			throw 'data-record-id === "" for index: ' + index;
		}
		var noTaxonomyIdRecordId = parseInt( noTaxonomyIdRecordIdString, 10 );

		if ( isNaN( noTaxonomyIdRecordId ) ) {
			throw 'data-record-id not integer for index: ' + index;
		}

		var header_line_number = $row.attr("data-header-line-number");
		
		var $taxonomy_id_from_user_jq = $row.find(".taxonomy_id_from_user_jq");
		
		var taxonomyIdString = $taxonomy_id_from_user_jq.val();
		
		if ( taxonomyIdString === "" ) {
			dataErrorFound = true;
			alert( "taxonomy id cannot be left blank for header line number: " + header_line_number );
			return false;
		}

		if ( ! is_numeric( taxonomyIdString ) ) {
			dataErrorFound = true;
			alert( "taxonomy id must be an integer for header line number: " + header_line_number );
			return false;
		}
		
		var taxonomyId = parseInt( taxonomyIdString, 10 );
		
		if ( isNaN( taxonomyId ) ) {
			dataErrorFound = true;
			alert( "taxonomy id must be an integer for header line number: " + header_line_number );
			return false;
		}

		var userProvidedTaxonomyIdsEntry = {
				
				noTaxonomyIdRecordId : noTaxonomyIdRecordId,
				taxonomyId : taxonomyId
		};
		
		userProvidedTaxonomyIds.push( userProvidedTaxonomyIdsEntry );
	} );
	
	if ( dataErrorFound ) {
		
		return;
	}
	

	saveTaxonomyIdsCallAJAX( {  userProvidedTaxonomyIds : userProvidedTaxonomyIds } );
	
}

/////////////////////////////

//Save user entered taxonomy ids to database

function saveTaxonomyIdsCallAJAX( params ) {
	
	
	var userProvidedTaxonomyIds = params.userProvidedTaxonomyIds;
	

	var file_import_id = $("#file_import_id").val();


	var ajaxRequestData = {
			
			fasta_import_tracking_id : file_import_id,
			userProvidedTaxonomyIds : userProvidedTaxonomyIds
	};
	
	var ajaxRequestDataString = JSON.stringify( ajaxRequestData ); 


	var _URL = contextPathJSVar + "/services/saveTaxonomyFromUser/save";



	$.ajax({
		type: "POST",
		url: _URL,
	    contentType: "application/json; charset=utf-8",
	    data: ajaxRequestDataString,  //  The data sent

		dataType: "json", // returned data type

		traditional: true,  //  Force traditional serialization of the data sent
//		One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//		So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			saveTaxonomyIdsCallAJAXProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}




function saveTaxonomyIdsCallAJAXProcessResponse( params ) {

//	var ajaxResponseData = params.ajaxResponseData;

	$("#taxonomy_ids_saved").show();

	setTimeout( function() {
		
		//  refresh after delay
		
		showFile();

	}, SHOW_FILE_AFTER_SAVED_TAXONOMY_IDS_DELAY );
	
	
}


///////////////////////

function taxonomyIdChanged( taxonomyFieldHTMLElement ) {
	
	var $taxonomyFieldHTMLElement = $( taxonomyFieldHTMLElement );
	
	saveTaxonomyIdToDBOnChange( { $taxonomyFieldHTMLElement : $taxonomyFieldHTMLElement } );
	
	lookupOrganismForTaxonomyId( { $taxonomyFieldHTMLElement : $taxonomyFieldHTMLElement } );
}


/////////////////////////////

//Save user entered taxonomy ids to database

function saveTaxonomyIdToDBOnChange( params ) {


	var $taxonomyFieldHTMLElement = params.$taxonomyFieldHTMLElement;
	var taxonomyFieldHTML_Value = $taxonomyFieldHTMLElement.val();

	var $taxonomy_entry_container_jq = $taxonomyFieldHTMLElement.closest(".taxonomy_entry_container_jq");
	
	var recordIdFromRow = $taxonomy_entry_container_jq.attr("data-record-id");

	

	var ajaxRequestData = {
			noTaxonomyIdRecordId : recordIdFromRow
	};
	
	if ( taxonomyFieldHTML_Value && taxonomyFieldHTML_Value !== "" && is_numeric( taxonomyFieldHTML_Value ) ) {
		
		var taxonomyId = parseInt( taxonomyFieldHTML_Value, 10 );
		
		ajaxRequestData.taxonomyId = taxonomyId;
	}
	

	var ajaxRequestDataString = JSON.stringify( ajaxRequestData ); 


	var _URL = contextPathJSVar + "/services/headerNoTaxonomy/update";



	$.ajax({
		type: "POST",
		url: _URL,
		contentType: "application/json; charset=utf-8",
		data: ajaxRequestDataString,  //  The data sent

		dataType: "json", // returned data type

		traditional: true,  //  Force traditional serialization of the data sent
//		One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//		So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			var z = 0;
		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}





///////////////////////


function taxonomyIdKeyUp( taxonomyFieldHTMLElement ) {
	
	var $taxonomyFieldHTMLElement = $( taxonomyFieldHTMLElement );
	

	var $taxonomy_entry_container_jq = $taxonomyFieldHTMLElement.closest(".taxonomy_entry_container_jq");
	
	var $taxonomy_id_organism_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_organism_jq");
	var $taxonomy_id_not_found_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_not_found_jq");
	
	$taxonomy_id_not_found_jq.hide();
	$taxonomy_id_organism_jq.text( "" );
		
			
			
	
	var taxonomyIdLookupTimeoutTimerId = $taxonomyFieldHTMLElement.attr( taxonomyIdLookupTimeoutTimerIdAttr );
	
	if ( taxonomyIdLookupTimeoutTimerId ) {
		
		clearTimeout( taxonomyIdLookupTimeoutTimerId );
		
		$taxonomyFieldHTMLElement.attr( taxonomyIdLookupTimeoutTimerIdAttr, null );
	}

	
	taxonomyIdLookupTimeoutTimerId = setTimeout( function() {
		
		lookupOrganismForTaxonomyId( { $taxonomyFieldHTMLElement : $taxonomyFieldHTMLElement } );
			
	}, TAXONOMY_ID_LOOKUP_TIMER_DELAY );
	
	
	return false;
}

/////////

function lookupOrganismForTaxonomyId( params ) {

	var $taxonomyFieldHTMLElement = params.$taxonomyFieldHTMLElement;

	$taxonomyFieldHTMLElement.attr( taxonomyIdLookupTimeoutTimerIdAttr, null );

	var taxonomyValue = $taxonomyFieldHTMLElement.val();

	if ( taxonomyValue === "" ) {

		updateSaveTaxonomyIdsButtonForTaxonomyIdChange();
		return;
	}

	if ( taxonomyById[ taxonomyValue ] ) {

		updateForOrganismForTaxonomyId(
				{
					$taxonomyFieldHTMLElement : $taxonomyFieldHTMLElement,
					taxonomyData : taxonomyById[ taxonomyValue ]
				}
		);

	} else {

		getOrganismForTaxonomyId( $taxonomyFieldHTMLElement );

	}

}


/////////

function updateForOrganismForTaxonomyId( params ) {



	var $taxonomyFieldHTMLElement = params.$taxonomyFieldHTMLElement;
	var taxonomyData = params.taxonomyData;

	var taxonomyId = params.taxonomyId;
	
	if ( taxonomyId ) {
		
		var taxonomyIdString = taxonomyId.toString();
	
		taxonomyById[ taxonomyIdString ] = taxonomyData;
	}

	var $taxonomy_entry_container_jq = $taxonomyFieldHTMLElement.closest(".taxonomy_entry_container_jq");
	
	var $taxonomy_id_organism_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_organism_jq");
	var $taxonomy_id_not_found_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_not_found_jq");
	
	
	if ( taxonomyData ) {
		
		if ( taxonomyData.scientificNameFound ) {
	
			$taxonomy_id_not_found_jq.hide();
			$taxonomy_id_organism_jq.text( taxonomyData.scientificName );
			
		} else {
			
			$taxonomy_id_not_found_jq.show();
			$taxonomy_id_organism_jq.empty();
		}
	}
	
	updateSaveTaxonomyIdsButtonForTaxonomyIdChange();
}



///////////////////////

function getOrganismForTaxonomyId( taxonomyFieldHTMLElement ) {

	
	var $taxonomyFieldHTMLElement = $( taxonomyFieldHTMLElement );
	
	var taxonomyIdString = $taxonomyFieldHTMLElement.val();
	
	
	var $taxonomy_entry_container_jq = $taxonomyFieldHTMLElement.closest(".taxonomy_entry_container_jq");
	
	var $taxonomy_id_organism_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_organism_jq");
	var $taxonomy_id_not_found_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_not_found_jq");
	var $taxonomy_id_must_be_integer_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_must_be_integer_jq");

	$taxonomy_id_not_found_jq.hide();
	$taxonomy_id_must_be_integer_jq.hide();
	$taxonomy_id_organism_jq.empty();
	


	if ( ! is_numeric( taxonomyIdString ) ) {
//		dataErrorFound = true;
//		alert( "taxonomy id must be an integer." );
		
		$taxonomy_id_must_be_integer_jq.show();

		return false;
	}
	
	var taxonomyId = parseInt( taxonomyIdString, 10 );
	
	if ( isNaN( taxonomyId ) ) {
//		dataErrorFound = true;
//		alert( "taxonomy id must be an integer." );
		
		$taxonomy_id_must_be_integer_jq.show();

		return false;
	}
	

	var _URL = contextPathJSVar + "/services/ncbiDataForTaxonomyId/get";


	var ajaxRequestData = {

			taxonomyId : taxonomyId
	};


	$.ajax({
		type: "GET",
		url: _URL,
		dataType: "json",
		data: ajaxRequestData,  //  The data sent as params on the URL

		traditional: true,  //  Force traditional serialization of the data sent
//		One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//		So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

		success: function(data)	{

			getOrganismForTaxonomyIdProcessResponse( 
					{ ajaxResponseData: data, 
						taxonomyId : taxonomyId,
						ajaxRequestData: ajaxRequestData,
						$taxonomyFieldHTMLElement : $taxonomyFieldHTMLElement
					});

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function getOrganismForTaxonomyIdProcessResponse( params ) {

	updateForOrganismForTaxonomyId( 
			{ $taxonomyFieldHTMLElement : params.$taxonomyFieldHTMLElement,
				taxonomyId : params.taxonomyId,
				taxonomyData : params.ajaxResponseData
			} );
	
}


function updateSaveTaxonomyIdsButtonForTaxonomyIdChange() {
	
	var allTaxonomyIdsFound = true;
	
	var $no_tax_id_table__tbody_tr = $("#no_tax_id_table tbody tr");
	
	$no_tax_id_table__tbody_tr.each( function(  index, element ) {
		
		var $taxonomy_entry_container_jq = $( this );

		var $taxonomy_id_organism_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_organism_jq");

		var taxonomy_id_organism_jq__text = $taxonomy_id_organism_jq.text();
		
		if ( taxonomy_id_organism_jq__text === "" ) {
			
			allTaxonomyIdsFound = false;
		}
		
	});
	
	if ( allTaxonomyIdsFound ) {
		
		$("#save_taxonomy_ids_button").prop( "disabled", false );
		$("#save_taxonomy_ids_button_disabled_overlay").hide();
		
	} else {
		
		$("#save_taxonomy_ids_button_disabled_overlay").show();
		$("#save_taxonomy_ids_button").prop( "disabled", true );
	}
}

