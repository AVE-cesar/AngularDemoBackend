$output.java("${configuration.rootPackage}.elasticsearch.repository", "${entity.model.type}ElasticsearchRepository")##

$output.require("org.springframework.data.elasticsearch.repository.ElasticsearchRepository")##

$output.require("com.jaxio.demo.elasticsearch.model.${entity.model.type}")##

public interface ${entity.model.type}ElasticsearchRepository extends ElasticsearchRepository<${entity.model.type}, String> {

}
