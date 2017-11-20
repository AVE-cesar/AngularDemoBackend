$output.java("${configuration.rootPackage}.rest.controller.security", "RegistrationController")##

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jaxio.demo.jpa.model.AppUser;
import com.jaxio.demo.jpa.model.AppAuthority;
import com.jaxio.demo.service.AppUserService;
import com.jaxio.demo.service.admin.MailService;

@RestController
public class RegistrationController {

	private final Logger log = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	private AppUserService appUserService;

	@Autowired
	private MailService mailService;

	/**
	 * Create a new account and send an email to the user.
	 */
	@RequestMapping(value = "/createLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppUser> createLogin(@RequestBody AppUser appUser) throws URISyntaxException {
		log.debug("CreateLogin for user : {}", appUser);

		// step 1: create the users
		
		// must be disabled by default !
		appUser.setEnabled(0);

		// attach a role to the user
		AppAuthority appAuthority = new AppAuthority();
		// FIXME: use ENUM or Constant for ROLE
		appAuthority.setId(1);
		appAuthority.setName("user");
		List<AppAuthority> appAuthorities = new ArrayList<AppAuthority>();
		appAuthorities.add(appAuthority);
		appUser.setAppAuthorities(appAuthorities);

		// save the user into the database
		AppUser result = appUserService.create(appUser);

		// step 2: send the email
		Locale locale_US = new Locale("en", "US");
		Locale locale_FR = new Locale("fr", "FR");
		mailService.sendWelcomeEmail(appUser.getEmail(), Locale.FRENCH, result.getId());

		return ResponseEntity.created(new URI("/api/appUsers/" + result.getId())).body(result);
	}

	/**
	 * Registration from the welcome email.
	 */
	@RequestMapping(value = "/registration/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<AppUser> registration(@PathVariable Integer id) {
		log.debug("Registration for id: {}.", id);

		AppUser fullyLoadedAppUser = appUserService.findById(id);

		if (fullyLoadedAppUser != null) {
			fullyLoadedAppUser.setEnabled(1);
		} else {
			log.debug("The registration id {} doesn't exist !", id);
			return ResponseEntity.status(404).body(null);
		}

		return ResponseEntity.ok().body(fullyLoadedAppUser);
	}
	
	/**
	 * Check if a login already exists or not.
	 * Useful for a new user registration, to avoid duplicate login.
	 * 
	 * @return true if this login is available, false otherwise
	 */
	@RequestMapping(value = "/checkLoginAvailability/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkLoginAvailability(@PathVariable String login) {
			boolean available = true;
			log.debug("Checking login availability for: {}.", login);
		
			if (login != null) {
				AppUser appUser = appUserService.findByLoginIgnoreCase(login);
				if (appUser != null) {
					available = false;
				}
			} else {
				available = false;
			}
			
			log.debug("is login {} available ? {}.", login, available);
			
			return new ResponseEntity<>(available, new HttpHeaders(), HttpStatus.OK);
	}
}