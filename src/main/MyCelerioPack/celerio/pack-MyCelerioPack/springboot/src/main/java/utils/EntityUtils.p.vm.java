$output.java("${configuration.rootPackage}.utils", "EntityUtils")##

$output.require("java.math.BigDecimal")##

$output.require("com.jaxio.demo.jpa.model.Book")##

public class EntityUtils {

	public static Book createNewBook(String id) {
		Book book = new Book();
		book.setId(id);
		book.setTitle("Title "+id);
		book.setPrice(new BigDecimal(1));
		book.setDescription("a detailed description !");
		
		return book;
	}
	
	public static com.jaxio.demo.elasticsearch.model.Book createNewElasticsearchBook(String id) {
		return new com.jaxio.demo.elasticsearch.model.Book(id, "Titre "+id);
	}
	
	public static com.jaxio.demo.elasticsearch.model.Book convertToElasticsearchBook (Book book) {
		com.jaxio.demo.elasticsearch.model.Book elasticsearchBook = new com.jaxio.demo.elasticsearch.model.Book();
		
		elasticsearchBook.setId(book.getId());
		elasticsearchBook.setTitle(book.getTitle());
		
		return elasticsearchBook;
	}
}
