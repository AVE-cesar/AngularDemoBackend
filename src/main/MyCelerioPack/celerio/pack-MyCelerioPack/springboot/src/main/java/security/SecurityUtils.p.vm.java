$output.java("${configuration.rootPackage}.security", "SecurityUtils")##

$output.require("static ${configuration.rootPackage}.config.Constants.CONTENT_TYPE")##

$output.require("com.fasterxml.jackson.databind.ObjectMapper")##
$output.require("${configuration.rootPackage}.security.model.Error")##
$output.require("${configuration.rootPackage}.security.model.Response")##
$output.require("org.springframework.security.core.Authentication")##
$output.require("org.springframework.security.core.context.SecurityContext")##
$output.require("org.springframework.security.core.context.SecurityContextHolder")##
$output.require("org.springframework.security.core.userdetails.UserDetails")##

$output.require("javax.servlet.http.HttpServletResponse")##
$output.require("java.io.IOException")##
$output.require("java.io.PrintWriter")##

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private static final ObjectMapper mapper = new ObjectMapper();
    
    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails springSecurityUser = null;
        String userName = null;

        if(authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    public static void sendError(HttpServletResponse response, Exception exception, int status, String message) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        Error error = new Error("authError", exception.getMessage());
        writer.write(mapper.writeValueAsString(new Response(status, message, error)));
        writer.flush();
        writer.close();
    }

    public static void sendResponse(HttpServletResponse response, int status, Object object) throws IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(object));
        response.setStatus(status);
        writer.flush();
        writer.close();
    }
}
