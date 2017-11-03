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
	
	private static final int NB = 10;
	
	@Test
	public void testEquals() {
		${entity.model.type} ${entity.model.var}1 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.type} ${entity.model.var}2 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		
		assertTrue(${entity.model.var}1.equals(${entity.model.var}1));
		assertFalse(${entity.model.var}1.equals(${entity.model.var}2));

	}

	@Test
	public void testHashCode() {
		${entity.model.type} ${entity.model.var}1 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.type} ${entity.model.var}2 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		
#foreach ($attribute in $entity.attributes.list)
	#if (!$attribute.isInFk())
		${entity.model.var}2.set${attribute.varUp}(${entity.model.var}1.get${attribute.varUp}());
	#end
#end	
	
		// One to one relation
#foreach ($relation in $entity.oneToOne.list)        
${entity.model.var}2.set${relation.to.varUp}(${entity.model.var}1.get${relation.to.varUp}());
#end	
		
		// Many to one relation
#foreach ($relation in $entity.manyToOne.list)
${entity.model.var}2.set${relation.to.varUp}(${entity.model.var}1.get${relation.to.varUp}());
#end
		
		assertTrue(${entity.model.var}1.equals(${entity.model.var}2));
		assertTrue(${entity.model.var}1.hashCode() == ${entity.model.var}2.hashCode());
		
	}

	@Test
	public void testBeanValidation() {
		// we test 10 several automatic generations to be more accurate
		for (int i = 0; i < NB; i++) {
			${entity.model.type} book = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
			
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
}
