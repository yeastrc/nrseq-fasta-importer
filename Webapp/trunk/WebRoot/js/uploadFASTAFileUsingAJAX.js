


//////////////////////////////////

// JavaScript directive:   all variables have to be declared with "var", maybe other things

"use strict";

//   On the page:

//  var maxFileUploadSize = <%= FileUploadConstants.MAX_FILE_UPLOAD_SIZE %>;
//  var maxFileUploadSizeFormatted = "<%= FileUploadConstants.MAX_FILE_UPLOAD_SIZE_FORMATTED %>";




function descriptionChanged() {
	
//	enableDisableImportFileButton();
}


function checkFile() {
	
	
	if ( ! checkFileSize() ) {
		
		enableDisableImportFileButton();
		return;
	}
	
	
	var callbackIfFilenameNotInDB = function() {
		
		enableDisableImportFileButton();
		
		$("#fasta_file_description").focus();
		
	};
	
	checkIfFilenameInDB( { callbackIfFilenameNotInDB : callbackIfFilenameNotInDB } );
	
}


/////////////

function checkFileSize() {
	
	try {
	
		// get file upload element
		var fileElement = document.getElementById("fasta_file_field");

		//  length is zero if no file selected
		if( fileElement.files.length === 0){

			return false;
		}

		var file = fileElement.files[ 0 ];  // get file, will only be one file since not multi-select <file> element

		if ( file.size > maxFileUploadSize ) {
			
			alert( "File to upload is too large, it exceeds " + maxFileUploadSizeFormatted + " bytes." );

			$('#fasta_file_field').val('');
			
			return false;
		}

    } catch (e) { 
    	
    	alert( "Javascript File API not supported.  Use a newer browser" );
    	
    	return false;
    }
    
    return true;
}


///////////////

function checkIfFilenameInDB( params ) {


	// get file upload element
	var fileElement = document.getElementById("fasta_file_field");

	//  length is zero if no file selected
	if( fileElement.files.length === 0){

		return;
	}

	var file = fileElement.files[ 0 ];  // get file, will only be one file since not multi-select <file> element

	var filename = file.name;


	var _URL = contextPathJSVar + "/services/nrseqIdForFilename/get";


	var ajaxRequestData = {

			filename : filename
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

			checkIfFilenameInDBProcessResponse( { ajaxResponseData: data, ajaxRequestData: ajaxRequestData, originalCallParams : params });

		},
		failure: function(errMsg) {
			handleAJAXFailure( errMsg );
		},
		error: function(jqXHR, textStatus, errorThrown) {	

			handleAJAXError( jqXHR, textStatus, errorThrown );
		}
	});
}


function checkIfFilenameInDBProcessResponse( params ) {

	var ajaxResponseData = params.ajaxResponseData;

	if ( ajaxResponseData && ajaxResponseData.nrseqFileId ) {

		alert("The filename is already in the database.  It cannot be inserted again");
		
		$('#fasta_file_field').val('');

	} else {
			
		var originalCallParams = params.originalCallParams;

		if ( originalCallParams && originalCallParams.callbackIfFilenameNotInDB  ) {
			
			originalCallParams.callbackIfFilenameNotInDB();
		}
	}

}




//////////////////////////

function uploadFile() {
	
	
	var callbackIfFilenameNotInDB = function() {
		
		uploadFileafterCheckFile();
		
	};
	
	checkIfFilenameInDB( { callbackIfFilenameNotInDB : callbackIfFilenameNotInDB } );
		
}


////

