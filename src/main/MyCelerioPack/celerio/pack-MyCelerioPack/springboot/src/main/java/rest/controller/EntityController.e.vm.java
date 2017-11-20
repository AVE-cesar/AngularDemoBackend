$output.java("${configuration.rootPackage}.rest.controller", "${entity.model.type}Controller")##

#if($entity.hasUniqueBigIntegerAttribute())
$output.require("java.math.BigInteger")##
#end
#if($entity.hasUniqueDateAttribute() || $entity.root.hasDatePk() || !$entity.getCpkDateAttributes().isEmpty())
$output.require("java.util.Date")##
#end
$output.require("${configuration.rootPackage}.jpa.model.${entity.model.type}")##
$output.require($entity.root.primaryKey)##
#foreach($enumAttribute in $entity.uniqueEnumAttributes.list)
$output.require($enumAttribute)##
#end

$output.require("${configuration.rootPackage}.jpa.repository.${entity.model.type}JpaRepository")##
#if (($entity.hasSimplePk()))
$output.require("${configuration.rootPackage}.elasticsearch.repository.${entity.model.type}ElasticsearchRepository")##
$output.require("${configuration.rootPackage}.utils.${entity.model.type}EntityUtils")##
#end
$output.require("java.util.List")##
$output.require("java.util.stream.Stream")##
$output.require("java.net.URISyntaxException")##
$output.require("java.net.URI")##
$output.require("java.util.Optional")##

$output.require("javax.transaction.Transactional")##

$output.require("org.slf4j.LoggerFactory")##
$output.require("org.slf4j.Logger")##

$output.require("javax.persistence.EntityManager")##
$output.require("javax.persistence.criteria.CriteriaBuilder")##
$output.require("javax.persistence.criteria.CriteriaQuery")##
$output.require("javax.persistence.criteria.Predicate")##
$output.require("javax.persistence.criteria.Root")##

$output.require("org.springframework.web.bind.annotation.*")##
$output.require("org.springframework.http.MediaType")##
$output.require("org.springframework.http.ResponseEntity")##
$output.require("org.springframework.web.bind.annotation.RequestBody")##
$output.require("org.springframework.data.domain.Pageable")##
$output.require("org.springframework.data.domain.Page")##
$output.require("org.springframework.http.HttpHeaders")##
$output.require("org.springframework.http.HttpStatus")##
$output.require("org.springframework.scheduling.annotation.Async")##
$output.require("org.springframework.web.bind.annotation.RequestMapping")##
$output.require("org.springframework.web.bind.annotation.RequestMethod")##
$output.require("org.springframework.web.bind.annotation.PathVariable")##
#if (($entity.hasSimplePk()))
$output.require("static org.elasticsearch.index.query.QueryBuilders.queryStringQuery")##
$output.require("java.util.stream.Collectors")##
$output.require("java.util.stream.StreamSupport")##
#end
$output.require("java.util.ArrayList")##
$output.require("org.springframework.beans.factory.annotation.Autowired")##
$output.require("org.springframework.data.domain.PageImpl")##
$output.require("org.springframework.data.domain.PageRequest")##
$output.require("${configuration.rootPackage}.service.${entity.model.type}Service")##


@RestController
@RequestMapping("/api/${entity.model.vars}")
public class $output.currentClass{

