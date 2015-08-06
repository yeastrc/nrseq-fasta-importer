

//   handleServicesAJAXErrors.js




/////////////////////

//Handle when AJAX call gets failure

function handleAJAXFailure( errMsg ) {

	showAjaxErrorMsgFromMsg( { errorMsg : "Connecting to server failed: " + errorMsg } );
}

/////////////////////

//  Handle when AJAX call gets error

function handleAJAXError( jqXHR, textStatus, errorThrown ) {

	
	var jqXHR_statusCode = jqXHR.status;
	var jqXHR_responseText = jqXHR.responseText;
	

	if ( jqXHR_statusCode === 401 ) {
		
		showAjaxErrorMsg( { errorMsg : "401 received, responseText: " + jqXHR_responseText  } );
							
	} else if ( jqXHR_statusCode === 403 ) {
		
		showAjaxErrorMsg( { errorMsg : "403 received, responseText: " + jqXHR_responseText } );
			
	} else if ( jqXHR_statusCode === 404 ) {
		
		showAjaxErrorMsg( { errorMsg : "404 received, service not found on server, textStatus: " + textStatus  } );
			

	} else if ( jqXHR_statusCode === 500 ) {
		
		showAjaxErrorMsg( { errorMsg : "Internal Server error, status code 500, textStatus: " + textStatus  } );
		
	} else {
		
		showAjaxErrorMsg( { errorMsg : "exception: " + errorThrown + ", jqXHR: " + jqXHR + ", textStatus: " + textStatus } );
	}
	
	
	
};


function showAjaxErrorMsg( params ) {
	
	var errorPageElementId = params.errorPageElementId;
	var errorMsg = params.errorMsg;
	
	if ( errorPageElementId  ) {

		var $msg = $("#" + errorPageElementId );
		
		if ( $msg.length === 0 ) {
			
			showAjaxErrorMsgFromMsg( { errorMsg : errorMsg } );
			
		} else {
			
			$(".overlay_show_hide_parts_jq").hide();
			
			$msg.show();
			
			window.scroll(0, 0);  // scroll to top left, assuming message is in that corner
		}
		
	} else {
		
		showAjaxErrorMsgFromMsg( { errorMsg : errorMsg } );
		
		
	}
	
	
}
	

function showAjaxErrorMsgFromMsg( params ) {
	
	var errorMsg = params.errorMsg;
	
	if ( ! errorMsg || errorMsg === "" )  {
		
		throw "No value passed in params.errorMsg to function showAjaxErrorMsgFromMsg( params )";
	}
	
//	alert( errorMsg );
	
	var html = '<div style="position: absolute; background-color: white; z-index: 10000; top:40px; left:40px; width:500px; padding: 10px; border-width: 5px; border-color: red; border-style: solid;" >'
	
	+ '<h1 style="color: red;">Error accessing server</h1>'
	
	+ '<h3>Please reload the page and try again.</h3>'
	+ '<h3>If this error continues to occur, please contact the administrator.</h3>'
	
	+ '<br><br>'
	
	+ 'Error Message:<br>'
	
	+ errorMsg
	
	+ '<br><br>'
	
	+ '</div>';
		
		
	$("body").append( html );
	
	window.scroll(0, 0);  // scroll to top left, assuming message is in that corner

}
	
			
			