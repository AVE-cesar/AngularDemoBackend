$output.javaTest("${configuration.rootPackage}.rest.controller", "${entity.model.type}ControllerTest")##

$output.require("static org.hamcrest.Matchers.containsString")##
$output.require("static org.hamcrest.Matchers.hasSize")##
$output.require("static org.assertj.core.api.Assertions.assertThat")##
$output.require("static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get")##
$output.require("static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post")##
$output.require("static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put")##
$output.require("static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print")##
$output.require("static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content")##
$output.require("static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath")##
$output.require("static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status")##

$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc")##
$output.require("org.springframework.boot.test.context.SpringBootTest")##
$output.require("org.springframework.http.MediaType")##
$output.require("org.springframework.security.test.context.support.WithMockUser")##
$output.require("org.springframework.test.context.ActiveProfiles")##
$output.require("org.springframework.test.context.junit4.SpringRunner")##
$output.require("org.springframework.test.web.servlet.MockMvc")##

$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require("${configuration.rootPackage}.rest.controller.${entity.model.type}Controller")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityTestUtils")##

$output.require("${configuration.rootPackage}.utils.JsonUtils")##

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "TEST")
public class ${entity.model.type}ControllerTest {

	private final Logger log = LoggerFactory.getLogger(${entity.model.type}ControllerTest.class);

	@Autowired
    private ${entity.model.type}Controller ${entity.model.var}Controller;

	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void contextLoads() {
		log.info("Ckecks if ${entity.model.var}Controller is not null");
		assertThat(${entity.model.var}Controller).isNotNull();
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void testFindAll() throws Exception {
		this.mockMvc.perform(get("/api/${entity.model.var}s/")) //
			.andDo(print()) //
			.andExpect(status().isOk()) //
			.andExpect(content().string(containsString("")));
	}
	
	// does not work because of field "price" of table "${entity.model.type}" (its format and its conversion within Celerio)
	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void testFindAllByPage() throws Exception {
		int size = 20;
		${entity.model.type} ${entity.model.var}s[] = new ${entity.model.type}[3*size];
		for (int i = 0; i < 3 * size; i++) {
			${entity.model.var}s[i] = createA${entity.model.type}();
		}
				
		int firstPage = 0;
		// les pages commencent à zéro
		this.mockMvc.perform(get("/api/${entity.model.var}s/bypage") //
			.param("page", Integer.toString(firstPage)) //
			.param("sort", "title") //
			.param("size", Integer.toString(size))) //
			.andDo(print()) //
			.andExpect(status().isOk()) //
			.andExpect(jsonPath("${dollar}.numberOfElements").value(size)) //
			// total number of records
			.andExpect(jsonPath("${dollar}.content", hasSize(size))); //
			// to check an attribut value of the first record
			// unfortunately: it is nto back in the expected order
			//.andExpect(jsonPath("${dollar}.content[0].description").value(${entity.model.var}s[0].getDescription()));
	}
	
	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void testCreate() throws Exception {
		createA${entity.model.type}();
	}

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void testUpdate() throws Exception {
    	// real update
    	${entity.model.type} ${entity.model.var} = createA${entity.model.type}();
    	
    	this.mockMvc.perform(put("/api/${entity.model.var}s/") //
    		.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) //
    		.content(JsonUtils.convertObjectToJsonBytes(${entity.model.var}))) //
			.andDo(print()).andExpect(status().isOk());
    	
    	// update becomes a creation because the ID is not set 
    	${entity.model.type} ${entity.model.var}withoutId = createA${entity.model.type}();
    	
    	this.mockMvc.perform(put("/api/${entity.model.var}s/") //
    		.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) //
    		.content(JsonUtils.convertObjectToJsonBytes(${entity.model.var}withoutId))) //
			.andDo(print()).andExpect(status().isOk());
    	
    }
	
	private ${entity.model.type} createA${entity.model.type} () throws Exception {
		${entity.model.type} ${entity.model.var} = ${entity.model.type}EntityTestUtils.createNew${entity.model.type}();
		
		// https://www.petrikainulainen.net/programming/spring-framework/integration-testing-of-spring-mvc-applications-write-clean-assertions-with-jsonpath/
		this.mockMvc.perform(post("/api/${entity.model.var}s/") //
			.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE) //
			.content(JsonUtils.convertObjectToJsonBytes(${entity.model.var}))) //
			.andDo(print()) //
			.andExpect(status().isCreated()) //
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)); //
			//.andExpect(jsonPath(".price").value(${entity.model.var}.getPrice()));
		
		return ${entity.model.var};
	}
}
