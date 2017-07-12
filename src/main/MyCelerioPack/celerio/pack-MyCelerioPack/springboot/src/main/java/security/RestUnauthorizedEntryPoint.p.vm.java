$output.java("${configuration.rootPackage}.security", "RestUnauthorizedEntryPoint")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.security.core.AuthenticationException")##
$output.require("org.springframework.security.web.AuthenticationEntryPoint")##
$output.require("org.springframework.stereotype.Component")##

$output.require("javax.servlet.ServletException")##
$output.require("javax.servlet.http.HttpServletRequest")##
$output.require("javax.servlet.http.HttpServletResponse")##
$output.require("java.io.IOException")##

/**
 * Returns a 401 error code (Unauthorized) to the client.
 * 
 * We need a custom authenticationEntryPoint because default Spring-Security config will redirect to login page. 
 * In our case we need just a https status 401 and a json response.
 * 
 * @see SecurityConfig.configure(...)
 */
@Component
public class RestUnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private final Logger log = LoggerFactory.getLogger(RestUnauthorizedEntryPoint.class);
	
    /**
     * Always returns a 401 error code to the client.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
    	
    	log.info("inside RestUnauthorizedEntryPoint.commence");
    	
    	// send a 401 back to the browser 
        SecurityUtils.sendError(response, exception, HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication failed");
    }
}
