$output.resource("static\assets\js", "loginController.js")##

'use strict';

/* controller dedicated to login page */
app.controller('LoginController', function (${dollar}rootScope, ${dollar}scope, AuthSharedService, ${dollar}log) {

	${dollar}scope.rememberMe = true;
        
    ${dollar}scope.doLogin = function () {
    		${dollar}log.info("call login on server side for: " + ${dollar}scope.username);
        	
    		${dollar}rootScope.authenticationError = false;
            
    		AuthSharedService.login(
    				${dollar}scope.username,
    				${dollar}scope.password,
    				${dollar}scope.rememberMe
    		);
    };
    
});

/*
app.factory('RegistrationService', function (${dollar}resource) {
	return ${dollar}resource('createLogin', {}, {
		'createLogin': { method: 'GET'}
	});
});
*/