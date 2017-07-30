$output.resource("static\assets\js", "logoutController.js")##

'use strict';

/* controller dedicated to logout page */
app.controller('LogoutController', function (${dollar}rootScope, ${dollar}scope, AuthSharedService, ${dollar}log) {

	AuthSharedService.logout();
	
    });