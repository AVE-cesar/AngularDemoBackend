$output.java("${configuration.rootPackage}.rest.error", "MyErrorController")##

$output.require("org.springframework.boot.autoconfigure.web.ErrorController")##
$output.require("org.springframework.web.bind.annotation.RequestMapping")##
$output.require("org.springframework.web.bind.annotation.RestController")##

// detailed info in: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
@RestController
public class MyErrorController implements ErrorController {

	@RequestMapping(value = "/error")
    public String error() {
        return "Error handling";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}