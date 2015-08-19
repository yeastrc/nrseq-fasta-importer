
//  localDataCaches.js

//   In Browser caching of data and retrieval of values not in the cache




//////////////////////////////////

// JavaScript directive:   all variables have to be declared with "var", maybe other things

"use strict";



///////////////////////////////////////////////


///////   Class for retrieving and caching the NCBI description for taxonomy Ids

///  The callback fcn passed in will be called with an object 
///           {  taxonomyData  : { scientificName : , scientificNameFound :  }
///
///          where scientificName is the organism name
///                     and
///                scientificNameFound is a boolean indicating that the scientificName was found


var NCBIDescriptorFromTaxonomyIdClass = function() {

	this.taxonomyDataByTaxonomyId = {}; //  Single taxonomyData object has properties: scientificName, scientificNameFound
	
	this.callbackFcnListByTaxonomyId = {}; //  list of callback function to call when description retrieved, per taxonomy id
};


//  Instantiated object of this class.  Call the functions on this object

var ncbiDescriptorFromTaxonomyIdObject = new NCBIDescriptorFromTaxonomyIdClass();



//////

NCBIDescriptorFromTaxonomyIdClass.prototype.get_NCBIDescriptorForTaxonomyId = function( params ) {
	
	var objectThis = this;
	
	var taxonomyId = params.taxonomyId;
	var callbackFcn = params.callbackFcn;
	
	if ( this.taxonomyDataByTaxonomyId[ taxonomyId ] ) {
		
		//  Have taxonomy label so immediately call callbackFcn(..)
		
		callbackFcn( { taxonomyData : this.taxonomyDataByTaxonomyId[ taxonomyId ] } );
		
		return; //  EARLY EXIT
	}
	
	// Do not have taxonomy label for id so add callbackFcn(..) to list 
	
	if ( ! this.callbackFcnListByTaxonomyId[ taxonomyId ] ) {
		
		this.callbackFcnListByTaxonomyId[ taxonomyId ] = [];
	}
	
	this.callbackFcnListByTaxonomyId[ taxonomyId ].push( callbackFcn );
	
	
	//  What is next is an optimization so that there is only one call to the web service
	//    and for the rest of the requests for this taxonomy id the callback fcns will be 
	//    accumulated and called when the data is retrieved.
	
	
	if ( this.callbackFcnListByTaxonomyId[ taxonomyId ].length === 1 ) {
		
		// This is the first callback fcn added so need to make the ajax call
	
		//  Retrieve taxonomy label from web service


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
//			One thing this means is that arrays are sent as the object property instead of object property followed by "[]".
//			So searchIds array is passed as "searchIds=<value>" which is what Jersey expects

			success: function(data)	{

				objectThis.__get_NCBIDescriptorForTaxonomyId_ProcessResponse(
						{ ajaxResponseData: data, 
							taxonomyId : taxonomyId,
							ajaxRequestData: ajaxRequestData }
				);
			},
			failure: function(errMsg) {
				handleAJAXFailure( errMsg );
			},
			error: function(jqXHR, textStatus, errorThrown) {	

				handleAJAXError( jqXHR, textStatus, errorThrown );
			}
		});
	
	
	}
	


};


//////

NCBIDescriptorFromTaxonomyIdClass.prototype.__get_NCBIDescriptorForTaxonomyId_ProcessResponse = function( params ) {

	var taxonomyData = params.ajaxResponseData;
	
	var taxonomyId = params.taxonomyId;
	
	this.taxonomyDataByTaxonomyId[ taxonomyId ] = taxonomyData;

	var callbackFunctionArray = this.callbackFcnListByTaxonomyId[ taxonomyId ];
	
	if ( callbackFunctionArray ) {
		
		for ( var callbackFunctionIndex = 0; callbackFunctionIndex < callbackFunctionArray.length; callbackFunctionIndex++ ) {
			
			var callbackFcn = callbackFunctionArray[ callbackFunctionIndex ];
			
			callbackFcn( { taxonomyData : taxonomyData } );
		}
		
		delete this.callbackFcnListByTaxonomyId[ taxonomyId ]; //  No longer need since processed these callbacks
	}
	
};


