$output.java("${configuration.rootPackage}.searchrepository", "${entity.model.type}SearchRepository")##

#if ($entity.hasSimplePk())
$output.require($entity.model)##
$output.require("org.springframework.data.elasticsearch.repository.ElasticsearchRepository")##
#end

/**
 * Spring Data ElasticSearch repository for the entity named: $entity.getName().
 */
#if ($entity.hasSimplePk())
public interface ${output.currentClass} extends ElasticsearchRepository<$entity.model.type, $entity.root.primaryKey.type> {
#else
public interface ${output.currentClass} {
	// Entity with composite primary key are not supported by Spring Data ElasticSearch project (01 march 2016)
	// so this feature is desactivated
#end
}
