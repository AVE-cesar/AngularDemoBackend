$output.javaTest("${configuration.rootPackage}.rest.controller", "BookControllerTest")##

import static org.hamcrest.Matchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.jaxio.demo.Application;
import com.jaxio.demo.jpa.model.Book;
import com.jaxio.demo.rest.controller.BookController;
import com.jaxio.demo.utils.BookEntityUtils;

import com.jaxio.demo.utils.JsonUtils;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
//@WebMvcTest(BookController.class) ne marche pas
@ActiveProfiles(profiles = "TEST")
@SpringBootTest(classes = Application.class)
public class BookControllerTest {

	private final Logger log = LoggerFactory.getLogger(BookControllerTest.class);

	@Autowired
    private BookController bookController;

	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void contextLoads() {
		log.info("Ckecks if bookController is not null");
		assertThat(bookController).isNotNull();
	}

	@Test
	public void testFindAll() throws Exception {
		this.mockMvc.perform(get("/api/books/"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("")));
	}
	
	@Test
	public void testFindAllByPage() throws Exception {
		int size = 20;
		for (int i = 0; i < 3 * size; i++) {
			testCreate();
		}
				
		int firstPage = 0;
		// les pages commencent à zéro
		this.mockMvc.perform(get("/api/books/bypage")
			.param("page", Integer.toString(firstPage))
			.param("sort", "title")
			.param("size", Integer.toString(size)))
			.andDo(print())
			.andExpect(status().isOk())
			// nombre d'elements en retour
			.andExpect(jsonPath("$.content", hasSize(size)))
			.andExpect(jsonPath("$.numberOfElements", is(size)));
			// pour checker un attribut du premier element
			//.andExpect(jsonPath("$.content[0].description", is("a detailed description !")));
	}
	
	@Test
	public void testCreate() throws Exception {
		Random rand = new Random();
		Book book = BookEntityUtils.createNewBook("" + rand.nextInt());
		
		// https://www.petrikainulainen.net/programming/spring-framework/integration-testing-of-spring-mvc-applications-write-clean-assertions-with-jsonpath/
		this.mockMvc.perform(post("/api/books/")
			.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
			/* add a request parameter */
			.content(JsonUtils.convertObjectToJsonBytes(book)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
			// FIXME: marche plus
			//.andExpect(jsonPath("$.price", is(book.getPrice())));
	}
}
