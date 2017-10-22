$output.resource("static\assets\js", "loginController.js")##

'use strict';

/* controller dedicated to login page */
app.controller('LoginController', function (${dollar}rootScope, ${dollar}scope, AuthSharedService, ${dollar}log, credential) {

	${dollar}log.info("inside LoginController, credential: " + credential);
	
	${dollar}scope.rememberMe = true;
	${dollar}scope.username = credential.username;
	${dollar}scope.password = credential.password;
	${dollar}scope.authenticationError = false;
	${dollar}rootScope.authenticationError = false;
	
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
