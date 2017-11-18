$output.java("${configuration.rootPackage}.scheduledtasks", "RegistrationHouseHoldTask")##

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jaxio.demo.jpa.model.AppUser;
import com.jaxio.demo.jpa.repository.AppUserJpaRepository;

@Component
public class RegistrationHouseHoldTask {

	private static final Logger log = LoggerFactory.getLogger(RegistrationHouseHoldTask.class);
	
    @Autowired
    private AppUserJpaRepository appUserJpaRepository;
	
	/**
	 * Clean pending user registration.
	 * fixedDelay is in milliseconds.
	 */
	@Scheduled(fixedDelay = 300000)
	public void scheduleTaskWithFixedRate() {
		log.debug("RegistrationHouseHoldTask starting");
		log.debug("Current Thread : {}", Thread.currentThread().getName());
		
		// we need to delete users that are still disable since the last household.
		List<AppUser> appUsers = appUserJpaRepository.findByEnabled(0);
		log.debug("We have to delete {} incomplete registration.", appUsers.size());
		appUserJpaRepository.delete(appUsers);
		
		log.debug("RegistrationHouseHoldTask stopping");
	}
}
