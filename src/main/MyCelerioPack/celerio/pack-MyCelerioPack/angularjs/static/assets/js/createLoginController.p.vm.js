$output.resource("static\assets\js", "createLoginController.js")##

'use strict';

/* controller dedicated to the create login page */
app.controller('CreateLoginController', function (${dollar}rootScope, ${dollar}scope, AuthSharedService, ${dollar}log) {
    
	${dollar}scope.mode = 'EDIT';
	
	${dollar}scope.createLogin = function () {
		${dollar}log.info("call create login on server side for: " + ${dollar}scope.item.email);
    }
});