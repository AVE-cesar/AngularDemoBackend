$output.javaTest("${configuration.rootPackage}.jpa.repository", "BookJpaRepositoryTest")##

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jaxio.demo.jpa.model.Book;
import com.jaxio.demo.jpa.model.Author;
import com.jaxio.demo.jpa.repository.BookJpaRepository;
import com.jaxio.demo.jpa.repository.AuthorJpaRepository;
import com.jaxio.demo.utils.BookEntityTestUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookJpaRepositoryTest {

	private final Logger log = LoggerFactory.getLogger(BookJpaRepositoryTest.class);
	
	@Autowired
	private BookJpaRepository bookRepository;
	
	// One to one
	@Autowired
	private BarCodeJpaRepository barcodeRepository;
	
	// Many to one
	@Autowired
	private AuthorJpaRepository authorRepository;

	@Test
	public void testSave() {
		Book savedBook = bookRepository.save(BookEntityTestUtils.createNewBook());
		
		assertThat(bookRepository.findOne(savedBook.getId())).isNotNull();
	}

	@Test
	public void testUpdate() {
		Book book = BookEntityTestUtils.createNewBook();
		Book book1 = bookRepository.save(book);
		
		Book book2 = bookRepository.save(book1);
		
		assertThat(book2).isNotEqualTo(book);
		
		String value = "test";
		book1.setDescription(value);
		Book book3 = bookRepository.save(book1);
		assertThat(book3).hasFieldOrPropertyWithValue("id", book1.getId());
		assertThat(book3).hasFieldOrPropertyWithValue("description", value);
	}

	@Test
	public void testDelete() {
		Book book = BookEntityTestUtils.createNewBook();
		Book savedBook = bookRepository.save(book);
		assertThat(bookRepository.findOne(savedBook.getId())).isNotNull();
		
		bookRepository.delete(savedBook);
		
		assertThat(bookRepository.findOne(book.getId())).isNull();
	}
	
	@Test
	public void should_store_a_book() {
        Book book = BookEntityTestUtils.createNewBook();

        assertThat(book).hasFieldOrPropertyWithValue("author", book.getAuthor());
        assertThat(book).hasFieldOrPropertyWithValue("barcode", book.getBarcode());

        // save the book and its linked entities into DB
        Book savedBook = bookRepository.save(book);
        // the linked Author should find into the db
        assertThat(authorRepository.findOne(savedBook.getAuthor().getId())).isNotNull();
        // the linked BarCode should find into the db
        assertThat(barcodeRepository.findOne(savedBook.getBarcode().getId())).isNotNull();

        // copy generated id from savedBook to original one
        book.getAuthor().setId(savedBook.getAuthor().getId());
        book.getBarcode().setId(savedBook.getBarcode().getId());
        
        assertThat(savedBook).hasFieldOrPropertyWithValue("title", book.getTitle());
        assertThat(savedBook).hasFieldOrPropertyWithValue("author", book.getAuthor());
        assertThat(savedBook).hasFieldOrPropertyWithValue("barcode", book.getBarcode());
	}

	@Test
	public void should_delete_all_books() {
		// add some books
		Book book1 = BookEntityTestUtils.createNewBook();
		bookRepository.save(book1);

		Book book2 = BookEntityTestUtils.createNewBook();
		bookRepository.save(book2);

		// and then, check method deleteAll
		bookRepository.deleteAll();

		assertThat(bookRepository.findAll()).isEmpty();
	}

	@Test
	public void should_find_all_books() {
		// memorize the number of existing books
		long bookCount = bookRepository.count();
		log.debug("book count: " + bookCount);
		
		// then add some books
		Book book1 = BookEntityTestUtils.createNewBook();
		bookRepository.save(book1);
		Book book2 = BookEntityTestUtils.createNewBook();
		bookRepository.save(book2);
		Book book3 = BookEntityTestUtils.createNewBook();
		bookRepository.save(book3);

		// and then, check method findAll
		Iterable<Book> books = bookRepository.findAll();

		assertThat(books).hasSize((int)(bookCount + 3));
	}

	@Test
	public void should_find_Book_by_id() {
		Book book = BookEntityTestUtils.createNewBook();

		Book savedBook = bookRepository.save(book);

		Book foundBook = bookRepository.findOne(savedBook.getId());

		assertThat(foundBook).isEqualTo(savedBook);
	}
}
