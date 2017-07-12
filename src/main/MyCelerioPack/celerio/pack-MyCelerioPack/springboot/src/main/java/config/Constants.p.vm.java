$output.java("${configuration.rootPackage}.config", "Constants")##

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast"
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";

    // Role for ADMIN features
    public static final String ADMIN = "ADMIN";
    
    // Cookie name
    public static final String REMEMBER_ME_KEY = "rememberme_key";
    
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    
    private Constants() {
    }
}