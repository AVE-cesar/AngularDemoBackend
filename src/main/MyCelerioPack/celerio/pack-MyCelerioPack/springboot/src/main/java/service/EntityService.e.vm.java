$output.java("${configuration.rootPackage}.service","${entity.model.type}Service")##

$output.require("javax.transaction.Transactional")##
$output.require("java.util.List")##
$output.require("java.util.stream.Stream")##
$output.require("java.util.ArrayList")##

$output.require("org.slf4j.Logger")##
$output.require("org.slf4j.LoggerFactory")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.stereotype.Service")##
$output.require("org.springframework.scheduling.annotation.Async")##

#if (($entity.hasSimplePk()))
$output.require("static org.elasticsearch.index.query.QueryBuilders.queryStringQuery")##
$output.require("java.util.stream.Collectors")##
$output.require("java.util.stream.StreamSupport")##
#end

$output.require("org.springframework.data.domain.Pageable")##
$output.require("org.springframework.data.domain.Page")##
$output.require("org.springframework.data.domain.PageRequest")##
$output.require("org.springframework.data.domain.PageImpl")##

$output.require("javax.persistence.EntityManager")##
$output.require("javax.persistence.criteria.CriteriaBuilder")##
$output.require("javax.persistence.criteria.CriteriaQuery")##
$output.require("javax.persistence.criteria.Predicate")##
$output.require("javax.persistence.criteria.Root")##

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
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * Create a new $entity.model.type.
	 */
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
	
	/**
	 * Update $entity.model.type.
	 */
	@Transactional
	public $entity.model.type update($entity.model.type $entity.model.var) {
		log.debug("Update $entity.model.varUp : {}",$entity.model.var);
		if (${entity.model.var}.getId() == null) {
			return create(${entity.model.var});
		}
		$entity.model.type result = ${entity.model.var}JpaRepository.save($entity.model.var);
#if (($entity.hasSimplePk()))
		${entity.model.var}ElasticsearchRepository.save(${entity.model.type}EntityUtils.convertToElasticsearch${entity.model.type}(${entity.model.var}));
#end
	return result;
	}
	
	/**
	 * Find all $entity.model.type.
	 * WARNING: if your table has got a lot of records, you will face OutOfMemory error.
	 */
	@Transactional
	public List<$entity.model.type> findAll() {
		log.debug("Find all $entity.model.varsUp");
		List<${entity.model.type}> list = ${entity.model.var}JpaRepository.findAll();
		return list;
	}

    /**
     * Find all $entity.model.type by page.
     */
	@Transactional
    public Page<$entity.model.type> findAllByPage(Pageable pageable) {
        log.debug("Find all by page $entity.model.varsUp, page: " + pageable.getPageNumber() + ", size: " + pageable.getPageSize());
        Page<${entity.model.type}> page = ${entity.model.var}JpaRepository.findAll(pageable);
        log.debug("There are " + page.getTotalElements() + " $entity.model.vars.");
        return page;
    }

#if (($entity.hasSimplePk()))
    /**
     * Find by id $entity.model.type (for simple key).
     */
	@Transactional
    public $entity.model.type findById($entity.primaryKey.type $entity.primaryKey.var) {
        log.debug("Find by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        
        $entity.model.type fullyLoaded${entity.model.type} = ${entity.model.var}JpaRepository.findOne($entity.primaryKey.var);
#foreach ($relation in $entity.manyToMany.list)
	#if ($velocityCount == 1)
        // force object loading from database because of lazy loading settings
	#end
        fullyLoaded${entity.model.type}.${relation.to.getters}().size();
#end        		
        
		return fullyLoaded${entity.model.type};
    }
#else
    /**
     * Find by id $entity.model.type (for composite key).
     */
    @Transactional
    public $entity.model.type findById($entity.extended.getCpkAttributesListRestStyle()) {
    	$entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}($entity.extended.getCpkAttributesListConstructorStyle());
        log.debug("Find by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        
        $entity.model.type fullyLoaded${entity.model.type} = ${entity.model.var}JpaRepository.findOne($entity.primaryKey.var);
#foreach ($relation in $entity.manyToMany.list)
	#if ($velocityCount == 1)
        // force object loading from database because of lazy loading settings
	#end
        fullyLoaded${entity.model.type}.${relation.to.getters}().size();
#end        		
        
		return fullyLoaded${entity.model.type});
    }
#end

