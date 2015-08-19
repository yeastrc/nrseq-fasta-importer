
//   showFileOnHomePage.js




//////////////////////////////////

// JavaScript directive:   all variables have to be declared with "var", maybe other things

"use strict";



var UPDATE_STATUS_DELAY = 1500;  // in milliseconds

var UPDATE_PAGE_DATA_DELAY = 1500;  // in milliseconds

var SHOW_FILE_AFTER_SAVED_TAXONOMY_IDS_DELAY = 1500;  // in milliseconds


var TAXONOMY_ID_LOOKUP_TIMER_DELAY = 500;  // in milliseconds  was 1300


//var UPDATE_STATUS_DELAY = 5000;  // in milliseconds
//
//var UPDATE_PAGE_DATA_DELAY = 5000;  // in milliseconds
//
//var SHOW_FILE_AFTER_SAVED_TAXONOMY_IDS_DELAY = 5000;  // in milliseconds


var taxonomyIdLookupTimeoutTimerIdAttr = "taxonomyIdLookupTimeoutTimerId";



var updatePageDataNeeded = true;

var lastHeaderNoTaxonomyId = null;





$(document).ready(function()  { 
	
	initPage_showFileOnHomePage();
		
		
});
	


/////////////

function initPage_showFileOnHomePage() {


	attachFileImportDetailsOverlayClickHandlers();


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

	} else if ( statusData.systemError ) {
		
		colorStatusClassNameToAdd = "system-error-status";
	}

	return colorStatusClassNameToAdd;

}


///////

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
			
			// TODO   Scroll window so row is visible in viewport
			
//			var rowTop = $file_entry_row_jq.offset().top;
//			
//			var viewportHeight = $( window ).height();
//			
//			var scrollTop = $(window).scrollTop();
//			
//			var minScrollTopToShowRow = rowTop - viewportHeight
//			
//			if (  )
			
			
		
			showFileDetailsForRow( { $file_entry_row_jq : $file_entry_row_jq } );
			
			return false;
		}
	});
}


///////

function showFileDetails( clickedLinkElement ) {
	
	var $clickedLinkElement = $( clickedLinkElement );
	
	var $file_entry_row_jq = $clickedLinkElement;
	
	if ( ! $clickedLinkElement.hasClass( "file_entry_row_jq" ) ) {
	
		$file_entry_row_jq = $clickedLinkElement.closest(".file_entry_row_jq");
	}
	
	showFileDetailsForRow(  { $file_entry_row_jq : $file_entry_row_jq, rowClicked : true } );
}

///////

function showFileDetailsForRow( params ) {
	
	var $file_entry_row_jq = params.$file_entry_row_jq;
	var rowClicked = params.rowClicked;
	

	//  Code to mark the last clicked row with a different color.
	$(".file_entry_row_jq").removeClass("file-details-row-clicked");
	$file_entry_row_jq.addClass("file-details-row-clicked");

	if ( rowClicked ) {
		
//		$file_entry_row_jq.scrollTop( file_entry_row_ScrollTop );
//		$(window).scrollTop( currentScrollTop );
	}
	

	//  Populate overlay
	
	
	var record_id = $file_entry_row_jq.attr("data-record-id");

	var $file_import_id = $("#file_import_id");

	$file_import_id.val( record_id );

	var $prev_item_status_hidden_Field = $("#prev_item_status");

	$prev_item_status_hidden_Field.val( "" );  //  clear prev status value
	
	var $filename_jq = $file_entry_row_jq.find(".filename_jq");
	
	var filename = $filename_jq.text();

	$("#upload_details_overlay_filename").text( filename );
	

	var $mapping_details_link = $("#mapping_details_link");
	
	var mapping_details_link_root_URL = $mapping_details_link.attr("data-root-url");
	
	var mapping_details_link_URL = mapping_details_link_root_URL + record_id;
	
	$mapping_details_link.attr( "href", mapping_details_link_URL );
	
	$mapping_details_link.hide();
	

	

	var $file_upload_details_overlay_div = $("#file_upload_details_overlay_div");


	//  Adjust the overlay positon to be within the viewport

	var scrollTopWindow = $(window).scrollTop();

	if ( scrollTopWindow > 0 ) {

		//  User has scrolled down

//		var overlayBackgroundDivTop = $("#lorikeet-modal-dialog-overlay-background").offset().top;
//
//		var overlayTop = scrollTopWindow - overlayBackgroundDivTop + 10;
		
		//  Changed since overlay-background is position fixed

		var overlayTop = scrollTopWindow + 10;

		$file_upload_details_overlay_div.css( { top: overlayTop + "px" } );

	} else {

		$file_upload_details_overlay_div.css( { top: "10px" } );
	}


	
	var $file_upload_details_modal_dialog_overlay_background = $("#file_upload_details_modal_dialog_overlay_background");
	
	$file_upload_details_modal_dialog_overlay_background.show();
	
	$file_upload_details_overlay_div.show();
	
	
	showFile();

}


