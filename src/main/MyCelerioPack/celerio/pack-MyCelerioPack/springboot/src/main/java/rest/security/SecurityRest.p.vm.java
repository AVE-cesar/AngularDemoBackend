$output.java("${configuration.rootPackage}.rest.security", "SecurityRest")##

$output.require("java.security.Principal")##
$output.require("org.springframework.web.bind.annotation.RequestMapping")##
$output.require("org.springframework.web.bind.annotation.RestController")##

@RestController
public class SecurityRest {

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}