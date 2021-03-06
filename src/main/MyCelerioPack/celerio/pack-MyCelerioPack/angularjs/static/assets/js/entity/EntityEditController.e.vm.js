$output.resource("static/assets/js/entity", "${entity.model.var}EditController.js")##

app.controller("${entity.model.type}EditController", ["${dollar}scope", "${dollar}window", "${dollar}aside", 
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
		"${entity.model.type}RestInvRelationService", "${dollar}alert", "${dollar}timeout", "item", "mode", "usSpinnerService", function(scope, window, aside, log, 
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
		${entity.model.var}RestInvRelationService, alertService, timeoutService, item, mode, usSpinnerService) {
	
	log.info("inside ${entity.model.type}EditController, mode: " + mode);
	log.info("inside ${entity.model.type}EditController, item: " + item);
## convert date columns to javascript date type because we're using input of type date	
#foreach ($attribute in $entity.attributes.list)
	#if ($attribute.type == "Date")
		// to be compliant with input of type Date
		item.$attribute.name = new Date(item.$attribute.name); 
	#end
#end	
	scope.mode = mode;
	scope.item = item;

/* Many to Many relations */
## --------------- Many to many: to entity
#foreach ($relation in $entity.manyToMany.list)
	scope.${relation.to.var}sSearchFilter = '';
#end

#foreach ($attribute in $entity.allAttributes.list)
	#if ($attribute.isInFk() || ($attribute.isInCpk() == true && $attribute.isSimpleFk() == true))
		#if ($attribute.getXToOneRelation().isManyToOne())
// fill $attribute.getEntityIPointTo().name combo with data from server side
${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1).toLowerCase()}RestService.query({query: '*'}, function success(result){
	log.info("receiving ${attribute.getEntityIPointTo().name} from server side");
	scope.$attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()$attribute.getEntityIPointTo().name.substring(1)s = result;
	log.info("${attribute.getEntityIPointTo().name} post refresh: " + result.length);
	
	// sort values to facilitate research for the end user
	try {scope.${attribute.getEntityIPointTo().name.substring(0,1).toLowerCase()}${attribute.getEntityIPointTo().name.substring(1)}s.sort(dynamicSort("$attribute.getEntityIPointTo().extended.getFirstNoneKeyAttribute()"));
	} catch (err) {}
});
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
			// Type of relation [$attribute.getXToOneRelation()] not implemented yet !!!! 
		#end 
	#end
#end		

/* inverse relations */	
## --------------- Inverse relation
#foreach ($entityP in $project.getEntities().list)
#foreach ($rel in $entityP.getRelations().list)
#if ($entity == $rel.getToEntity())
#if ($rel.getKind() == "many-to-one" || $rel.getKind() == "many-to-many" || $rel.getKind() == "one-to-one")
scope.${entityP.model.var}sSearchFilter = '';
	${entity.model.var}RestInvRelationService.find${entityP.model.type}By${entity}({id: item.id}, function(result) {
	    scope.find${entityP.model.type}By${entity} = result;
	    log.info("inv. relation, ${entityP.model.type} data post refresh: " + scope.find${entityP.model.type}By${entity}.length);
    });
#end
#end
#end ## end foreach relation
#end  ## end foreach entity

	/** Clear item */
	scope.clear = function () {
			scope.item = null;
	};
	
	/** Creates or updates an item */
	scope.saveItem = function() {
		// activate the loading spinner
		usSpinnerService.spin('spinner-1');
		
		log.info("Creating or updating an item");
		
		// defines the success behavior inside a method
		var onSaveSuccess = function success(data) {
			console.log('success, got data: ', data);
			
			${dollar}("#successAlert").fadeTo(2000, 500).slideUp(500, function(){
				${dollar}("#successAlert").hide();
			});
			
			// deactivate the loading spinner
			usSpinnerService.stop('spinner-1');
		};
		
		// defines the error behavior inside a method
		var onSaveError = function (result) {
			console.log('error from the server');
			${dollar}("#errorAlert").fadeTo(2000, 500).slideUp(500, function(){
				${dollar}("#errorAlert").hide();
			});
			
			// deactivate the loading spinner
			usSpinnerService.stop('spinner-1');
		};
		
		if (scope.item.id != null) {
			// update mode
			console.log("update mode");
			#if (!$entity.hasSimplePk())
				#foreach ($attribute in $entity.getPrimaryKey().getAttributes())
					#if ($attribute.isSimpleFk() == true)
						scope.item.id.${attribute.var}  = scope.item.${attribute.var}.id;
					#else
						scope.item.${attribute.var} = scope.item.id.${attribute.var};
					#end
				#end
			#end	
			${entity.model.var}RestService.update(scope.item, onSaveSuccess, onSaveError);
		} else {
			// creation mode
			console.log("creation mode");
			${entity.model.var}RestService.save(scope.item, onSaveSuccess, onSaveError);
		}
	};

	/** Removes one item or a list of items (selected ones) */
	scope.remove = function(item) {
		
		var r = confirm("Are you sure ?");
		if (r == true) {
			if (item) {				
				// one item deletion mode
				#set ($key = "{id: item.id}")
				#if (!$entity.hasSimplePk())
					#set ($key = $entity.extended.getCpkAttributesListJsonStyleItemId())
					#foreach ($attribute in $entity.getPrimaryKey().getAttributes())
						console.log("cpk: " + item.id.$attribute.var);
					#end
				#end
				${entity.model.var}RestService.delete($key, function success(data) {
					scope.clear();
					alert('item deleted');
				}, function failure(err) {
					alert('request failed');
				});
			}	
		}
	};

}]);