function is_numeric(str){
    return /^\d+$/.test(str);
}


///////////////

function processStatus( params ) {
	
	var statusData = params.statusData;
	var file_import_id = params.file_import_id;
//	var updateOtherFields = params.updateOtherFields;
	
	var file_import_id_String = "";
	
	if ( file_import_id ) {
		
		file_import_id_String = file_import_id.toString();
	}
	
	

	var $file_import_id = $("#file_import_id");
	
	var current_file_import_id_from_field = $file_import_id.val();

	
	
	if ( statusData.noRecordFound ) {
		
		//  not found so remove from page

		var $item_status_cell = $("#item_status_id_" + file_import_id );

		if ( $item_status_cell.length === 0 ) {
			
			return;  //  no longer on the page
		}

		var $file_entry_row_jq =  $item_status_cell.closest(".file_entry_row_jq");
		
		$file_entry_row_jq.remove();
		


		//  If this row is currently being displayed in the overlay, then close the overlay

		if ( current_file_import_id_from_field === file_import_id_String ) {
			
			closeFileImportDetailsOverlay();
		}
		
		return;  // EARLY EXIT
	}


	if ( ! ( statusData.failed || statusData.importComplete || statusData.systemError
			
//			|| statusData.status === 'queued for validation__' //  TODO  'queued for validation__' is temporary
//			|| statusData.status ===  'user input required'    //  TODO  'user input required' is temporary
					
					) ) { 
		
		//  Update the status on a timer 
		
		updateStatusTimerObject.cancel_all_updateStatusTimerId_for__file_import_id( { file_import_id : file_import_id } );

		
		var updateStatusTimerIdForRemove = null;
		
		var updateStatusTimerId = setTimeout( function() {
			
			//  refresh status after delay
			
			updateStatusTimerObject.remove_updateStatusTimerId( { updateStatusTimerId : updateStatusTimerIdForRemove, file_import_id : file_import_id } );
			
			updateStatus( {  file_import_id : file_import_id } ); 

		}, UPDATE_STATUS_DELAY );
		
		updateStatusTimerIdForRemove = updateStatusTimerId;
		
		updateStatusTimerObject.add_updateStatusTimerId( { updateStatusTimerId : updateStatusTimerId, file_import_id : file_import_id } );
		
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
		

	
	var user_entered_file_import_id = $("#user_entered_file_import_id").val();
	
	if ( user_entered_file_import_id === file_import_id ) {
		
		var z = 0;
	}

	

	//  If this row is currently being displayed, then update on status change

	if ( current_file_import_id_from_field === file_import_id_String ) {
		
		//  update status
		
		var $upload_details_overlay_status = $("#upload_details_overlay_status");
		
		$upload_details_overlay_status.text( statusData.status );
		
		var colorStatusClassNameToAdd = getStatusBackgroundClass( statusData );

		//  replace all the classes to remove the existing status coloring
		$upload_details_overlay_status.attr("class", colorStatusClassNameToAdd );
		
		
		
		var $mapping_details_link = $("#mapping_details_link");
		
		if ( statusData.userInputRequired || statusData.importComplete || statusData.importProcessing ) {
			
			$mapping_details_link.show();
		} else {
			
			$mapping_details_link.hide();
		}
		
		

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
		cache: false,
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
		cache: false,
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
		cache: false,
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

///////

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
		
		
		//  Get HTML template for spacer row
		
		
		var $spacer_row_template = $("#spacer_row_template tbody");
		if ( $spacer_row_template.length === 0 ) {
			throw '$("#spacer_row_template") not found';
		}
		var spacer_row_template_html = $spacer_row_template.html();
				

//		Get the Handlebars template for an suggestion button and compile it
		var $suggestion_button_template = $("#suggestion_button_template");
		if ( $suggestion_button_template.length === 0 ) {
			throw '$("#suggestion_button_template") not found';
		}
		var suggestion_button_template__source = $suggestion_button_template.html();
		
		if ( ! suggestion_button_template__source ) {
			throw '$("#suggestion_button_template").html(); returned no html';
		}
		
		var suggestion_button_template__template = Handlebars.compile(suggestion_button_template__source);

//		var $spacer_row_template_html = 
//		$(spacer_row_template_html).appendTo( $no_tax_id_table__tbody );
		
//		process the file list
		for ( var noTaxonomyListIndex = 0; noTaxonomyListIndex < noTaxonomyList.length; noTaxonomyListIndex++ ) {

			var fileEntry = noTaxonomyList[ noTaxonomyListIndex ];
			
			lastHeaderNoTaxonomyId = fileEntry.item.id;  // assume sorted in ascending id

			var context = fileEntry;

			var html = template(context);

			var $no_tax_id_entry = $(html).appendTo( $no_tax_id_table__tbody );
			
			//  Update suggestion select if suggestions are provided
			
			if ( fileEntry.suggestions && fileEntry.suggestions.length > 0 ) {
				
				var $suggestions_holder_jq = $no_tax_id_entry.find(".suggestions_holder_jq");
				$suggestions_holder_jq.show();
				
				var $suggestions_group_jq = $no_tax_id_entry.find(".suggestions_group_jq");
				
				for ( var suggestionsIndex = 0; suggestionsIndex < fileEntry.suggestions.length; suggestionsIndex++ ) {

					var suggestion = fileEntry.suggestions[ suggestionsIndex ];


					var suggestionButtonContext = suggestion;

					var suggestion_button_html = suggestion_button_template__template( suggestionButtonContext );

					var $suggestion_button_container =
						$( suggestion_button_html ).appendTo( $suggestions_group_jq );



					updateSuggestionButtonWithOrganismName( { $suggestion_button_container : $suggestion_button_container, suggestion : suggestion } );

				}

				
//				var $taxonomy_suggestion_jq = $no_tax_id_entry.find(".taxonomy_suggestion_jq");
//
//				$taxonomy_suggestion_jq.show();
//				
//				for ( var suggestionsIndex = 0; suggestionsIndex < fileEntry.suggestions.length; suggestionsIndex++ ) {
//					
//					var suggestion = fileEntry.suggestions[ suggestionsIndex ];
//					
//					var html = '<option value="' + suggestion.taxonomyId + '" >' +
//						suggestion.taxonomyId +
////						" : " + 
////						suggestion.accessionString +
//						'</option>';
//					
//					$( html ).appendTo( $taxonomy_suggestion_jq );
//					
//				}
			}
			
			if ( noTaxonomyListIndex < noTaxonomyList.length - 1 ) {

//				var $spacer_row_template_html = 
					$(spacer_row_template_html).appendTo( $no_tax_id_table__tbody );
			}
			
			var $taxonomy_id_from_user_jq = $no_tax_id_entry.find(".taxonomy_id_from_user_jq");

			lookupOrganismForTaxonomyId( { $taxonomyFieldHTMLElement : $taxonomy_id_from_user_jq } );

			
		}


		$("#no_tax_id_loading").hide();
		
		$("#no_tax_id_table").show();

		$("#taxonomy_ids_saved").hide();
	}

}



///////////////

function updateSuggestionButtonWithOrganismName( params ) {
	
	var $suggestion_button_container = params.$suggestion_button_container;
	
	var suggestion = params.suggestion;
	
	
	//  Update the button text with the organism name
	

	var callbackFcn = function( params ) {
		
		var taxonomyData = params.taxonomyData;
		
		var scientificName = taxonomyData.scientificName;
		var scientificNameFound = taxonomyData.scientificNameFound;

		var $suggestion_button_jq = $suggestion_button_container.find(".suggestion_button_jq");
		
		

		var taxonomyId = $suggestion_button_jq.attr("data-taxonomy-id");
		
		var newButtonValue = taxonomyId + " : " + scientificName;
		
		if ( ! scientificNameFound ) {
			
			newButtonValue = taxonomyId + " : Unable to determine organism";
		}
		
		$suggestion_button_jq.attr("value", newButtonValue );
	};
	
	
	ncbiDescriptorFromTaxonomyIdObject.get_NCBIDescriptorForTaxonomyId( { taxonomyId : suggestion.taxonomyId, callbackFcn : callbackFcn } );
	
	
	
	
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
		cache: false,
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

//  For <select>

function taxonomySuggestionChanged( taxonomySuggestionFieldHTMLElement ) {
	
	var $taxonomySuggestionFieldHTMLElement = $( taxonomySuggestionFieldHTMLElement );
	
	var taxonomySuggestionFieldValue = $taxonomySuggestionFieldHTMLElement.val();
	
	var $taxonomy_entry_container_jq = $taxonomySuggestionFieldHTMLElement.closest(".taxonomy_entry_container_jq");
	
	var $taxonomy_id_from_user_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_from_user_jq");
	
	$taxonomy_id_from_user_jq.val( taxonomySuggestionFieldValue );
	
	var taxonomy_id_from_userHTMLElement = $taxonomy_id_from_user_jq[0];
	
	taxonomyIdChanged( taxonomy_id_from_userHTMLElement );
}



///////////////////////

//   For Button

function taxonomySuggestionChosen( taxonomySuggestionFieldHTMLElement ) {

	var $taxonomySuggestionFieldHTMLElement = $( taxonomySuggestionFieldHTMLElement );

	var taxonomySuggestionFieldValue = $taxonomySuggestionFieldHTMLElement.attr("data-taxonomy-id");

	var $taxonomy_entry_container_jq = $taxonomySuggestionFieldHTMLElement.closest(".taxonomy_entry_container_jq");

	var $taxonomy_id_from_user_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_from_user_jq");

	$taxonomy_id_from_user_jq.val( taxonomySuggestionFieldValue );

	var taxonomy_id_from_userHTMLElement = $taxonomy_id_from_user_jq[0];

	taxonomyIdChanged( taxonomy_id_from_userHTMLElement );
}





///////////////////////



function taxonomyIdChangedUserInputInTextField( taxonomyFieldHTMLElement ) {
	
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
		
		//  Delay the actual lookup of the Organism for the Taxonomy id
		
		var taxonomyIdLookupTimeoutTimerId = $taxonomyFieldHTMLElement.attr( taxonomyIdLookupTimeoutTimerIdAttr );
		
		if ( taxonomyIdLookupTimeoutTimerId ) {
			
			clearTimeout( taxonomyIdLookupTimeoutTimerId );
			
			$taxonomyFieldHTMLElement.attr( taxonomyIdLookupTimeoutTimerIdAttr, null );
		}
		
		taxonomyIdChanged( taxonomyFieldHTMLElement );
			
	}, TAXONOMY_ID_LOOKUP_TIMER_DELAY );
	
	$taxonomyFieldHTMLElement.attr( taxonomyIdLookupTimeoutTimerIdAttr, taxonomyIdLookupTimeoutTimerId );

	
	return false;
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



/////////

function lookupOrganismForTaxonomyId( params ) {

	var $taxonomyFieldHTMLElement = params.$taxonomyFieldHTMLElement;

	var taxonomyIdString = $taxonomyFieldHTMLElement.val();

	if ( taxonomyIdString === "" ) {
		
		var $taxonomy_entry_container_jq = $taxonomyFieldHTMLElement.closest(".taxonomy_entry_container_jq");

		var $taxonomy_id_organism_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_organism_jq");
		var $taxonomy_id_must_be_integer_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_must_be_integer_jq");
		var $taxonomy_id_not_found_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_not_found_jq");
		
		$taxonomy_id_organism_jq.text("");
		$taxonomy_id_must_be_integer_jq.hide();
		$taxonomy_id_not_found_jq.hide();
		
		updateSaveTaxonomyIdsButtonForTaxonomyIdChange();
		
		return;  //  EARLY EXIT
	}

	
	var $taxonomy_entry_container_jq = $taxonomyFieldHTMLElement.closest(".taxonomy_entry_container_jq");
	
	var $taxonomy_id_organism_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_organism_jq");
	var $taxonomy_id_not_found_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_not_found_jq");
	var $taxonomy_id_must_be_integer_jq = $taxonomy_entry_container_jq.find(".taxonomy_id_must_be_integer_jq");

	$taxonomy_id_not_found_jq.hide();
	$taxonomy_id_must_be_integer_jq.hide();
	$taxonomy_id_organism_jq.empty();
	


	if ( ! is_numeric( taxonomyIdString ) ) {

		$taxonomy_id_must_be_integer_jq.show();

		return false;  //  EARLY EXIT
	}
	
	var taxonomyId = parseInt( taxonomyIdString, 10 );
	
	if ( isNaN( taxonomyId ) ) {
		
		$taxonomy_id_must_be_integer_jq.show();

		return false;  //  EARLY EXIT
	}
	
	
	var callbackFcn = function( params ) {
		
		var taxonomyData = params.taxonomyData;
		
//		var scientificName = taxonomyData.scientificName;
//		var scientificNameFound = taxonomyData.scientificNameFound;
		
		updateForOrganismForTaxonomyId(
				{
					$taxonomyFieldHTMLElement : $taxonomyFieldHTMLElement,
					taxonomyData : taxonomyData
				}
		);
	};
	
	
	ncbiDescriptorFromTaxonomyIdObject.get_NCBIDescriptorForTaxonomyId( { taxonomyId : taxonomyId, callbackFcn : callbackFcn } );
	
}


/////////

function updateForOrganismForTaxonomyId( params ) {



	var $taxonomyFieldHTMLElement = params.$taxonomyFieldHTMLElement;
	var taxonomyData = params.taxonomyData;

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


///////////


function updateSaveTaxonomyIdsButtonForTaxonomyIdChange() {
	
	var allTaxonomyIdsFound = true;
	
	var $no_tax_id_table__tbody_tr = $("#no_tax_id_table tbody tr.taxonomy_entry_container_jq");
	
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




///  Overlay general processing

var closeFileImportDetailsOverlay = function (  ) {

	$("#file_upload_details_modal_dialog_overlay_background").hide();
	$(".file-upload-details-overlay-div").hide();

};

///////////////////////////////////////////////////////////////////////////

///  Attach Overlay Click handlers


var attachFileImportDetailsOverlayClickHandlers = function (  ) {

	var $view_spectra_overlay_X_for_exit_overlay = $(".file-upload-details-overlay-X-for-exit-overlay");
	
	$view_spectra_overlay_X_for_exit_overlay.click( function( eventObject ) {

		closeFileImportDetailsOverlay();
	} );

	$("#file_upload_details_modal_dialog_overlay_background").click( function( eventObject ) {

		closeFileImportDetailsOverlay();
	} );

//	$(".file-upload-details-overlay-div").click( function( eventObject ) {
//
//		closeFileImportDetailsOverlay();
//	} );

//	$(".error-message-ok-button").click( function( eventObject ) {
//
//		closeFileImportDetailsOverlay();
//	} );

	
	
	
//	$("#file-upload-details-overlay-div").click( function( eventObject ) {
//
//		return false;
//	} );

};
