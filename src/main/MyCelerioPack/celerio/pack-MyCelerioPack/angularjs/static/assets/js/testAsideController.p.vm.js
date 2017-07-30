$output.resource("static\assets\js", "testAsideController.js")##

/* Simple Controller */

app.controller("TestAsideController", ["${dollar}scope",  
"${dollar}log", function(scope, log) {

log.info("inside TestAsideController");

// create an aside
scope.aside = {"title": "Holy guacamole!", "content": "Best check yo self, you're not looking too good my dear !"};

log.info("aside created");




}]);
