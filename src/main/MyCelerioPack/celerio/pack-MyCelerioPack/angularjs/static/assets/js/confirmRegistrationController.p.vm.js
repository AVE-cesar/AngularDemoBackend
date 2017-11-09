$output.resource("static\assets\js", "confirmRegistrationController.js")##

'use strict';

/* controller dedicated to the login confirmation */
app.controller('ConfirmRegistrationController',
["${dollar}rootScope", "${dollar}scope", "AuthSharedService", "item", "${dollar}log", 
function (${dollar}rootScope, ${dollar}scope, AuthSharedService, item, ${dollar}log) {
	${dollar}log.info("inside ConfirmRegistrationController: " + item);
}]);
