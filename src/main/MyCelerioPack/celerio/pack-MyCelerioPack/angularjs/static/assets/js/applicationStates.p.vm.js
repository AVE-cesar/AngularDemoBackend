$output.resource("static\assets\js", "applicationStates.js")##

#########################################################################################
## macro that can generate various useful String to deal with composite key:  
## str1: {keyPart1},{keyPart2}
## str2: {keyPart1: $stateParams.keyPart1, keyPart2: $stateParams.keyPart2}
## str3: @PathVariable String keyPart1, @PathVariable String keyPart2
## str4: keyPart1, keyPart2
## str5: String keyPart1, String keyPart2
## str6: :keyPart1,:keyPart2
## str7: {{item.id.keyPart1}}:{{item.id.keyPart2}}
## str8: {keyPart1: item.id.keyPart1, keyPart2: item.id.keyPart2}
## str9: { "column1": true, "column2": true, ... }
## str10: column1, column2, ... 
#########################################################################################
##
## Be careful: this method below is call several times; be sure to change all calls when you add a parameter
## calls: 
##			3 inside this file
##			1 in EntityController.e.vm.js
##			1 in EntityEditController.e.vm.js
##			1 in entity.e.vm.html
##			1 in CompositePk.cpk.vm.java
##			1 in EntityResource.e.vm.java
##	
##
#macro(generateSimpleOrCompositeKeyForURL $str1 $str2 $str3 $str4 $str5 $str6 $str7 $str8 $str9 $str10 $list)
#set ($compositeCall = false) 
	#foreach ($attribute in $list)
		#if ($attribute.isInCpk() == true)
			#if ($str1 == "")
				#set ($str1 = "{$attribute.name}")
				#set ($compositeCall = true)
				#set ($str2 = "{$attribute.name: ${dollar}stateParams.$attribute.name")
				#set ($str3 = "@PathVariable $attribute.type $attribute.var")
				#set ($str4 = "$attribute.var")
				#set ($str5 = "$attribute.type $attribute.var")
				#set ($str6 = ":$attribute.name")
				#set ($str7 = "{{item.id.$attribute.name}}")
				#set ($str8 = "{$attribute.name: item.id.$attribute.name")
			#else
				#set ($str1 = "$str1,{$attribute.name}")
				#set ($str2 = "$str2, $attribute.name: ${dollar}stateParams.$attribute.name")
				#set ($str3 = "$str3, @PathVariable $attribute.type $attribute.var")
				#set ($str4 = "$str4, $attribute.var")
				#set ($str5 = "$str5, $attribute.type $attribute.var")
				#set ($str6 = "$str6,:$attribute.name")
				#set ($str7 = "$str7:{{item.id.$attribute.name}}")
				#set ($str8 = "$str8, $attribute.name: item.id.$attribute.name")
			#end
		#end
		
		#if ($str9 == "") 
			#set ($str9 = "{ \"$attribute.name\": true")
			#set ($str10 = "$attribute.getColumnName()")
		#else
			#set ($str9 = "$str9, \"$attribute.name\": true")
			#set ($str10 = "$str10, $attribute.getColumnName()")
		#end
	#end
	
	#if ($str1 == "")
		#set ($str1 = "{id}") 
		#set ($str2 = "{id : ${dollar}stateParams.id}")
		#set ($str6 = ":id")
	#end	
	#if ($compositeCall == true)
		## close bracket
		#set ($str2 = "$str2}")
		#set ($str8 = "$str8}")
	#end
	## close bracket
	#set ($str9 = "$str9}")
#end


