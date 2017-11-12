$output.resource("static\assets\js", "createLoginController.js")##

'use strict';

/* controller dedicated to the create login page */
app.controller('CreateLoginController', function (${dollar}rootScope, ${dollar}scope, AuthSharedService, RegistrationService, ${dollar}log) {
    
	${dollar}scope.mode = 'EDIT';
	
	// defines the success behavior inside a method
	var onSaveSuccess = function success(data) {
		console.log('success, got data: ', data);
		
		${dollar}("#successAlert").fadeTo(2000, 500).slideUp(500, function(){
			${dollar}("#successAlert").hide();
		});
	};
	
	// defines the error behavior inside a method
	var onSaveError = function (result) {
		console.log('error from the server');
		${dollar}("#errorAlert").fadeTo(2000, 500).slideUp(500, function(){
			${dollar}("#errorAlert").hide();
		});
	};
	
	${dollar}scope.createLogin = function () {
		${dollar}log.info("call create login on server side for: " + ${dollar}scope.item);
		${dollar}scope.item.enabled = 0;
		
		RegistrationService.createLogin(${dollar}scope.item, onSaveSuccess, onSaveError);
    }
});


app.factory('RegistrationService', function (${dollar}resource) {
	return ${dollar}resource('createLogin', {}, {
		'createLogin': { method: 'POST'},
		'registration': { 
			method: 'GET',
			url: 'registration/:id',
			transformResponse: function (data) {
				try {
					data = angular.fromJson(data);
				} catch (err) {}	
				return data;
			}
		}
	});
});