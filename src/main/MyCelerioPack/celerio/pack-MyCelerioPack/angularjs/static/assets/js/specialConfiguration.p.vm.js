$output.resource("static\assets\js", "specialConfiguration.js")##

/** ResourceProvider special configuration (very important !) */
app.config(['${dollar}resourceProvider', '${dollar}httpProvider', function(${dollar}resourceProvider, ${dollar}httpProvider) {
	/* Don't strip trailing slashes from calculated URLs */
	${dollar}resourceProvider.defaults.stripTrailingSlashes = false;
	
	/*
	 The custom "X-Requested-With" is a conventional header sent by browser clients, and it used to be the default in Angular 
	 but they took it out in 1.3.0. Spring Security responds to it by not sending a "WWW-Authenticate" header in a 401 response, 
	 and thus the browser will not pop up an authentication dialog (which is desirable in our app since we want to control the authentication). */
	${dollar}httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
}]);