app.config(function(${dollar}stateProvider, ${dollar}urlRouterProvider) {
	//
	// For any unmatched url, redirect to /homepage
	${dollar}urlRouterProvider.otherwise("/home");
	//
  
    	
## main state for each entity    	
#foreach ($entity in $project.entities.list)
#set ($str1 = "")
#set ($str2 = "")
#set ($str3 = "")
#set ($str4 = "")
#set ($str5 = "")
#set ($str6 = "")
#set ($str7 = "")
#set ($str8 = "")
#set ($str9 = "")
#set ($str10 = "")
#generateSimpleOrCompositeKeyForURL($str1 $str2 $str3 $str4 $str5 $str6 $str7 $str8 $str9 $str10 $entity.attributes.list)
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
						return appParameterRestService.create({"domain": "SCREEN_CONFIG", "key": "${entity.name}", "value": "$str9"});
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
#set ($str1 = "")
#set ($str2 = "")
#set ($str3 = "")
#set ($str4 = "")
#set ($str5 = "")
#set ($str6 = "")
#set ($str7 = "")
#set ($str8 = "")
#set ($str9 = "")
#set ($str10 = "")
#generateSimpleOrCompositeKeyForURL($str1 $str2 $str3 $str4 $str5 $str6 $str7 $str8 $str9 $str10 $entity.attributes.list)
    /* to go in Edit mode on a ${entity.model.var} entity */
	${dollar}stateProvider
		.state('edit${entity.name}', {
			url: "/${entity.name.toLowerCase()}/${str1}",
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
					return ${entity.name}RestService.get(${str2}, function success(result) {}, function failure(result){
						log.info("something goes wrong !");
						}).${dollar}promise;
                    }]
				}
	});
#end

## VIEW state for each entity    	
#foreach ($entity in $project.entities.list)
#set ($str1 = "")
#set ($str2 = "")
#set ($str3 = "")
#set ($str4 = "")
#set ($str5 = "")
#set ($str6 = "")
#set ($str7 = "")
#set ($str8 = "")
#set ($str9 = "")
#set ($str10 = "")
#generateSimpleOrCompositeKeyForURL($str1 $str2 $str3 $str4 $str5 $str6 $str7 $str8 $str9 $str10 $entity.attributes.list)
	/* to go in read only mode on a ${entity.model.var} entity */
	${dollar}stateProvider
		.state('view${entity.name}', {
			url: "/${entity.name.toLowerCase()}/view/${str1}",
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
					return ${entity.name}RestService.get(${str2}, function success(result) {}, function failure(result){
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
							return appParameterRestService.create({"domain": "SCREEN_CONFIG", "key": "${entity.name}", "value": "$str9"});
						} else {
							return result;
						}
						});
                    }]
				}
	});
#end

  /* common states */
  ${dollar}stateProvider
		.state('home', {
            url: "/",
            views: {
				"mainView": {templateUrl: "assets/tpl/commons/home.html"},
				"footerView": {templateUrl: "assets/tpl/commons/footer.html"}
				}
            })
		.state('form', {
            url: "/form",
            views: {
				"mainView": {templateUrl: "assets/tpl/commons/form.html"},
				"footerView": {templateUrl: "assets/tpl/commons/footer.html"}
				}
            })
		.state('table', {
            url: "/table",
            views: {
            	"searchView": {templateUrl: "assets/tpl/commons/search.html"},
				"mainView": {templateUrl: "assets/tpl/commons/table.html"},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
            })
        .state('testAlert', {
            url: "/testAlert",
            views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/testAlert.html",
					controller: "TestAlertController"},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
            })
        .state('testIcon', {
            url: "/testIcon",
            views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/testIcon.html"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
            })
		.state('testAside', {
            url: "/testAside",
            views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/testAside.html",
					controller: "TestAsideController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
            })
        .state('testModal', {
            url: "/tesModal",
            views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/testModal.html",
					controller: "TestModalController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
            });


	// Now set up the states
	${dollar}stateProvider
    	.state('dashboard', {
      		url: "/dashboard",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/dashboard.html"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
	});
	
	${dollar}stateProvider
    	.state('settings', {
      		url: "/settings",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/settings.html"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
    });
    
    ${dollar}stateProvider
    	.state('logLevels', {
      		url: "/logLevels",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/logLevels.html"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
    });
    
    ${dollar}stateProvider
    	.state('translation', {
      		url: "/translation",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/apps/admin/translation.html"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
    });
	

    /*
     * 
     * Authentication part of the application: login, logout
     * 
     */
    
    /* to redirect users to the login page */
    ${dollar}stateProvider
		.state('login', {
	  		url: "/login",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/login.html",
					controller : "LoginController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				},
			resolve: {
				credential : function() {
	  				return {"login": "admin", "password": "admin", "error": false};
				}
			}
	});

    /* to redirect users to the logout page */
    ${dollar}stateProvider
		.state('logout', {
	  		url: "/logout",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/logout.html",
					controller: "LogoutController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
	});
	
});