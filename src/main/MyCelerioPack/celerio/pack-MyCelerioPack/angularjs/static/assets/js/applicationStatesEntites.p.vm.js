$output.resource("static\assets\js", "applicationStatesEntities.js")##

app.config(function(${dollar}stateProvider, ${dollar}urlRouterProvider) {
    	
## main state for each entity    	
#foreach ($entity in $project.entities.list)
  	/* to go to ${entity.model.vars} search screen */
	${dollar}stateProvider
		.state('${entity.model.var}', {
	    	url: "/${entity.model.var}",
	    	resolve: {
			config : ['${dollar}stateParams', 'AppParameterRestService', '${dollar}log', function(${dollar}stateParams, appParameterRestService, log) {
				return appParameterRestService.getParameter({domain: 'SCREEN_CONFIG', key: '${entity.name}'}).${dollar}promise.then (function (result) {
				 	if (!result.value) {
				 		log.info("no data found");
                			// no data has been found inside the dabatase, we need to create a fresh one
						return appParameterRestService.create({"domain": "SCREEN_CONFIG", "key": "${entity.name}", "value": "$entity.extended.getAttributesListJsonStyle()"});
					} else {
						return result;
					}
					});
                   }],
                 savedSearch : function() {
      				return {title: "a", query: "test"}; 
			} 
	    	},
			views: {    
				"mainView": {templateUrl: "assets/tpl/apps/${entity.model.var}/${entity.model.var}.html",
					controller: "${entity.name}Controller"},
				"footerView": {templateUrl: "assets/tpl/commons/footer.html"}
				}
            });	  
#end    	

## EDIT state for each entity    	
#foreach ($entity in $project.entities.list)
    /* to go in Edit mode on a ${entity.model.var} entity */
	${dollar}stateProvider
		.state('edit${entity.name}', {
			url: "/${entity.name.toLowerCase()}/$entity.extended.getCpkAttributesListWithCommaAndCurlyBracket()",
			views: {
				"mainView": {
	    				templateUrl: "assets/tpl/apps/${entity.model.var}/${entity.model.var}Edit.html",
					controller: "${entity.name}EditController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/footer.html"}
			},
			resolve: {
				mode : function() {
      				return "EDIT";
    			},
				item : ['${dollar}stateParams', '${entity.name}RestService', '${dollar}log', function(${dollar}stateParams, ${entity.name}RestService, log) {
					return ${entity.name}RestService.get($entity.extended.getCpkAttributesListJsonStyleStateParams(), function success(result) {}, function failure(result){
						log.info("something goes wrong !");
						}).${dollar}promise;
                    }]
				}
	});
#end

## VIEW state for each entity    	
#foreach ($entity in $project.entities.list)
	/* to go in read only mode on a ${entity.model.var} entity */
	${dollar}stateProvider
		.state('view${entity.name}', {
			url: "/${entity.name.toLowerCase()}/view/$entity.extended.getCpkAttributesListWithCommaAndCurlyBracket()",
			views: {
				"mainView": {
	    				templateUrl: "assets/tpl/apps/${entity.model.var}/${entity.model.var}Edit.html",
					controller: "${entity.name}EditController"
				},
			"footerView": {templateUrl: "assets/tpl/commons/footer.html"}
				},	
			resolve: {
				mode : function() {
      				return "VIEW";
    			},
				item : ['${dollar}stateParams', '${entity.name}RestService', '${dollar}log', function(${dollar}stateParams, ${entity.name}RestService, log) {
					return ${entity.name}RestService.get($entity.extended.getCpkAttributesListJsonStyleStateParams(), function success(result) {}, function failure(result){
						log.info("something goes wrong !");
						}).${dollar}promise;
                    }]
				}
	});
#end

## CREATE state for each entity    	
#foreach ($entity in $project.entities.list)
	/* to go in create mode to add a new ${entity.name} */
	${dollar}stateProvider
		.state('create${entity.name}', {
			url: "/${entity.name.toLowerCase()}",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/apps/${entity.model.var}/${entity.model.var}Edit.html",
					controller: "${entity.name}CreateController"
					},
				"footerView": {templateUrl: "assets/tpl/commons/footer.html"}
				},
			resolve: {
				mode : function() {
      				return "CREATE";
    			}
			}
		});
#end

## CONFIG state for each entity    	
#foreach ($entity in $project.entities.list)
	${dollar}stateProvider
		.state('config${entity.name}', {
			url: "/${entity.model.var}Config",
	    	templateUrl: "assets/tpl/apps/${entity.model.var}/${entity.model.var}Config.html",
			controller: "${entity.name}ConfigController",
			resolve: {
				config : ['${dollar}stateParams', 'AppParameterRestService', '${dollar}log', function(${dollar}stateParams, appParameterRestService, log) {
					return appParameterRestService.getParameter({domain: 'SCREEN_CONFIG', key: '${entity.name}'}).${dollar}promise.then (function (result) {
					 	if (!result.value) {
                   			// no data has been found inside the dabatase, we need to create a fresh one
							return appParameterRestService.create({"domain": "SCREEN_CONFIG", "key": "${entity.name}", "value": "$entity.extended.getAttributesListJsonStyle()"});
						} else {
							return result;
						}
						});
                    }]
				}
	});
#end
	
});