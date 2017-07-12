$output.java("${configuration.rootPackage}.security", "RestAccessDeniedHandler")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.security.access.AccessDeniedException")##
$output.require("org.springframework.security.web.access.AccessDeniedHandler")##
$output.require("org.springframework.stereotype.Component")##

$output.require("javax.servlet.ServletException")##
$output.require("javax.servlet.http.HttpServletRequest")##
$output.require("javax.servlet.http.HttpServletResponse")##
$output.require("java.io.IOException")##

/**
 * The access denied handler returns http status 403.
 * @author egd
 *
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
    	
    	log.info("inside RestAccessDeniedHandler.handle");
    	
    	// send a 403 back to the browser 
        SecurityUtils.sendError(response, exception, HttpServletResponse.SC_FORBIDDEN, "Not authorized resources");
    }
}
