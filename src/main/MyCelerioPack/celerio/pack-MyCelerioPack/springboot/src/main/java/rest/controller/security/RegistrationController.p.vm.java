$output.java("${configuration.rootPackage}.rest.controller.security", "RegistrationController")##

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jaxio.demo.jpa.model.AppUser;
import com.jaxio.demo.jpa.repository.AppUserJpaRepository;
import com.jaxio.demo.service.admin.MailService;

@RestController
public class RegistrationController {

	private final Logger log = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	private AppUserJpaRepository appUserJpaRepository;

	@Autowired
	private MailService mailService;

	/**
	 * Create a new account and send an email to the user.
	 */
	@RequestMapping(value = "/createLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppUser> createLogin(@RequestBody AppUser appUser) throws URISyntaxException {
		log.debug("CreateLogin for user : {}", appUser);

		// step 1: create the users
		appUser.setEnabled(0);
		AppUser result = appUserJpaRepository.save(appUser);

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

		AppUser fullyLoadedAppUser = appUserJpaRepository.findOne(id);

		fullyLoadedAppUser.setEnabled(1);

		return ResponseEntity.ok().body(fullyLoadedAppUser);
	}
}