function uploadFileafterCheckFile() {


	var formData;

	var filename;
	
	try {

		// get file upload element
		var fileElement = document.getElementById("fasta_file_field");

		//  length is zero if no file selected
		if( fileElement.files.length === 0){

			alert("File is required");

			return;
		}

		var file = fileElement.files[ 0 ];  // get file, will only be one file since not multi-select <file> element


		if ( ! checkFileSize() ) {

			return;
		}
		
		filename = file.name;


		// get description element
		var fastaDescriptionElement = document.getElementById("fasta_file_description");
		var fastaDescription = fastaDescriptionElement.value;

		var emailElement = document.getElementById("email");
		
		var email = "";
		if ( emailElement ) {
			email = emailElement.value;
		}

//		if ( fastaDescription === "" ) {
//
//			alert("Description is required");
//			return;
//		}


//		if ( email === "" ) {
//
//			alert("email is required");
//			return;
//		}


//		alert("Uploading file with name: " + file.name );

		formData = new FormData();
		formData.append( 'uploadFASTAFile', file, file.name );

		formData.append( 'fastaDescription', fastaDescription );  // description, sent as part of the multipart form

		formData.append( 'email', email );  // description, sent as part of the multipart form

//		formData.append( 'uploadTypeInForm', "uploadTypeInForm" ); // sent as part of the multipart form



    } catch (e) { 
    	
    	alert( "Javascript File API not supported.  Please use a newer browser" );
    	
    	return;
    }
    
    
	$("#uploading_message_and_progress").show();

	progressBarClear();
	
	
    var xhr = new XMLHttpRequest();
    
    xhr.onload = function() {

		var xhrResponse = xhr.response;
		
    	if (xhr.status === 200) {
    		
        	var resp = null;
        	
            try {
                resp = JSON.parse(xhrResponse);
                
                if ( resp.statusSuccess ) {

                	progressBarComplete();

//                	alert("File Uploaded, next displaying status for processing uploaded file" );
                	
                	$("#uploading_message_and_progress").hide();
                	
                	$("#fasta_file_field").val("");
 
                   	disableImportFileButton();
                	
                    
//                	window.location.href = "showFile.do?id=" + resp.id;
                	
                	
                	setTimeout( function(){
	
	                	var $file_import_id = $("#file_import_id");
	                	
	                	$file_import_id.val( resp.id );
	                	
	                	updateFileList( { callAfterUpdate : showFileDetailsForIdInHiddenField } );
                	
                	}, 10 );
                	
                } else {
                	
                	progressBarClear();
                	
                	alert("File NOT Uploaded, service returned failed status");
                }
                
            } catch (e) {
                resp = {
                		statusSuccess: false,
                    data: 'Unknown error occurred: [' + xhr.responseText + ']'
                };
                
                alert("File Uploaded but failed to get information from server response.");
            }
    		
    		
    	} else if (xhr.status === 400) {
    		
        	var resp = null;
        	
            try {
                resp = JSON.parse(xhrResponse);
                
                if ( resp.fileSizeLimitExceeded ) {
                	
                	progressBarClear();
                	
                	alert( "File NOT Uploaded, file too large.  Max file size in bytes: " + resp.maxSizeFormatted );
                	
                } else if ( resp.filenameAlreadyInDB ) {

                	progressBarClear();
                	
                	alert( "The filename is already in the database: " + filename );

                } else {
                	
                	progressBarClear();

            		alert("File NOT Uploaded, input data error, status 400");
                }
                
                
            } catch (e) {
            	
                resp = {
                		statusSuccess: false,
                    data: 'Unknown error occurred: [' + xhr.responseText + ']'
                };
                

            }

    		progressBarClear();


    	} else if (xhr.status === 500) {

    		
        	var resp = null;
        	
            try {
                resp = JSON.parse(xhr.response);
            } catch (e){
                resp = {
                		statusSuccess: false,
                    data: 'Unknown error occurred: [' + xhr.responseText + ']'
                };
            }
            
    		progressBarClear();
    		
    		alert("File NOT Uploaded, server error, status 500");
    		

    	} else if (xhr.status === 404) {

    		
        	var resp = null;
        	
            try {
                resp = JSON.parse(xhr.response);
            } catch (e){
                resp = {
                		statusSuccess: false,
                    data: 'Unknown error occurred: [' + xhr.responseText + ']'
                };
            }
            
    		progressBarClear();
    		
    		alert("File NOT Uploaded, Service not found on server. status 404");
    		    		
    	} else {

    		alert("File upload failed. xhr.status: " + xhr.status);
    		
    		progressBarClear();
    	}
    };
 

    xhr.upload.addEventListener('error', function(event){
    	
    	alert("File NOT Uploaded.  Error connecting to server");
    	
    	progressBarClear();
    	
    }, false);
    
    
    
    xhr.upload.addEventListener('progress', function(event){
    	
    	var progressPercent = Math.ceil( ( event.loaded / event.total) * 100 );
    	
//    	var MAX_PROGRESS_PERCENT_UPDATE_FROM_PROGRESS_LISTENER = 90;
//    	
//    	if ( progressPercent > MAX_PROGRESS_PERCENT_UPDATE_FROM_PROGRESS_LISTENER ) {
//    		
//    		progressPercent = MAX_PROGRESS_PERCENT_UPDATE_FROM_PROGRESS_LISTENER;
//    	}
    	
    	progressBarUpdate( progressPercent );
    	
    }, false);

    //  parameters added to the query string are available when the request is first received at the server.
    
    var postURL = contextPathJSVar + '/uploadFASTA?uploadTypeQueryString=uploadTypeQueryString';
    
    xhr.open('POST', postURL);
    xhr.send(formData);

}


var progressBarClear = function() {
	
	progressBarUpdate( 0 );
};

var progressBarComplete = function() {
	
	progressBarUpdate( 100 );
};


var progressBarUpdate = function( progressPercent ) { // progressPercent as integer 0 to 100
	
	
	var progressBar = document.getElementById('progressBar');

	progressBar.style.width = progressPercent + '%';
};





function enableDisableImportFileButton() {
	
	// description no longer required
	
//	var fasta_file_description = $("#fasta_file_description").val();
//	
//	if ( fasta_file_description === "" ) {
//
//		disableImportFileButton();
//		return false;
//	}


//	get file upload element
	var fileElement = document.getElementById("fasta_file_field");

//	length is zero if no file selected
	if( fileElement.files.length === 0){

		disableImportFileButton();
		return false;
	}
	
	enableImportFileButton();
}


function enableImportFileButton() {

	$("#import_file_button").prop( "disabled", false );
	$("#import_file_button_disabled_overlay").hide();
}

function disableImportFileButton() {

	$("#import_file_button_disabled_overlay").show();
	$("#import_file_button").prop( "disabled", true );
}
