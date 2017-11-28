$output.java("${configuration.rootPackage}.security.exception", "UserLockedException")##

$output.require("org.springframework.security.core.AuthenticationException")##

public class UserLockedException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6787575051079597535L;

    public UserLockedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserLockedException(String msg) {
        super(msg);
    }
}
