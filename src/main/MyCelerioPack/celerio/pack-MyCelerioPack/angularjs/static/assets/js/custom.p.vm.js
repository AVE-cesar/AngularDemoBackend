$output.resource("static\assets\js", "custom.js")##

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
