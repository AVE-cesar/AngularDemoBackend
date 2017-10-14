$output.resource("static\assets\js", "testComboBoxController.js")##

/**
 * AngularJS default filter with the following expression:
 * "person in people | filter: {name: ${dollar}select.search, age: ${dollar}select.search}"
 * performs an AND between 'name: ${dollar}select.search' and 'age: ${dollar}select.search'.
 * We want to perform an OR.
 */
app.filter('propsFilter', function() {
  return function(items, props) {
    var out = [];

    if (angular.isArray(items)) {
      var keys = Object.keys(props);

      items.forEach(function(item) {
        var itemMatches = false;

        for (var i = 0; i < keys.length; i++) {
          var prop = keys[i];
          var text = props[prop].toLowerCase();
          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
            itemMatches = true;
            break;
          }
        }

        if (itemMatches) {
          out.push(item);
        }
      });
    } else {
      // Let the output be the input untouched
      out = items;
    }

    return out;
  };
});

/* Simple Controller */
app.controller("TestComboBoxController", ["${dollar}scope",  
"${dollar}log", function(scope, log) {

log.info("inside TestComboBoxController");

var vm = this;

scope.peopleObj = {
	    '1' : { name: 'Adam',      email: 'adam@email.com',      age: 12, country: 'United States' },
	    '2' : { name: 'Amalie',    email: 'amalie@email.com',    age: 12, country: 'Argentina' },
	    '3' : { name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina' },
	    '4' : { name: 'Adrian',    email: 'adrian@email.com',    age: 21, country: 'Ecuador' },
	    '5' : { name: 'Wladimir',  email: 'wladimir@email.com',  age: 30, country: 'Ecuador' },
	    '6' : { name: 'Samantha',  email: 'samantha@email.com',  age: 30, country: 'United States' },
	    '7' : { name: 'Nicole',    email: 'nicole@email.com',    age: 43, country: 'Colombia' },
	    '8' : { name: 'Natasha',   email: 'natasha@email.com',   age: 54, country: 'Ecuador' },
	    '9' : { name: 'Michael',   email: 'michael@email.com',   age: 15, country: 'Colombia' },
	    '10' : { name: 'Nicolás',   email: 'nicolas@email.com',    age: 43, country: 'Colombia' }
	  };

scope.person = {};

scope.person.selectedValue = scope.peopleObj[3];
scope.person.selectedSingle = 'Samantha';
scope.person.selectedSingleKey = '5';
// To run the demos with a preselected person object, uncomment the line below.
scope.person.selected = scope.person.selectedValue;

scope.people = [
    { name: 'Adam',      email: 'adam@email.com',      age: 12, country: 'United States' },
    { name: 'Amalie',    email: 'amalie@email.com',    age: 12, country: 'Argentina' },
    { name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina' },
    { name: 'Adrian',    email: 'adrian@email.com',    age: 21, country: 'Ecuador' },
    { name: 'Wladimir',  email: 'wladimir@email.com',  age: 30, country: 'Ecuador' },
    { name: 'Samantha',  email: 'samantha@email.com',  age: 30, country: 'United States' },
    { name: 'Nicole',    email: 'nicole@email.com',    age: 43, country: 'Colombia' },
    { name: 'Natasha',   email: 'natasha@email.com',   age: 54, country: 'Ecuador' },
    { name: 'Michael',   email: 'michael@email.com',   age: 15, country: 'Colombia' },
    { name: 'Nicolás',   email: 'nicolas@email.com',    age: 43, country: 'Colombia' }
  ];

}]);
