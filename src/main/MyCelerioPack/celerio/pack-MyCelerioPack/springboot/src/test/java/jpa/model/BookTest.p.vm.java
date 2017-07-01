$output.javaTest("${configuration.rootPackage}.jpa.model", "BookTest")##

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.Assert.assertFalse;

import com.jaxio.demo.jpa.model.Book;
import com.jaxio.demo.jpa.model.BookTest;
import com.jaxio.demo.utils.BookEntityTestUtils;

public class BookTest {

	private final Logger LOGGER = LoggerFactory.getLogger(BookTest.class);
	
    @Test
    public void testEquals() {
        Book book1 = BookEntityTestUtils.createNewBook();
        Book book2 = BookEntityTestUtils.createNewBook();
        
        assertTrue(book1.equals(book1));
        assertFalse(book1.equals(book2));
        
    }
    
    @Test
    public void testHashCode() {
        Book book1 = BookEntityTestUtils.createNewBook();
        Book book2 = BookEntityTestUtils.createNewBook();
        
        book2.setId(book1.getId());
        book2.setTitle(book1.getTitle());
        book2.setDescription(book1.getDescription());
        book2.setPrice(book1.getPrice());
        book2.setAuthorId(book1.getAuthorId());
        book2.setBarcodeid(book1.getBarcodeid());
        book2.setPublicationDate(book1.getPublicationDate());
        
        assertTrue(book1.equals(book2));
        assertTrue(book1.hashCode() == book2.hashCode());
        
    }
    
    @Test
    public void testBeanValidation() {
        Book book = BookEntityTestUtils.createNewBook();
        // FIXME: il faut enlever la ligne suivante et trouver pourquoi cette méthode plante
        //book.setPrice(new Double(1));
        book.setPrice(Integer.valueOf(1));
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Book>> constraintViolations = validator.validate(book);
        
        if (constraintViolations.size() > 0 ) {
        		
        		for (ConstraintViolation<Book> contraintes : constraintViolations) {
        			LOGGER.debug(contraintes.getRootBeanClass().getSimpleName() + "." + contraintes.getPropertyPath().toString() + " " + contraintes.getMessage());
        		}
        		assertTrue(constraintViolations.size() == 0);
        	}
    }
}