	private final Logger log=LoggerFactory.getLogger(${output.currentClass}.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	${entity.model.type}Service ${entity.model.var}Service;

	/**
	 * Create a new $entity.model.type.
	 */
	@RequestMapping(value = "/",
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<$entity.model.type> create(@RequestBody $entity.model.type $entity.model.var) throws URISyntaxException {

		${entity.model.type} result = ${entity.model.var}Service.create($entity.model.var);

		return ResponseEntity.created(new URI("/api/${entity.model.vars}/"+result.getId()))
			.body(result);
	}

	/**
	 * Update $entity.model.type.
	 */
	@RequestMapping(value = "/",
		method = RequestMethod.PUT,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<$entity.model.type> update(@RequestBody $entity.model.type $entity.model.var) throws URISyntaxException {

	${entity.model.type} result = ${entity.model.var}Service.update($entity.model.var);

	return ResponseEntity.ok().body(result);
	}

    /**
     * Find all $entity.model.type.
     * WARNING: if your table has got a lot of records, you will face OutOfMemory error.
     */
    @RequestMapping(value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<$entity.model.type>> findAll() {
        log.debug("Find all $entity.model.varsUp");
        List<${entity.model.type}> list = ${entity.model.var}Service.findAll();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }
    
    /**
     * Find all $entity.model.type by page.
     */
    @RequestMapping(value = "/bypage",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<$entity.model.type> findAllByPage(Pageable pageable) {
        log.debug("Find all by page $entity.model.varsUp, page: " + pageable.getPageNumber() + ", size: " + pageable.getPageSize());
        Page<${entity.model.type}> page = ${entity.model.var}Service.findAll(pageable);
        log.debug("There are " + page.getTotalElements() + " $entity.model.vars.");
        return page;
    }

#if (($entity.hasSimplePk()))
    /**
     * Find by id $entity.model.type (for simple key).
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<$entity.model.type> findById(@PathVariable $entity.primaryKey.type $entity.primaryKey.var) {
        log.debug("Find by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        
        $entity.model.type fullyLoaded${entity.model.type} = ${entity.model.var}Service.findOne($entity.primaryKey.var);
#foreach ($relation in $entity.manyToMany.list)
	#if ($velocityCount == 1)
        // force object loading from database because of lazy loading settings
	#end
        fullyLoaded${entity.model.type}.${relation.to.getters}().size();
#end        		
        
		return Optional.ofNullable(fullyLoaded${entity.model.type})
            .map(${entity.model.var} -> new ResponseEntity<>(
                ${entity.model.var},
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
#else
    /**
     * Find by id $entity.model.type (for composite key).
     */
    @RequestMapping(value = "/$entity.extended.getCpkAttributesListWithCommaAndCurlyBracket()",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<$entity.model.type> findById($entity.extended.getCpkAttributesListRestStyle()) {
    	$entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}($entity.extended.getCpkAttributesListConstructorStyle());
        log.debug("Find by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        
        $entity.model.type fullyLoaded${entity.model.type} = ${entity.model.var}Service.findOne($entity.primaryKey.var);
#foreach ($relation in $entity.manyToMany.list)
	#if ($velocityCount == 1)
        // force object loading from database because of lazy loading settings
	#end
        fullyLoaded${entity.model.type}.${relation.to.getters}().size();
#end        		
        
		return Optional.ofNullable(fullyLoaded${entity.model.type})
            .map(${entity.model.var} -> new ResponseEntity<>(
                ${entity.model.var},
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
#end

#if (($entity.hasSimplePk()))
    /**
     * Delete by id $entity.model.type (for simple key).
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable $entity.primaryKey.type $entity.primaryKey.var) {
        log.debug("Delete by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        ${entity.model.var}Service.delete($entity.primaryKey.var);
        return ResponseEntity.ok().build();
    }
#else
	/**
     * Delete by id $entity.model.type (for composite key).
     */
    @RequestMapping(value = "/$entity.extended.getCpkAttributesListWithCommaAndCurlyBracket()",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete($entity.extended.getCpkAttributesListRestStyle()) {
	    $entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}($entity.extended.getCpkAttributesListConstructorStyle());
        log.debug("Delete by id $entity.model.varsUp : {}.", $entity.primaryKey.var);
        ${entity.model.var}JpaRepository.delete($entity.primaryKey.var); 
#if (($entity.hasSimplePk()))         
        ${entity.model.var}ElasticsearchRepository.delete($entity.primaryKey.var);
#end        
        return ResponseEntity.ok().build();
    }
#end
	
#if (($entity.hasSimplePk()))
    /**
     * Mass deletion (for simple key).
     */
    @RequestMapping(value = "/mass/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable $entity.primaryKey.type[] id) {
        log.debug("Delete by id $entity.model.varsUp : {}.", (Object[])id);
        Stream.of(id).forEach(item -> {${entity.model.var}JpaRepository.delete(item); #if (($entity.hasSimplePk()))${entity.model.var}ElasticsearchRepository.delete(item);#end}); 
        
        return ResponseEntity.ok().build();
    }
#else
	/**
     * Mass deletion (for composite key).
     */
    @RequestMapping(value = "/mass/$entity.extended.getCpkAttributesListWithCommaAndCurlyBracket()", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable $entity.primaryKey.type[] id) {
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
#end
    
    /**
     * Index all $entity.model.type.
     */
    @RequestMapping(value = "/indexAll",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(value = "/esearch/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List<${configuration.rootPackage}.elasticsearch.model.${entity.model.type}> search${entity.model.type}s(@PathVariable String query) {
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
    @RequestMapping(value = "/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> count() {
        log.debug("Count $entity.model.vars");
        long count = ${entity.model.var}JpaRepository.count();
        
        return new ResponseEntity<>(count, new HttpHeaders(), HttpStatus.OK);
    }

#if (($entity.hasSimplePk()))
    /**
     * Check if a $entity.model.type exists via its id.
     */
    @RequestMapping(value = "/exists/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> exists(@PathVariable $entity.primaryKey.type $entity.primaryKey.var) {
    	log.debug("Check $entity.model.var existence via its id: {}.", id);
    	Boolean exists = ${entity.model.var}JpaRepository.exists(id);
        
        return new ResponseEntity<>(exists, new HttpHeaders(), HttpStatus.OK);
    }
#else
	/**
     * Check if a $entity.model.type exists via its composite id.
     */
    @RequestMapping(value = "/exists/$entity.extended.getCpkAttributesListWithCommaAndCurlyBracket()",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> exists($entity.extended.getCpkAttributesListRestStyle()) {
		$entity.primaryKey.type $entity.primaryKey.var = new ${entity.primaryKey.type}($entity.extended.getCpkAttributesListConstructorStyle());
    	log.debug("Check $entity.model.var existence via its id: {}.", $entity.primaryKey.var);
    	Boolean exists = ${entity.model.var}JpaRepository.exists($entity.primaryKey.var);
        
        return new ResponseEntity<>(exists, new HttpHeaders(), HttpStatus.OK);
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
    @RequestMapping(value = "/findBy${oneToOne.to.type}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
$!{MethodsHistoryMap.put("findBy${oneToOne.to.type}", "findBy${oneToOne.to.type}")}
    public ResponseEntity<List<$entity.model.type>> findBy${oneToOne.to.type}(@PathVariable $oneToOne.toEntity.primaryKey.type $oneToOne.toEntity.primaryKey.var) throws URISyntaxException {
        log.debug("Find $entity.model.varsUp by ${oneToOne.to.type} id : {}.", id);
        
        ${oneToOne.to.type} ${oneToOne.toEntity.model.var} = new ${oneToOne.to.type}();
        ${oneToOne.toEntity.model.var}.setId(id);
        List<$entity.model.type> $entity.model.vars = ${entity.model.var}JpaRepository.findBy${oneToOne.to.varUp}(${oneToOne.toEntity.model.var});
        
        return new ResponseEntity<List<$entity.model.type>>($entity.model.vars, new HttpHeaders(), HttpStatus.OK);
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
    @RequestMapping(value = "/findBy${manyToOne.to.type}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
$!{MethodsHistoryMap.put("findBy${manyToOne.to.type}", "findBy${manyToOne.to.type}")}
    public ResponseEntity<List<$entity.model.type>> findBy${manyToOne.to.type}(@PathVariable $manyToOne.toEntity.primaryKey.type $manyToOne.toEntity.primaryKey.var) throws URISyntaxException {
        log.debug("Find $entity.model.varsUp by ${manyToOne.to.type} id : {}.", id);
        
        ${manyToOne.to.type} ${manyToOne.toEntity.model.var} = new ${manyToOne.to.type}();
        ${manyToOne.toEntity.model.var}.setId(id);
        List<$entity.model.type> $entity.model.vars = ${entity.model.var}JpaRepository.findBy${manyToOne.to.varUp}(${manyToOne.toEntity.model.var});
        
        return new ResponseEntity<List<$entity.model.type>>($entity.model.vars, new HttpHeaders(), HttpStatus.OK);
	}
#end

	/**
	 * Search $entity.model.vars.
	 */
	@RequestMapping(value = "/search",
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<$entity.model.type> search(@RequestBody $entity.model.type $entity.model.var, Pageable pageable) {
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
    @RequestMapping(value = "/finder/{domain},{key}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppParameter> findById(@PathVariable String domain, @PathVariable String key) {
        log.debug("Find by domain and key AppParameters : %s %s", domain, key);
        
        AppParameter appParameter = appParameterJpaRepository.findByDomainAndKey(domain, key);
        
        return new ResponseEntity<>(appParameter, HttpStatus.OK);
    }
#end
}