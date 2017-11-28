$output.java("${configuration.rootPackage}.security", "RestAuthenticationFailureHandler")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.security.core.AuthenticationException")##
$output.require("org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler")##
$output.require("org.springframework.stereotype.Component")##

$output.require("javax.servlet.ServletException")##
$output.require("javax.servlet.http.HttpServletRequest")##
$output.require("javax.servlet.http.HttpServletResponse")##
$output.require("java.io.IOException")##
$output.require("java.util.Map")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##

$output.require("${configuration.rootPackage}.jpa.model.AppUser")##
$output.require("${configuration.rootPackage}.jpa.repository.AppUserJpaRepository")##

/**
 * Returns a 401 error code (Unauthorized) to the client, when Ajax authentication fails.
 */
@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int MAX_ATTEMPTS = 3;

	@Autowired
	private AppUserJpaRepository appUserJpaRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		
		log.info("inside RestAuthenticationFailureHandler.onAuthenticationFailure");

		String login = request.getParameter("username");
		AppUser appUser = appUserJpaRepository.findByLogin(login);
		if (appUser != null) {
			int value = appUser.getNbAttempts() == null ? 0:appUser.getNbAttempts();
			appUser.setNbAttempts(value+1);
			if (value + 1 > MAX_ATTEMPTS) {
				appUser.setLocked(1);
			}
			appUserJpaRepository.save(appUser);
		}
		// send a 401 back to the browser 
		SecurityUtils.sendError(response, exception, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
	}
}
