$output.java("${configuration.rootPackage}.security", "UserDetailsService")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.security.core.GrantedAuthority")##
$output.require("org.springframework.security.core.authority.SimpleGrantedAuthority")##
$output.require("org.springframework.security.core.userdetails.UserDetails")##
$output.require("org.springframework.security.core.userdetails.UsernameNotFoundException")##
$output.require("org.springframework.stereotype.Component")##

$output.require("${configuration.rootPackage}.jpa.model.AppAuthority")##
$output.require("${configuration.rootPackage}.jpa.model.AppUser")##
$output.require("${configuration.rootPackage}.jpa.repository.AppUserJpaRepository")##
$output.require("${configuration.rootPackage}.security.exception.UserNotEnabledException")##

$output.require("java.util.ArrayList")##
$output.require("java.util.Collection")##

$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("javax.transaction.Transactional")##

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private AppUserJpaRepository appUserJpaRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        AppUser appUser = appUserJpaRepository.findByLogin(login);
        
        if (appUser == null) {
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        } else if (appUser.getEnabled() != 1) {
            throw new UserNotEnabledException("User " + login + " was not enabled");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (AppAuthority appAuthority : appUser.getAppAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(appAuthority.getName());
            grantedAuthorities.add(grantedAuthority);
            
            log.debug("add: " + appAuthority.getName() + ", to user with login: " + login);
        }
        
        return new org.springframework.security.core.userdetails.User(login, appUser.getPassword(),
                grantedAuthorities);
    }
}