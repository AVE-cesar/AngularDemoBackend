$output.javaTest("${configuration.rootPackage}.elasticsearch.repository", "${entity.model.type}ElasticsearchRepositoryTest")##

$output.require("static org.assertj.core.api.Assertions.assertThat")##

$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.boot.test.context.SpringBootTest")##
$output.require("org.springframework.test.context.ActiveProfiles")##
$output.require("org.springframework.test.context.junit4.SpringRunner")##

$output.require("${configuration.rootPackage}.Application")##
$output.require("${configuration.rootPackage}.elasticsearch.model.${entity.model.type}")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityTestUtils")##

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "TEST")
@SpringBootTest(classes = Application.class)
public class ${entity.model.type}ElasticsearchRepositoryTest {

	@Autowired
	private ${entity.model.type}ElasticsearchRepository ${entity.model.var}ElasticsearchRepository;
	
	@Test
	public void testFind() {
		${entity.model.type} ${entity.model.var} = ${entity.model.type}EntityTestUtils.createNewElasticsearch${entity.model.type}("1");
		${entity.model.type} saved${entity.model.type} = ${entity.model.var}ElasticsearchRepository.save(${entity.model.var});
		
		assertThat(saved${entity.model.type}).isEqualTo(${entity.model.var});
	}
}
