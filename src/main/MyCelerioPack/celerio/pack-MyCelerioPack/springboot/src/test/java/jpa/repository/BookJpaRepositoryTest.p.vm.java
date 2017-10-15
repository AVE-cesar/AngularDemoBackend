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
	
	// Many to one
	@Autowired
	private AuthorJpaRepository authorRepository;

	@Test
	public void testSave() {
		bookRepository.save(BookEntityTestUtils.createNewBook());
	}

	@Test
	public void testUpdate() {
		Book book = BookEntityTestUtils.createNewBook();
		Book book1 = bookRepository.save(book);
		
		Book book2 = bookRepository.save(book1);
		
		assertThat(book2).isNotEqualTo(book);
	}

	@Test
	public void testDelete() {
		Book book = BookEntityTestUtils.createNewBook();
		Book savedBook = bookRepository.save(book);
		//assertThat(bookRepository.findOne(savedBook.getId())).isEqualTo(book);
		
		bookRepository.delete(savedBook);
		
		assertThat(bookRepository.findOne(book.getId())).isNull();
	}
	
	@Test
	public void should_store_a_book() {
		Book book = BookEntityTestUtils.createNewBook();

		// we must create the author before the book
		Author savedAuthor = authorRepository.save(book.getAuthor());
		assertThat(savedAuthor).hasFieldOrPropertyWithValue("name", book.getAuthor().getName());
		book.setAuthor(savedAuthor);
		
		Book savedBook = bookRepository.save(book);

		assertThat(savedBook).hasFieldOrPropertyWithValue("title", book.getTitle());
		assertThat(savedBook).hasFieldOrPropertyWithValue("author", book.getAuthor());
		assertThat(savedBook.getAuthor()).hasFieldOrPropertyWithValue("id", savedAuthor.getId());
	}

	@Test
	public void should_delete_all_books() {
		Book book1 = BookEntityTestUtils.createNewBook();

		bookRepository.save(book1);

		Book book2 = BookEntityTestUtils.createNewBook();

		bookRepository.save(book2);

		bookRepository.deleteAll();

		assertThat(bookRepository.findAll()).isEmpty();
	}

	@Test
	public void should_find_all_books() {
		long bookCount = bookRepository.count();
		
		log.debug("book count: " + bookCount);
		
		Book book1 = BookEntityTestUtils.createNewBook();

		bookRepository.save(book1);

		Book book2 = BookEntityTestUtils.createNewBook();

		bookRepository.save(book2);
		
		Book book3 = BookEntityTestUtils.createNewBook();

		bookRepository.save(book3);

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
