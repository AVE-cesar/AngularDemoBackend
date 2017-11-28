$output.resource("static\assets\js", "footerController.js")##

'use strict';

/* controller dedicated to the footer page */
app.controller('FooterController', function (${dollar}rootScope, ${dollar}scope, ${dollar}log, ${dollar}location, ${dollar}anchorScroll) {

	var scrollTo = function (id) {
	    ${dollar}location.hash(id);
	    ${dollar}anchorScroll();
	};
	
	${dollar}scope.scrollDown = function () {
	        scrollTo('mybottom');
	};
	
	${dollar}scope.scrollUp = function () {
	    scrollTo('mytop');
	};


});