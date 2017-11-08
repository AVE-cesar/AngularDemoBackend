$output.java("${configuration.rootPackage}.service","${entity.model.type}Service")##

$output.require("javax.transaction.Transactional")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.stereotype.Service")##

$output.require("${configuration.rootPackage}.elasticsearch.repository.${entity.model.type}ElasticsearchRepository")##
$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require("${configuration.rootPackage}.jpa.repository.${entity.model.type}JpaRepository")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityUtils")##

@Service
public class ${entity.model.type}Service {

	private final Logger log = LoggerFactory.getLogger(${entity.model.type}Service.class);

	@Autowired
	private ${entity.model.type}JpaRepository ${entity.model.var}JpaRepository;

	@Autowired
	private ${entity.model.type}ElasticsearchRepository ${entity.model.var}ElasticsearchRepository;

	@Transactional
	public ${entity.model.type} create(${entity.model.type} ${entity.model.var}) {
		log.debug("Create ${entity.model.type} : {}", ${entity.model.var});

		// to avoid the following error: detached entity passed to persist
#foreach ($spkattribute in $entity.simplePkAttributes.list)
#if ($spkattribute.type == "String")
		${entity.model.var}.set${spkattribute.varUp}("-1");
#else
		${entity.model.var}.set${spkattribute.varUp}(-1);
#end
#end


		${entity.model.type} result = ${entity.model.var}JpaRepository.save(${entity.model.var});
#if (($entity.hasSimplePk()))
		${entity.model.var}ElasticsearchRepository.save(${entity.model.type}EntityUtils.convertToElasticsearch${entity.model.type}(${entity.model.var}));
#end
		return result;
	}
}
