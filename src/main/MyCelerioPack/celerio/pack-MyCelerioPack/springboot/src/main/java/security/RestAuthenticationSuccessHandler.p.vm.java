$output.java("${configuration.rootPackage}.security", "RestAuthenticationSuccessHandler")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.security.core.Authentication")##
$output.require("org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler")##
$output.require("org.springframework.stereotype.Component")##

$output.require("${configuration.rootPackage}.jpa.model.AppUser")##
$output.require("${configuration.rootPackage}.jpa.repository.AppUserJpaRepository")##

$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("javax.servlet.ServletException")##
$output.require("javax.servlet.http.HttpServletRequest")##
$output.require("javax.servlet.http.HttpServletResponse")##
$output.require("java.io.IOException")##

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private AppUserJpaRepository appUserJpaRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        
    	log.info("inside RestAuthenticationSuccessHandler.onAuthenticationSuccess");
    	
    	// try to find a corresponding user with a login
    	AppUser appUser = appUserJpaRepository.findByLogin(authentication.getName());
        
    	// send this user back to the browser 
        SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, appUser);
    }
}
