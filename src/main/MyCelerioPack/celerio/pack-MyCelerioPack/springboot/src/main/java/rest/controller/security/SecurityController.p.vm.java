$output.java("${configuration.rootPackage}.rest.controller.security", "SecurityController")##

$output.require("java.util.List")##

$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.security.access.prepost.PreAuthorize")##
$output.require("org.springframework.web.bind.annotation.RequestMapping")##
$output.require("org.springframework.web.bind.annotation.RequestMethod")##
$output.require("org.springframework.web.bind.annotation.ResponseBody")##

$output.require("${configuration.rootPackage}.jpa.model.AppToken")##
$output.require("${configuration.rootPackage}.jpa.model.AppUser")##
$output.require("${configuration.rootPackage}.jpa.repository.AppTokenJpaRepository")##
$output.require("${configuration.rootPackage}.jpa.repository.AppUserJpaRepository")##
$output.require("${configuration.rootPackage}.security.SecurityUtils")##

$output.require("org.springframework.web.bind.annotation.RequestMapping")##
$output.require("org.springframework.web.bind.annotation.RestController")##

@RestController
public class SecurityController {

	@Autowired
    private AppUserJpaRepository appUserJpaRepository;


    @Autowired
    private AppTokenJpaRepository appTokenJpaRepository;

    @RequestMapping(value = "/security/account", method = RequestMethod.GET)
    public @ResponseBody
    AppUser getUserAccount()  {
    		AppUser appUser = appUserJpaRepository.findByLogin(SecurityUtils.getCurrentLogin());
    		appUser.setPassword(null);
        return appUser;
    }


    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/security/tokens", method = RequestMethod.GET)
    public @ResponseBody
    List<AppToken> getTokens () {
        List<AppToken> tokens = appTokenJpaRepository.findAll();
        for(AppToken t:tokens) {
            t.setId(null);
            t.setTokenValue(null);
        }
        return tokens;
    }
}