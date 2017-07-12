$output.java("${configuration.rootPackage}.rest.controller.security", "SecurityController")##

$output.require("java.security.Principal")##
$output.require("org.springframework.web.bind.annotation.RequestMapping")##
$output.require("org.springframework.web.bind.annotation.RestController")##

@RestController
public class SecurityController {

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}