$output.resource("static\assets\js", "applicationStates.js")##

app.config(function(${dollar}stateProvider, ${dollar}urlRouterProvider) {
	//
	// For any unmatched url, redirect to /homepage
	${dollar}urlRouterProvider.otherwise("/home");
	//
  
    	
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
            url: "/testModal",
            views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/testModal.html",
					controller: "TestModalController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
				}
            })
         .state('testComboBox', {
            url: "/testComboBox",
            views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/testComboBox.html",
					controller: "TestComboBoxController"
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
				"footerView": {templateUrl: "assets/tpl/commons/loginFooter.html"}
				},
			resolve: {
				credential : function() {
	  				return {"login": "admin", "password": "admin", "error": false};
				}
			}
	});

	/* to redirect users to the create account page */
    ${dollar}stateProvider
		.state('createLogin', {
	  		url: "/createLogin",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/createLogin.html",
					controller: "CreateLoginController"
				},
				"footerView": {templateUrl: "assets/tpl/commons/emptyFooter.html"}
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