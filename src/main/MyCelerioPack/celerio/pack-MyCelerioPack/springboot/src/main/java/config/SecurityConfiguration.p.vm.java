$output.java("${configuration.rootPackage}.config", "SecurityConfiguration")##

$output.require("${configuration.rootPackage}.config.Constants")##
$output.require("${configuration.rootPackage}.security.RestUnauthorizedEntryPoint")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.context.annotation.ComponentScan")##
$output.require("org.springframework.context.annotation.Configuration")##
$output.require("org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder")##
$output.require("org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity")##
$output.require("org.springframework.security.config.annotation.web.builders.HttpSecurity")##
$output.require("org.springframework.security.config.annotation.web.builders.WebSecurity")##
$output.require("org.springframework.security.config.annotation.web.configuration.EnableWebSecurity")##
$output.require("org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter")##
$output.require("org.springframework.security.core.userdetails.UserDetailsService")##
$output.require("org.springframework.security.web.access.AccessDeniedHandler")##
$output.require("org.springframework.security.web.authentication.AuthenticationFailureHandler")##
$output.require("org.springframework.security.web.authentication.AuthenticationSuccessHandler")##
$output.require("org.springframework.security.web.authentication.RememberMeServices")##
$output.require("org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler")##

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"${configuration.rootPackage}.security"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SecurityConfiguration() {
        super();
        logger.info("loading SecurityConfiguration ................................................ ");
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RestUnauthorizedEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;

    @Autowired
    private RememberMeServices rememberMeServices;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    	/**
    	 * pages or URL excluded from security mechanism
    	 */
        web.ignoring().antMatchers("/assets/**", "/bower_components/**", "/index.html", "/login.html",
                "/partials/**", "/template/**", "/", "/error/**", "/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/failure").permitAll()
                /* you must be a member of admin group to go to pages below */
                .antMatchers("/v2/api-docs").hasAnyAuthority("admin")
                .antMatchers("/users/**").hasAnyAuthority("admin")
                /*.antMatchers("/h2-console/**").hasAnyAuthority("admin")*/
                /* you must be authenticated for other pages */
                .anyRequest().authenticated()
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
            .and()
            .formLogin()
               	.loginProcessingUrl("/authenticate")
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(restAuthenticationFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
            /* configure logout: When logout succeeds, we need to return ok status instead of login page redirection.
Spring HttpStatusReturningLogoutSuccessHandler class  implements logout handler that returns ok status. */    
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .rememberMe()
                .rememberMeServices(rememberMeServices)
                .key(Constants.REMEMBER_ME_KEY);
    }
}
