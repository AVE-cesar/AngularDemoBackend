$output.java("${configuration.rootPackage}.security", "RememberMeServices")##

$output.require("static com.jaxio.demo.config.Constants.REMEMBER_ME_KEY")##

$output.require("java.security.SecureRandom")##
$output.require("java.util.Arrays")##
$output.require("java.util.Date")##

$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("javax.servlet.http.HttpServletRequest")##
$output.require("javax.servlet.http.HttpServletResponse")##

$output.require("org.apache.commons.lang3.time.DateUtils")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.core.env.Environment")##
$output.require("org.springframework.security.core.Authentication")##
$output.require("org.springframework.security.core.userdetails.UserDetails")##
$output.require("org.springframework.security.crypto.codec.Base64")##
$output.require("org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices")##
$output.require("org.springframework.security.web.authentication.rememberme.CookieTheftException")##
$output.require("org.springframework.security.web.authentication.rememberme.InvalidCookieException")##
$output.require("org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException")##
$output.require("org.springframework.stereotype.Service")##

$output.require("${configuration.rootPackage}.jpa.model.AppToken")##
$output.require("${configuration.rootPackage}.jpa.model.AppUser")##
$output.require("${configuration.rootPackage}.jpa.repository.AppTokenJpaRepository")##
$output.require("${configuration.rootPackage}.jpa.repository.AppUserJpaRepository")##
$output.require("com.jaxio.demo.security.RememberMeServices")##

/**
 * Custom implementation of Spring Security's RememberMeServices.
 * <p/>
 * Persistent tokens are used by Spring Security to automatically log in users.
 * <p/>
 * This is a specific implementation of Spring Security's remember-me authentication, but it is much
 * more powerful than the standard implementations:
 * <ul>
 * <li>It allows a user to see the list of his currently opened sessions, and invalidate them</li>
 * <li>It stores more information, such as the IP address and the user agent, for audit purposes<li>
 * <li>When a user logs out, only his current session is invalidated, and not all of his sessions</li>
 * </ul>
 * <p/>
 */
@Service
public class RememberMeServices extends
        AbstractRememberMeServices {

    private final Logger log = LoggerFactory.getLogger(RememberMeServices.class);

    // FIXME use the service level
    @Autowired
    private AppUserJpaRepository appUserJpaRepository;
    
    // FIXME use the service level
    @Autowired
    private AppTokenJpaRepository appTokenJpaRepository;
    
    // Token is valid for one month
    private static final int TOKEN_VALIDITY_DAYS = 31;

    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;

    private static final int DEFAULT_SERIES_LENGTH = 16;

    private static final int DEFAULT_TOKEN_LENGTH = 16;

    private SecureRandom random;

    @Autowired
    public RememberMeServices(Environment env, org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        super(REMEMBER_ME_KEY, userDetailsService);
        super.setParameter("rememberme");
        random = new SecureRandom();
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {

        AppToken appToken = getPersistentToken(cookieTokens);
        String login = appToken.getUserLogin();

        // Token also matches, so login is valid. Update the token value, keeping the *same* series number.
        log.debug("Refreshing persistent login token for user '{}', series '{}'", login, appToken.getId());
        appToken.setTokenCreationDate(new Date());
        appToken.setTokenValue(generateTokenData());
        appToken.setIpAddress(request.getRemoteAddr());
        appToken.setUserAgent(request.getHeader("User-Agent"));
        try {
        		appTokenJpaRepository.save(appToken);
            addCookie(appToken, request, response);
        } catch (Exception e) {
            log.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
        }
        return getUserDetailsService().loadUserByUsername(login);
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        String login = successfulAuthentication.getName();

        log.debug("Creating new persistent login for user {}", login);
        AppUser appUser = appUserJpaRepository.findByLogin(login);
        // update the last login date for this user
        appUser.setLastLoginDate(new Date());
        appUserJpaRepository.save(appUser);
        
        AppToken appToken = new AppToken();
        appToken.setId(generateSeriesData());
        appToken.setUserLogin(appUser.getLogin());
        appToken.setTokenValue(generateTokenData());
        appToken.setTokenCreationDate(new Date());
        appToken.setIpAddress(request.getRemoteAddr());
        appToken.setUserAgent(request.getHeader("User-Agent"));
        try {
            appTokenJpaRepository.save(appToken);
            addCookie(appToken, request, response);
        } catch (Exception e) {
            log.error("Failed to save persistent token ", e);
        }
    }

    /**
     * When logout occurs, only invalidate the current token, and not all user sessions.
     * <p/>
     * The standard Spring Security implementations are too basic: they invalidate all tokens for the
     * current user, so when he logs out from one browser, all his other sessions are destroyed.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(rememberMeCookie);
                AppToken appToken = getPersistentToken(cookieTokens);
                appTokenJpaRepository.delete(appToken.getId());
            } catch (InvalidCookieException ice) {
                log.info("Invalid cookie, no persistent token could be deleted");
            } catch (RememberMeAuthenticationException rmae) {
                log.debug("No persistent token found, so no token could be deleted");
            }
        }
        super.logout(request, response, authentication);
    }

    /**
     * Validate the token and return it.
     */
    private AppToken getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        AppToken appToken = null;
        try {
        	appToken = appTokenJpaRepository.findOne(presentedSeries);
        } catch (Exception e) {
            log.error("Error to access database", e );
        }

        if (appToken == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        log.info("presentedToken={} / tokenValue={}", presentedToken, appToken.getTokenValue());
        if (!presentedToken.equals(appToken.getTokenValue())) {
            // Token doesn't match series value. Delete this session and throw an exception.
        		appTokenJpaRepository.delete(appToken.getId());
            throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
        }

        if (DateUtils.addDays(appToken.getTokenCreationDate(), TOKEN_VALIDITY_DAYS).before(new Date())) {
        		appTokenJpaRepository.delete(appToken.getId());
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }
        return appToken;
    }

    private String generateSeriesData() {
        byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

    private void addCookie(AppToken appToken, HttpServletRequest request, HttpServletResponse response) {
        setCookie(
                new String[]{appToken.getId(), appToken.getTokenValue()},
                TOKEN_VALIDITY_SECONDS, request, response);
    }
}