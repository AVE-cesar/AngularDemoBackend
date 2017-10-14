$output.resource("static/assets/js/entity", "${entity.model.var}CreateController.js")##

app.controller("${entity.model.type}CreateController", ["${dollar}scope", "${dollar}window", "${dollar}aside", 
"${dollar}log", "${entity.model.type}RestService", 
#foreach ($attribute in $entity.allAttributes.list)
#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))
	#if ($attribute.getXToOneRelation().isManyToOne())
		"${attribute.getEntityIPointTo().name}RestService",
	#else
		/* Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! */
	#end 
#end
#end
		"${dollar}alert", "${dollar}timeout", "mode", function(scope, window, aside, log, 
		${entity.model.var}RestService, 
#foreach ($attribute in $entity.allAttributes.list)
	#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))
		#if ($attribute.getXToOneRelation().isManyToOne())
			$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1).toLowerCase()RestService,
		#else
			/* Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! */
		#end 
	#end
#end		
		alertService, timeoutService, mode) {

	log.info("inside ${entity.model.type}EditController, mode: " + mode);
	scope.mode = mode;
		
#foreach ($attribute in $entity.allAttributes.list)
	#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))		
		#if ($attribute.getXToOneRelation().isManyToOne())
// fill $attribute.getEntityIPointTo().name combo with data from server side
${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1).toLowerCase()}RestService.query({query: '*'}, function success(result){
	log.info("receiving ${attribute.getEntityIPointTo().name} from server side");
	scope.$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1).toLowerCase()s = result;
	log.info("${attribute.getEntityIPointTo().name} post refresh: " + result.length);
});
		#else
			/* Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! */
		#end 
	#end
#end		
	
	/** Creates or updates an item */
	scope.saveItem = function() {
		log.info("Creating or updating an item");
		
		// defines the success behavior inside a methode
		var onSaveSuccess = function success(data) {
			console.log('success, got data: ', data);
		
			${dollar}("#successAlert").fadeTo(2000, 500).slideUp(500, function(){
				${dollar}("#successAlert").hide();
			});
		};
		
		// defines the error behavior inside a methode
		var onSaveError = function (result) {
			console.log('error from the server');
			${dollar}("#errorAlert").fadeTo(2000, 500).slideUp(500, function(){
				${dollar}("#errorAlert").hide();
			});
		};
		
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

}]);
