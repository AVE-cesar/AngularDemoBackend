$output.javaTest("${configuration.rootPackage}.jpa.model", "${entity.model.type}Test")##

$output.require("org.junit.Test")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##

$output.require("static org.junit.Assert.assertTrue")##

$output.require("java.util.Set")##

$output.require("javax.validation.ConstraintViolation")##
$output.require("javax.validation.Validation")##
$output.require("javax.validation.Validator")##
$output.require("javax.validation.ValidatorFactory")##

$output.require("static org.junit.Assert.assertFalse")##
$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}Test")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityTestUtils")##

public class ${entity.model.type}Test {

	private final Logger LOGGER = LoggerFactory.getLogger(${entity.model.type}Test.class);
	
	@Test
	public void testEquals() {
		${entity.model.type} book1 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.type} book2 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		
		assertTrue(book1.equals(book1));
		assertFalse(book1.equals(book2));

	}

	@Test
	public void testHashCode() {
		${entity.model.type} book1 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.type} book2 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		
		book2.setId(book1.getId());
		book2.setTitle(book1.getTitle());
		book2.setDescription(book1.getDescription());
		book2.setPrice(book1.getPrice());
		book2.setPublicationDate(book1.getPublicationDate());
		
		// One to one relation
		book2.setBarcode(book1.getBarcode());
		
		// Many to one relation
		book2.setAuthor(book1.getAuthor());
		
		assertTrue(book1.equals(book2));
		assertTrue(book1.hashCode() == book2.hashCode());
		
	}

	@Test
	public void testBeanValidation() {
		${entity.model.type} book = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		// FIXME: il faut enlever la ligne suivante et trouver pourquoi cette méthode plante
		//book.setPrice(new Double(1));
		book.setPrice(Integer.valueOf(1));
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<${entity.model.type}>> constraintViolations = validator.validate(book);
		
		if (constraintViolations.size() > 0 ) {
			
			for (ConstraintViolation<${entity.model.type}> constraints : constraintViolations) {
				LOGGER.debug(constraints.getRootBeanClass().getSimpleName() + "." + constraints.getPropertyPath().toString() + " " + constraints.getMessage());
			}
			assertTrue(constraintViolations.size() == 0);
		}
	}
}
