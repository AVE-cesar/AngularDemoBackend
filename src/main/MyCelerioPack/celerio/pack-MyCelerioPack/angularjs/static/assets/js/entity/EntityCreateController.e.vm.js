$output.resource("static/assets/js/entity", "${entity.model.var}CreateController.js")##

app.controller("${entity.model.type}CreateController", ["${dollar}scope", "${dollar}window", "${dollar}aside", 
"${dollar}log", "${entity.model.type}RestService", 
#foreach ($attribute in $entity.allAttributes.list)
#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))
	#if ($attribute.getXToOneRelation().isManyToOne())
		"${attribute.getEntityIPointTo().name}RestService",
	#elseif ($attribute.getXToOneRelation().isOneToOne())
		"${attribute.getEntityIPointTo().name}RestService",	
	#else
		/* Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! */
	#end 
#end
#end
		"${dollar}alert", "${dollar}timeout", "mode", "usSpinnerService", function(scope, window, aside, log, 
		${entity.model.var}RestService, 
#foreach ($attribute in $entity.allAttributes.list)
	#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))
		#if ($attribute.getXToOneRelation().isManyToOne())
			$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1).toLowerCase()RestService,
		#elseif ($attribute.getXToOneRelation().isOneToOne())
			$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1).toLowerCase()RestService,		
		#else
			/* Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! */
		#end 
	#end
#end		
		alertService, timeoutService, mode, usSpinnerService) {

	log.info("inside ${entity.model.type}EditController, mode: " + mode);
	scope.mode = mode;
	scope.item= {};

#foreach ($attribute in $entity.allAttributes.list)
	#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))		
		#if ($attribute.getXToOneRelation().isManyToOne())
	// fill $attribute.getEntityIPointTo().name combo with data from server side
	scope.${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1)}Item = {};
	scope.loadAll${attribute.getEntityIPointTo().name}s = function() {
		${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1).toLowerCase()}RestService.query({query: '*'}, function success(result){
			log.info("receiving ${attribute.getEntityIPointTo().name} from server side");
			scope.$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1).toLowerCase()s = result;
			log.info("${attribute.getEntityIPointTo().name} post refresh: " + result.length);

			// sort values to facilitate research for the end user
			try {scope.${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1)}s.sort(dynamicSort("name"));
			} catch (err) {}
		});
	};
	scope.loadAll${attribute.getEntityIPointTo().name}s();
	
	scope.add${attribute.getEntityIPointTo().name}Item = function() {
		console.log("inside add${attribute.getEntityIPointTo().name}Item: " + scope.${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1)}Item);
	
		// add the new ${attribute.getEntityIPointTo().name} into the database
		${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1).toLowerCase()}RestService.create(scope.${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1)}Item, onSaveSuccessReload${attribute.getEntityIPointTo().name}, onSaveError);
	};
	
	// defines the success behavior inside a method
	var onSaveSuccessReload${attribute.getEntityIPointTo().name} = function success(data) {
		console.log('success, got data: ', data);
	
		$("#successAlert").fadeTo(2000, 500).slideUp(500, function(){
			$("#successAlert").hide();
		});
		
		// add it also to the combo that lists all authors
		scope.loadAll${attribute.getEntityIPointTo().name}s();
		
		// deactivate the loading spinner
		usSpinnerService.stop('spinner-1');
	};
	
		#elseif ($attribute.getXToOneRelation().isOneToOne())
	// fill $attribute.getEntityIPointTo().name combo with data from server side
	${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1).toLowerCase()}RestService.query({query: '*'}, function success(result){
		log.info("receiving ${attribute.getEntityIPointTo().name} from server side");
		scope.$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1)s = result;
		log.info("${attribute.getEntityIPointTo().name} post refresh: " + result.length);
	
		// sort values to facilitate research for the end user
		try {scope.${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1)}s.sort(dynamicSort("name"));
		} catch (err) {}		
	});
		#else
			/* Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! */
		#end 
	#end
#end		
	
	/** Creates or updates an item */
	scope.saveItem = function() {
		// activate the loading spinner
		usSpinnerService.spin('spinner-1');

		log.info("Creating or updating an item");
		
		if (scope.item.id != null) {
			// update mode
			${entity.model.var}RestService.update(scope.item, onSaveSuccess, onSaveError);
		} else {
			// creation mode
			${entity.model.var}RestService.create(scope.item, onSaveSuccess, onSaveError);
		}
	};

	/** Removes one item or a list of items (selected ones) */
	scope.remove = function(b) {
		console.log(scope.selectAll);
		var r = confirm("Are you sure ?");
		if (r == true) {
			if (b) {				
				// one item deletion mode
				${entity.model.var}RestService.delete({id: b.id}, function success(data) {
					scope.refresh();
				}, function failure(err) {
					alert('request failed');
				});
			}	
		}
	};

	// defines the success behavior inside a methode
	var onSaveSuccess = function success(data) {
		console.log('success, got data: ', data);
	
		${dollar}("#successAlert").fadeTo(2000, 500).slideUp(500, function(){
			${dollar}("#successAlert").hide();
		});
		
		// deactivate the loading spinner
		usSpinnerService.stop('spinner-1');
	};
	
	// defines the error behavior inside a methode
	var onSaveError = function (result) {
		console.log('error from the server');
		${dollar}("#errorAlert").fadeTo(2000, 500).slideUp(500, function(){
			${dollar}("#errorAlert").hide();
		});

		// deactivate the loading spinner
		usSpinnerService.stop('spinner-1');
	};
}]);
