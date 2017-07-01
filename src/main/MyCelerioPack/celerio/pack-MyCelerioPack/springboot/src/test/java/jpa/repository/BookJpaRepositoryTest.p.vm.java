$output.javaTest("${configuration.rootPackage}.jpa.repository", "BookJpaRepositoryTest")##

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jaxio.demo.jpa.model.Book;
import com.jaxio.demo.jpa.repository.BookJpaRepository;
import com.jaxio.demo.utils.BookEntityTestUtils;


@RunWith(SpringRunner.class)
@DataJpaTest
public class BookJpaRepositoryTest {

	@Autowired
	private BookJpaRepository repository;

	@Test
	public void testSave() {
		repository.save(BookEntityTestUtils.createNewBook());
	}

	@Test
	public void testUpdate() {
		Book book = BookEntityTestUtils.createNewBook();
		Book book1 = repository.save(book);
		
		Book book2 = repository.save(book1);
		
		assertThat(book2).isNotEqualTo(book);
	}
/*
	@Test
	public void testDelete() {
		Book book = BookEntityTestUtils.createNewBook();
		repository.save(book);
		assertThat(repository.findOne(book.getId())).isEqualTo(book);
		
		repository.delete(book);
		
		assertThat(repository.findOne(book.getId())).isNull();
	}

	
	@Test
	public void should_store_a_customer() {
		Book book = BookEntityTestUtils.createNewBook();

		Book savedBook = repository.save(book);

		assertThat(savedBook).hasFieldOrPropertyWithValue("title", book.getTitle());
		assertThat(savedBook).hasFieldOrPropertyWithValue("authorId", book.getAuthorId());
	}

	@Test
	public void should_delete_all_customer() {
		Book book1 = BookEntityTestUtils.createNewBook();

		repository.save(book1);

		Book book2 = BookEntityUtils.createNewBook();

		repository.save(book2);

		repository.deleteAll();

		assertThat(repository.findAll()).isEmpty();
	}

	@Test
	public void should_find_all_customers() {
		Book book1 = BookEntityTestUtils.createNewBook();

		repository.save(book1);

		Book book2 = BookEntityTestUtils.createNewBook();

		repository.save(book2);
		
		Book book3 = BookEntityTestUtils.createNewBook();

		repository.save(book3);

		Iterable<Book> books = repository.findAll();

		assertThat(books).hasSize(3).contains(book1, book2, book3);
	}

	@Test
	public void should_find_Book_by_id() {
		Book book = BookEntityUtils.createNewBook();

		repository.save(book);

		Book foundBook = repository.findOne(book.getId());

		assertThat(foundBook).isEqualTo(book);
	}
*/
}