#if (($entity.hasSimplePk()))
    /**
     * Delete by id $entity.model.type (for simple key).
     */
	@Transactional
    public void delete($entity.primaryKey.type $entity.primaryKey.var) {
        log.debug("Delete by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        ${entity.model.var}JpaRepository.delete($entity.primaryKey.var);
        ${entity.model.var}ElasticsearchRepository.delete($entity.primaryKey.var);        
    }
#else
	/**
     * Delete by id $entity.model.type (for composite key).
     */
	@Transactional
    public void delete($entity.extended.getCpkAttributesListRestStyle()) {
	    $entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}($entity.extended.getCpkAttributesListConstructorStyle());
        log.debug("Delete by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        ${entity.model.var}JpaRepository.delete($entity.primaryKey.var); 
#if (($entity.hasSimplePk()))         
        ${entity.model.var}ElasticsearchRepository.delete($entity.primaryKey.var);
#end        
    }
#end
	
#if (($entity.hasSimplePk()))
    /**
     * Mass deletion (for simple key).
     */
	@Transactional
    public void delete($entity.primaryKey.type[] id) {
        log.debug("Delete by id $entity.model.varsUp : {}.", (Object[])id);
        Stream.of(id).forEach(item -> {${entity.model.var}JpaRepository.delete(item); #if (($entity.hasSimplePk()))${entity.model.var}ElasticsearchRepository.delete(item);#end}); 
    }
#else
	/**
     * Mass deletion (for composite key).
     */
	@Transactional
    public void delete($entity.primaryKey.type[] id) {
    }
#end
    
    /**
     * Index all $entity.model.type.
     */
	@Transactional
	@Async
    public void indexAll${entity.model.varsUp}() {
    	log.debug("REST request to index all $entity.model.varsUp, START");
#if (($entity.hasSimplePk()))
    	${entity.model.var}JpaRepository.findAll().forEach(p -> {log.debug("indexing {}", p);${entity.model.var}ElasticsearchRepository.index(${entity.model.type}EntityUtils.convertToElasticsearch${entity.model.type}(p));});
    	
    	PageRequest request = new PageRequest(0, 1000);
        try {
        	Page<$entity.model.type> page = findAllByPage(request);
        	page.forEach(p -> ${entity.model.var}ElasticsearchRepository.index(${entity.model.type}EntityUtils.convertToElasticsearch${entity.model.type}(p)));
                         
             while (page.hasNext()) {
                    request = new PageRequest(request.getPageNumber() + 1, 1000);
                    
                    log.debug("we are indexing page: " + (request.getPageNumber() + 1));
                    
                    page = findAllByPage(request);
                    page.forEach(p -> ${entity.model.var}ElasticsearchRepository.index(${entity.model.type}EntityUtils.convertToElasticsearch${entity.model.type}(p)));
              }
        } catch (Exception e) {
        	log.error("", e);
        }

        log.debug("REST request to index all $entity.model.varsUp, EXIT");
#end    	
    }
    
    /**
     * Search with ElasticSearch.
     */
	@Transactional
    public List<${configuration.rootPackage}.elasticsearch.model.${entity.model.type}> search${entity.model.type}s(String query) {
#if (($entity.hasSimplePk()))     	
        return StreamSupport
            .stream(${entity.model.var}ElasticsearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
#else
		return null;
#end
    }
    
    /**
     * Count $entity.model.type.
     * FIXME: this method should be asynchronous because it can take times to count all records !
     */
	@Transactional
    public Long count() {
        log.debug("Count $entity.model.vars");
        long count = ${entity.model.var}JpaRepository.count();
        
        return count;
    }

#if (($entity.hasSimplePk()))
    /**
     * Check if a $entity.model.type exists via its id.
     */
	@Transactional
    public Boolean exists($entity.primaryKey.type $entity.primaryKey.var) {
    	log.debug("Check $entity.model.var existence via its id: {}.", id);
    	Boolean exists = ${entity.model.var}JpaRepository.exists(id);
        
        return exists;
    }
#else
	/**
     * Check if a $entity.model.type exists via its composite id.
     */
	@Transactional
    public Boolean exists($entity.extended.getCpkAttributesListRestStyle()) {
		$entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}($entity.extended.getCpkAttributesListConstructorStyle());
    	log.debug("Check $entity.model.var existence via its id: {}.", $entity.primaryKey.var);
    	Boolean exists = ${entity.model.var}JpaRepository.exists($entity.primaryKey.var);
        
        return exists;
    }
#end	

## --------------- One to One
#set( $MethodsHistoryMap = {}) ## Map
#foreach ($oneToOne in $entity.oneToOne.list)
$output.require("${configuration.rootPackage}.jpa.model.$oneToOne.to.type")##
    /**
     * Finder to fill relation between this entity and ${oneToOne.to.varUp}.
     * 
     * @param id id of the linked entity.
     * @return list of $entity.model.type
     * @throws URISyntaxException
     */
	@Transactional
$!{MethodsHistoryMap.put("findBy${oneToOne.to.type}", "findBy${oneToOne.to.type}")}
    public List<$entity.model.type> findBy${oneToOne.to.type}($oneToOne.toEntity.primaryKey.type $oneToOne.toEntity.primaryKey.var) {
        log.debug("Find $entity.model.varsUp by ${oneToOne.to.type} id : {}.", id);
        
        ${oneToOne.to.type} ${oneToOne.toEntity.model.var} = new ${oneToOne.to.type}();
        ${oneToOne.toEntity.model.var}.setId(id);
        List<$entity.model.type> $entity.model.vars = ${entity.model.var}JpaRepository.findBy${oneToOne.to.varUp}(${oneToOne.toEntity.model.var});
        
        return $entity.model.vars;
	}
#end

## --------------- Many to One
#foreach ($manyToOne in $entity.manyToOne.list)
$output.require("${configuration.rootPackage}.jpa.model.$manyToOne.to.type")##
    /**
     * Finder to fill relation between this entity and ${manyToOne.to.varUp}.
     * 
     * @param id id of the linked entity.
     * @return list of $entity.model.type
     * @throws URISyntaxException
     * $MethodsHistoryMap.size()
     */
$!{MethodsHistoryMap.put("findBy${manyToOne.to.type}", "findBy${manyToOne.to.type}")}
	@Transactional
    public List<$entity.model.type> findBy${manyToOne.to.type} ($manyToOne.toEntity.primaryKey.type $manyToOne.toEntity.primaryKey.var) {
        log.debug("Find $entity.model.varsUp by ${manyToOne.to.type} id : {}.", id);
        
        ${manyToOne.to.type} ${manyToOne.toEntity.model.var} = new ${manyToOne.to.type}();
        ${manyToOne.toEntity.model.var}.setId(id);
        List<$entity.model.type> $entity.model.vars = ${entity.model.var}JpaRepository.findBy${manyToOne.to.varUp}(${manyToOne.toEntity.model.var});
        
        return $entity.model.vars;
	}
#end

	/**
	 * Search $entity.model.vars.
	 */
	@Transactional
	public Page<$entity.model.type> search($entity.model.type $entity.model.var, Pageable pageable) {
		log.debug("Search $entity.model.vars, page: " + pageable.getPageNumber() + ", size: " + pageable.getPageSize());
		log.debug("$entity.model.var: {}",  $entity.model.var);
 
		String SQL_WILDCARD = "%";
		CriteriaBuilder criteriaBuilderObj = entityManager.getCriteriaBuilder();
		CriteriaQuery<$entity.model.type> queryObj = criteriaBuilderObj.createQuery(${entity.model.type}.class);
		Root<${entity.model.type}> ${entity.model.var}Table = queryObj.from(${entity.model.type}.class);

		List<Predicate> conditionsList = new ArrayList<Predicate>();
		
		// add a condition for all filled attributes
#foreach ($attribute in $entity.attributes.list)
	#if (!$attribute.isInFk() && !$attribute.isInCpk())
		if (${entity.model.var}.get${attribute.varUp}() != null) {
			#if ($attribute.isString())
				
				Predicate ${attribute.var}Predicate = criteriaBuilderObj.like(${entity.model.var}Table.get("${attribute.var}"), SQL_WILDCARD + ${entity.model.var}.get${attribute.varUp}() + SQL_WILDCARD);
				conditionsList.add(${attribute.var}Predicate);
			#else
				Predicate ${attribute.var}Predicate = criteriaBuilderObj.equal(${entity.model.var}Table.get("${attribute.var}"), ${entity.model.var}.get${attribute.varUp}());
				conditionsList.add(${attribute.var}Predicate);
			#end			
		}
	#end
#end

		// add a condition for all ManyToOne filled attributes
#foreach ($manyToOne in $entity.manyToOne.list)
		if (${entity.model.var}.get${manyToOne.to.type}() != null) {
			Predicate ${manyToOne.to.var}Predicate = criteriaBuilderObj.equal(${entity.model.var}Table.get("${manyToOne.to.var}"), ${entity.model.var}.get${manyToOne.to.type}().getId());
			conditionsList.add(${manyToOne.to.var}Predicate);
		}
#end
		
		queryObj.where(conditionsList.toArray(new Predicate[] {}));
		List<${entity.model.type}> resultList = entityManager.createQuery(queryObj).getResultList();
		log.info("This query returns {} ${entity.model.var}(s).", resultList.size());

		return new PageImpl<>(resultList, pageable, resultList.size());
	}

## dedicated method for system entities
#if ($entity.model.type == "AppParameter")
    /**
     * Find by domain and key a AppParameter.
     */
	@Transactional
    public AppParameter findById(String domain, String key) {
        log.debug("Find by domain and key AppParameters : %s %s", domain, key);
        
        AppParameter appParameter = appParameterJpaRepository.findByDomainAndKey(domain, key);
        
        return appParameter;
    }
#end
}
