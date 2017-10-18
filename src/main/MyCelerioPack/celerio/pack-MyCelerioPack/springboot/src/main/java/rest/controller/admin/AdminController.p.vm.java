$output.java("${configuration.rootPackage}.rest.controller.admin", "AdminController")##

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Allows to make some search inside Spring beans.
 * 
 * @author avebertrand
 *
 */
@RestController
@RequestMapping("/admin/adminController")
public class AdminController {

	private final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
    private ApplicationContext appContext;
	
    /**
     * Get all Spring beans.
     */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> findAllBeans(@RequestParam String filter) {
        log.debug("Find all Spring beans");
        List<String> list = new ArrayList<>();
        		
        
        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);

        for (String bean : beans) {
            Object object = appContext.getBean(bean);
            String beanName = null;
            if (object != null) {
                beanName = bean + " of Type :: " + object.getClass();
            } else {
                beanName = bean + " of Type :: " + object;
            }
            
            if (!"%".equals(filter)) {
            		// we must use the filter
            		if (beanName.toUpperCase().indexOf(filter.toUpperCase()) > 0) {
            			list.add(beanName);
            		}
            } else {
            		list.add(beanName);
            }      
        }
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }
}
