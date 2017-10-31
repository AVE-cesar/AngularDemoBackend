$output.javaTest("${configuration.rootPackage}.jpa.repository", "${entity.model.type}JpaRepositoryTest")##

$output.require("static org.assertj.core.api.Assertions.assertThat")##

$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest")##
$output.require("org.springframework.test.context.junit4.SpringRunner")##

$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require("${configuration.rootPackage}.jpa.repository.${entity.model.type}JpaRepository")##
$output.require("${configuration.rootPackage}.jpa.repository.AuthorJpaRepository")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityTestUtils")##

@RunWith(SpringRunner.class)
@DataJpaTest
public class ${entity.model.type}JpaRepositoryTest {

	private final Logger log = LoggerFactory.getLogger(${entity.model.type}JpaRepositoryTest.class);
	
	@Autowired
	private ${entity.model.type}JpaRepository ${entity.model.var}Repository;

	// One to one relation
#foreach ($relation in $entity.oneToOne.list)
	@Autowired
	private ${relation.to.type}JpaRepository ${relation.to.var}Repository;
#end

	// Many to one relation
#foreach ($relation in $entity.ManyToOne.list)
	@Autowired
	private ${relation.to.type}JpaRepository ${relation.to.var}Repository;
#end

	@Test
	public void testSave() {
		${entity.model.type} saved${entity.model.type} = ${entity.model.var}Repository.save(${entity.model.type}EntityTestUtils.createNew${entity.model.type}());
		
		assertThat(${entity.model.var}Repository.findOne(saved${entity.model.type}.getId())).isNotNull();
	}

	@Test
	public void testUpdate() {
		${entity.model.type} ${entity.model.var} = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.type} ${entity.model.var}1 = ${entity.model.var}Repository.save(${entity.model.var});
		
		${entity.model.type} ${entity.model.var}2 = ${entity.model.var}Repository.save(${entity.model.var}1);
		
		assertThat(${entity.model.var}2).isNotEqualTo(${entity.model.var});
		
		String value = "test";
#set( $currentAttribute = "" )
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if (!$attribute.isInFk() && !$attribute.isInCpk() && !$attribute.isInPk())
	// $attribute.name
	${entity.model.var}1.${attribute.setter}(value);
	#set( $currentAttribute = $attribute.name )
	#break
#end
#end

		${entity.model.type} ${entity.model.var}3 = ${entity.model.var}Repository.save(${entity.model.var}1);
		assertThat(${entity.model.var}3).hasFieldOrPropertyWithValue("id", ${entity.model.var}1.getId());
#if (!$currentAttribute.equals(""))
		assertThat(${entity.model.var}3).hasFieldOrPropertyWithValue("$currentAttribute", value);
#end
	}

	@Test
	public void testDelete() {
		${entity.model.type} ${entity.model.var} = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.type} saved${entity.model.type} = ${entity.model.var}Repository.save(${entity.model.var});
		assertThat(${entity.model.var}Repository.findOne(saved${entity.model.type}.getId())).isNotNull();
		
		${entity.model.var}Repository.delete(saved${entity.model.type});
		
		assertThat(${entity.model.var}Repository.findOne(${entity.model.var}.getId())).isNull();
	}
	
	@Test
	public void should_store_a_${entity.model.var}() {
		${entity.model.type} ${entity.model.var} = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();

		// One to one relation
#foreach ($relation in $entity.oneToOne.list)
		assertThat(${entity.model.var}).hasFieldOrPropertyWithValue("${relation.to.var}", ${entity.model.var}.get${relation.to.varUp}());
#end
		// Many to one relation
#foreach ($relation in $entity.manyToOne.list)
		assertThat(${entity.model.var}).hasFieldOrPropertyWithValue("${relation.to.var}", ${entity.model.var}.get${relation.to.varUp}());
#end

        // save the ${entity.model.var} and its linked entities into DB
        ${entity.model.type} saved${entity.model.type} = ${entity.model.var}Repository.save(${entity.model.var});

		// One to one relation
#foreach ($relation in $entity.oneToOne.list)
		// the linked ${relation.to.varUp} should find into the db
		assertThat(${relation.to.var}Repository.findOne(saved${entity.model.type}.get${relation.to.varUp}().getId())).isNotNull();
#end
		// Many to one relation
#foreach ($relation in $entity.manyToOne.list)
		// the linked ${relation.to.varUp} should find into the db
		assertThat(${relation.to.var}Repository.findOne(saved${entity.model.type}.get${relation.to.varUp}().getId())).isNotNull();
#end

		// One to one relation
#foreach ($relation in $entity.oneToOne.list)
		${entity.model.var}.get${relation.to.varUp}().setId(saved${entity.model.type}.get${relation.to.varUp}().getId());
#end
// Many to one relation
#foreach ($relation in $entity.manyToOne.list)
		// copy generated id from saved${entity.model.type} to original one
		${entity.model.var}.get${relation.to.varUp}().setId(saved${entity.model.type}.get${relation.to.varUp}().getId());
#end

#foreach ($attribute in $entity.nonCpkAttributes.list)
#if (!$attribute.isInFk() && !$attribute.isInCpk() && !$attribute.isInPk())
	// $attribute.name
	assertThat(saved${entity.model.type}).hasFieldOrPropertyWithValue("$attribute.name", ${entity.model.var}.${attribute.getter}());
	#break
#end
#end

		// One to one relation
#foreach ($relation in $entity.oneToOne.list)        
		assertThat(saved${entity.model.type}).hasFieldOrPropertyWithValue("${relation.to.var}", ${entity.model.var}.get${relation.to.varUp}());
#end
//Many to one relation
#foreach ($relation in $entity.manyToOne.list)
		assertThat(saved${entity.model.type}).hasFieldOrPropertyWithValue("${relation.to.var}", ${entity.model.var}.get${relation.to.varUp}());
#end
	}

	@Test
	public void should_delete_all_${entity.model.var}s() {
		// add some ${entity.model.var}s
		${entity.model.type} ${entity.model.var}1 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.var}Repository.save(${entity.model.var}1);

		${entity.model.type} ${entity.model.var}2 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.var}Repository.save(${entity.model.var}2);

		// and then, check method deleteAll
		${entity.model.var}Repository.deleteAll();

		assertThat(${entity.model.var}Repository.findAll()).isEmpty();
	}

	@Test
	public void should_find_all_${entity.model.var}s() {
		// memorize the number of existing ${entity.model.var}s
		long ${entity.model.var}Count = ${entity.model.var}Repository.count();
		log.debug("${entity.model.var} count: " + ${entity.model.var}Count);
		
		// then add some ${entity.model.var}s
		${entity.model.type} ${entity.model.var}1 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.var}Repository.save(${entity.model.var}1);
		${entity.model.type} ${entity.model.var}2 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.var}Repository.save(${entity.model.var}2);
		${entity.model.type} ${entity.model.var}3 = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		${entity.model.var}Repository.save(${entity.model.var}3);

		// and then, check method findAll
		Iterable<${entity.model.type}> ${entity.model.var}s = ${entity.model.var}Repository.findAll();

		assertThat(${entity.model.var}s).hasSize((int)(${entity.model.var}Count + 3));
	}

	@Test
	public void should_find_${entity.model.type}_by_id() {
		${entity.model.type} ${entity.model.var} = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();

		${entity.model.type} saved${entity.model.type} = ${entity.model.var}Repository.save(${entity.model.var});

		${entity.model.type} found${entity.model.type} = ${entity.model.var}Repository.findOne(saved${entity.model.type}.getId());

		assertThat(found${entity.model.type}).isEqualTo(saved${entity.model.type});
	}
}
