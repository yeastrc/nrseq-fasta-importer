
//   timersManagement.js




//////////////////////////////////

// JavaScript directive:   all variables have to be declared with "var", maybe other things

"use strict";



///////////////////////////////////////////////


///////   Class for managing the times for updating the status on the page


var UpdateStatusTimerClass = function() {

	this.updateStatusTimerIds = {};
};

// Instantiated object of this class.  Call the functions on this object 

var updateStatusTimerObject = new UpdateStatusTimerClass();


//////

UpdateStatusTimerClass.prototype.add_updateStatusTimerId = function( params ) {
	
	var updateStatusTimerId = params.updateStatusTimerId;
	var file_import_id = params.file_import_id;

	if ( ! this.updateStatusTimerIds[ file_import_id ] ) {
		
		this.updateStatusTimerIds[ file_import_id ] = {};
	}
	
	var updateStatusTimerIds_for__file_import_id = this.updateStatusTimerIds[ file_import_id ];
	
	updateStatusTimerIds_for__file_import_id[ updateStatusTimerId ] = updateStatusTimerId;
};

//////

UpdateStatusTimerClass.prototype.remove_updateStatusTimerId = function( params ) {
	

	var updateStatusTimerId = params.updateStatusTimerId;
	var file_import_id = params.file_import_id;

	if (  this.updateStatusTimerIds[ file_import_id ] ) {

		var updateStatusTimerIds_for__file_import_id = this.updateStatusTimerIds[ file_import_id ];

		delete updateStatusTimerIds_for__file_import_id[ updateStatusTimerId ];
		
		var updateStatusTimerIdsKeysArray = Object.keys( updateStatusTimerIds_for__file_import_id );
		
		if ( updateStatusTimerIdsKeysArray.length === 0  ) {
			
			delete this.updateStatusTimerIds[ file_import_id ];
		}
	}	
};


//////

UpdateStatusTimerClass.prototype.cancel_all_updateStatusTimerId_for__file_import_id = function( params ) {
	
	var file_import_id = params.file_import_id;
	

	if ( this.updateStatusTimerIds[ file_import_id ] ) {

		var updateStatusTimerIdsPerFileImportId =  this.updateStatusTimerIds[ file_import_id ];

		var updateStatusTimerIdsPerFileImportIdKeysArray = Object.keys( updateStatusTimerIdsPerFileImportId );

		for ( var subIndex = 0; subIndex < updateStatusTimerIdsPerFileImportIdKeysArray.length; subIndex++ ) {

			var updateStatusTimerIdsPerFileImportIdProperty = updateStatusTimerIdsPerFileImportIdKeysArray[ subIndex ];

			var updateStatusTimerId = updateStatusTimerIdsPerFileImportId[ updateStatusTimerIdsPerFileImportIdProperty ];

			clearTimeout( updateStatusTimerId );
			
			this.remove_updateStatusTimerId( { file_import_id : file_import_id, updateStatusTimerId : updateStatusTimerIdsPerFileImportIdProperty } );
		}
	}
};

///////////

UpdateStatusTimerClass.prototype.cancelUpdateStatusTimers = function() {

	var updateStatusTimerIdsRootKeysArray = Object.keys( this.updateStatusTimerIds );

	for ( var index = 0; index < updateStatusTimerIdsRootKeysArray.length; index++ ) {
		
		var updateStatusTimerIdsRootProperty = updateStatusTimerIdsRootKeysArray[ index ];
		
		var updateStatusTimerIdsPerFileImportId =  this.updateStatusTimerIds[ updateStatusTimerIdsRootProperty ];

		var updateStatusTimerIdsPerFileImportIdKeysArray = Object.keys( updateStatusTimerIdsPerFileImportId );
		
		for ( var subIndex = 0; subIndex < updateStatusTimerIdsPerFileImportIdKeysArray.length; subIndex++ ) {

			var updateStatusTimerIdsPerFileImportIdProperty = updateStatusTimerIdsPerFileImportIdKeysArray[ subIndex ];

			var updateStatusTimerId = updateStatusTimerIdsPerFileImportId[ updateStatusTimerIdsPerFileImportIdProperty ];

			clearTimeout( updateStatusTimerId );
		}
	}
	
	this.updateStatusTimerIds = {};
};

//////////////////////////////
