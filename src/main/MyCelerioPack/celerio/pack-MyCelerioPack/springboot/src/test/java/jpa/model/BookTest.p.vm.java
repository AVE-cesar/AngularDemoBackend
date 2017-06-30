$output.javaTest("${configuration.rootPackage}.jpa.model", "BookTest")##

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


import com.jaxio.demo.jpa.model.Book;
import com.jaxio.demo.utils.BookEntityUtils;

public class BookTest {

    @Test
    public void testEquals() {
        Book book1 = BookEntityUtils.createNewBook();
        Book book2 = BookEntityUtils.createNewBook();
        
        assertTrue(book1.equals(book1));
        assertFalse(book1.equals(book2));
        
    }
    
    @Test
    public void testHashCode() {
        Book book1 = BookEntityUtils.createNewBook();
        Book book2 = BookEntityUtils.createNewBook();
        
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
    public void testSetters() {
    		Book book = new Book();
    		
    		Double value = new Double(12.2564789);
    		book.setPrice(value);
    		
    		assertTrue(value.equals(book.getPrice()));
    		assertNotNull(book.getPrice());
    }
}
