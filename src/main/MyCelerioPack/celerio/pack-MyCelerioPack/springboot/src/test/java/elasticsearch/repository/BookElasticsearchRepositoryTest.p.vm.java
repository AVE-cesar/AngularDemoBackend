$output.javaTest("${configuration.rootPackage}.elasticsearch.repository", "BookElasticsearchRepositoryTest")##

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jaxio.demo.Application;
import com.jaxio.demo.elasticsearch.model.Book;
import com.jaxio.demo.utils.BookEntityUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "TEST")
@SpringBootTest(classes = Application.class)
public class BookElasticsearchRepositoryTest {

	@Autowired
    private ApplicationContext appContext;
	
	@Autowired
	private BookElasticsearchRepository bookElasticsearchRepository;
	
	@Before
	// FIXME déplacer dans une méthode utilitaire et créer un Rest Controller pour pouvoir afficher les beans Spring
	public void beans() {
		String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        
        for (String bean : beans) {
        	Object object = appContext.getBean(bean);
        	if (object != null) {
        		System.out.println(bean + " of Type :: " + object.getClass());
        	} else {
        		System.out.println(bean + " of Type :: " + object);
        	}
        }
	}
	
	@Test
	public void testFind() {
		Book book = BookEntityUtils.createNewElasticsearchBook("1");
		Book savedBook = bookElasticsearchRepository.save(book);
		
		assertThat(savedBook).isEqualTo(book);
	}
}
