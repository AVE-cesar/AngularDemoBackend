$output.resource("static\assets\js", "applicationStates.js")##

app.config(function(${dollar}stateProvider, ${dollar}urlRouterProvider) {
	//
	// For any unmatched url, redirect to /homepage
	${dollar}urlRouterProvider.otherwise("/home");


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
	  				return {"username": "admin", "password": "admin", "error": false};
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
	
	/* to redirect users to the registration page */
    ${dollar}stateProvider
		.state('confirmRegistration', {
	  		url: "/confirmRegistration",
			views: {
				"mainView": {
					templateUrl: "assets/tpl/commons/confirmRegistration.html",
					controller: "ConfirmRegistrationController"
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