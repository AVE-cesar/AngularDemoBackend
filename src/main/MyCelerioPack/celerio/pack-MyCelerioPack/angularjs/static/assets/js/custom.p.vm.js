$output.resource("static\assets\js", "custom.js")##

/**
 * Add a directive to check if a login exists or not every time a guest enters the login in the input field
 * This directive makes a call to the server to do this check.
 * 
 * see https://www.codementor.io/vominhquoc/async-validator-in-angularjs-ovcr1p1jb
 * 
 * @param ${dollar}q
 * @param ${dollar}http
 *
 * @returns
 */
app.directive('login', function(${dollar}q, ${dollar}http) {
	return {
		require: 'ngModel',
		link: function(scope, element, attrs, ngModel) {
			ngModel.${dollar}asyncValidators.login = function(modelValue, viewValue) {
				return ${dollar}http.get('checkLoginAvailability/' + viewValue)
					.then(
					function(response) {
						console.log("status: " + response.data);
						if (!response.data) {
							return ${dollar}q.reject(); 
							/* Server will give me a  notify if it exist or not. */
							/* I will throw a error If it exist with "reject" */
							}
						return true;
					}
				);
			};
		}
	};
});

/**
 * Add a directive to check if two password field are equals.
 */
app.directive('pwdCheck', [function () {
	return {
		require: 'ngModel',
		link: function (scope, elem, attrs, ctrl) {
			/* find the other input field */
			var firstPassword = '#' + attrs.pwdCheck;
			elem.add(firstPassword).on('keyup', function () {
				scope.${dollar}apply(function () {
					// get the second value and compare it with the first one
					var v = elem.val()===${dollar}(firstPassword).val();
					/* set the result for later use */
					ctrl.${dollar}setValidity('pwdmatch', v);
				});
			});
		}
	}
}]);

/**
 * We override the Angular default filter for our ui-select component.
 * 
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



/**
 * other functions.
 */
(function(){
  ${dollar}(window).scroll(function () {
      var top = ${dollar}(document).scrollTop();
      ${dollar}('.splash').css({
        'background-position': '0px -'+(top/3).toFixed(2)+'px'
      });
      if(top > 50)
        ${dollar}('#home > .navbar').removeClass('navbar-transparent');
      else
        ${dollar}('#home > .navbar').addClass('navbar-transparent');
  });

  ${dollar}("a[href='#']").click(function(e) {
    e.preventDefault();
  });

  var ${dollar}button = ${dollar}("<div id='source-button' class='btn btn-primary btn-xs'>&lt; &gt;</div>").click(function(){
    var html = ${dollar}(this).parent().html();
    html = cleanSource(html);
    ${dollar}("#source-modal pre").text(html);
    ${dollar}("#source-modal").modal();
  });

  ${dollar}('.bs-component [data-toggle="popover"]').popover();
  ${dollar}('.bs-component [data-toggle="tooltip"]').tooltip();

  ${dollar}(".bs-component").hover(function(){
    ${dollar}(this).append(${dollar}button);
    ${dollar}button.show();
  }, function(){
    ${dollar}button.hide();
  });

  function cleanSource(html) {
    html = html.replace(/×/g, "&times;")
               .replace(/«/g, "&laquo;")
               .replace(/»/g, "&raquo;")
               .replace(/←/g, "&larr;")
               .replace(/→/g, "&rarr;");

    var lines = html.split(/\n/);

    lines.shift();
    lines.splice(-1, 1);

    var indentSize = lines[0].length - lines[0].trim().length,
        re = new RegExp(" {" + indentSize + "}");

    lines = lines.map(function(line){
      if (line.match(re)) {
        line = line.substring(indentSize);
      }

      return line;
    });

    lines = lines.join("\n");

    return lines;
  }

})();
