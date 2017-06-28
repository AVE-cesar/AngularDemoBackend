$output.javaTest("${configuration.rootPackage}.jpa.repository", "BookJpaRepositoryTest")##

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jaxio.demo.Application;
import com.jaxio.demo.elasticsearch.repository.BookElasticsearchRepository;
import com.jaxio.demo.jpa.model.Book;
import com.jaxio.demo.jpa.repository.BookJpaRepository;
import com.jaxio.demo.utils.BookEntityUtils;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = "TEST")
@SpringBootTest(classes = Application.class)
@EnableElasticsearchRepositories("com.jaxio.demo.elasticsearch.repository")
public class BookJpaRepositoryTest {

	@Autowired
	private BookJpaRepository repository;

	@Test
	public void testSave() {
		repository.save(BookEntityUtils.createNewBook("1"));
	}
/*
	@Test
	public void testUpdate() {
		Book book = EntityUtils.createNewBook("1");
		Book book1 = repository.save(book);
		
		Book book2 = repository.save(book1);
		
		assertThat(book2).isNotEqualTo(book);
	}
	
	@Test
	public void testDelete() {
		Book book = EntityUtils.createNewBook("1");
		repository.save(book);
		assertThat(repository.findOne(book.getId())).isEqualTo(book);
		
		repository.delete(book);
		
		assertThat(repository.findOne(book.getId())).isNull();
	}

	
	@Test
	public void should_store_a_customer() {
		Book book = EntityUtils.createNewBook("1");

		Book savedBook = repository.save(book);

		assertThat(savedBook).hasFieldOrPropertyWithValue("title", "Title 1");
		assertThat(savedBook).hasFieldOrPropertyWithValue("author", "Author 1");
	}

	@Test
	public void should_delete_all_customer() {
		Book book1 = EntityUtils.createNewBook("1");

		repository.save(book1);

		Book book2 = EntityUtils.createNewBook("2");

		repository.save(book2);

		repository.deleteAll();

		assertThat(repository.findAll()).isEmpty();
	}

	@Test
	public void should_find_all_customers() {
		Book book1 = EntityUtils.createNewBook("1");

		repository.save(book1);

		Book book2 = EntityUtils.createNewBook("2");

		repository.save(book2);
		
		Book book3 = EntityUtils.createNewBook("3");

		repository.save(book3);

		Iterable<Book> books = repository.findAll();

		assertThat(books).hasSize(3).contains(book1, book2, book3);
	}

	@Test
	public void should_find_Book_by_id() {
		Book book = EntityUtils.createNewBook("2");

		repository.save(book);

		Book foundBook = repository.findOne("2");

		System.out.println(foundBook);
		assertThat(foundBook).isEqualTo(book);
	}
	*/
}
