/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/rest/controller/EntityController.e.vm.java
 */
package com.jaxio.demo.rest.controller;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jaxio.demo.elasticsearch.repository.AppTranslationElasticsearchRepository;
import com.jaxio.demo.jpa.model.AppTranslation;
import com.jaxio.demo.jpa.repository.AppTranslationJpaRepository;
import com.jaxio.demo.utils.AppTranslationEntityUtils;

@RestController
@RequestMapping("/api/appTranslations")
public class AppTranslationController {

    private final Logger log = LoggerFactory.getLogger(AppTranslationController.class);

    @Inject
    private AppTranslationJpaRepository appTranslationJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Inject
    private AppTranslationElasticsearchRepository appTranslationElasticsearchRepository;

    /**
     * Create a new AppTranslation.
     */
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppTranslation> create(@RequestBody AppTranslation appTranslation) throws URISyntaxException {
        log.debug("Create AppTranslation : {}", appTranslation);
        AppTranslation result = appTranslationJpaRepository.save(appTranslation);
        appTranslationElasticsearchRepository.save(AppTranslationEntityUtils.convertToElasticsearchAppTranslation(appTranslation));
        return ResponseEntity.created(new URI("/api/appTranslations/" + result.getId())).body(result);
    }

    /**
     * Update AppTranslation.
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppTranslation> update(@RequestBody AppTranslation appTranslation) throws URISyntaxException {
        log.debug("Update AppTranslation : {}", appTranslation);
        if (appTranslation.getId() == null) {
            return create(appTranslation);
        }
        AppTranslation result = appTranslationJpaRepository.save(appTranslation);
        appTranslationElasticsearchRepository.save(AppTranslationEntityUtils.convertToElasticsearchAppTranslation(appTranslation));
        return ResponseEntity.ok().body(result);
    }

    /**
     * Find all AppTranslation.
     * WARNING: if your table has got a lot of records, you will face OutOfMemory error.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppTranslation>> findAll() throws URISyntaxException {
        log.debug("Find all AppTranslations");
        List<AppTranslation> list = appTranslationJpaRepository.findAll();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Find all AppTranslation by page.
     */
    @RequestMapping(value = "/bypage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AppTranslation> findAllByPage(Pageable pageable) throws URISyntaxException {
        log.debug("Find all by page AppTranslations, page: " + pageable.getPageNumber() + ", size: " + pageable.getPageSize());
        Page<AppTranslation> page = appTranslationJpaRepository.findAll(pageable);
        log.debug("There are " + page.getTotalElements() + " appTranslations.");
        return page;
    }

    /**
    * Find by id AppTranslation (for simple key).
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<AppTranslation> findById(@PathVariable Integer id) throws URISyntaxException {
        log.debug("Find by id AppTranslations : {}.", id);

        AppTranslation fullyLoadedAppTranslation = appTranslationJpaRepository.findOne(id);

        return Optional.ofNullable(fullyLoadedAppTranslation).map(appTranslation -> new ResponseEntity<>(appTranslation, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete by id AppTranslation (for simple key).
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {
        log.debug("Delete by id AppTranslations : {}.", id);
        appTranslationJpaRepository.delete(id);
        appTranslationElasticsearchRepository.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Mass deletion (for simple key).
     */
    @RequestMapping(value = "/mass/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Integer[] id) throws URISyntaxException {
        log.debug("Delete by id AppTranslations : {}.", (Object[]) id);
        Stream.of(id).forEach(item -> {
            appTranslationJpaRepository.delete(item);
            appTranslationElasticsearchRepository.delete(item);
        });

        return ResponseEntity.ok().build();
    }

    /**
     * Index all AppTranslation.
     */
    @RequestMapping(value = "/indexAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Async
    public void indexAllAppTranslations() {
        log.debug("REST request to index all AppTranslations, START");
        appTranslationJpaRepository.findAll().forEach(p -> {
            log.debug("indexing");
            appTranslationElasticsearchRepository.index(AppTranslationEntityUtils.convertToElasticsearchAppTranslation(p));
        });

        PageRequest request = new PageRequest(0, 1000);
        try {
            Page<AppTranslation> page = findAllByPage(request);
            page.forEach(p -> appTranslationElasticsearchRepository.index(AppTranslationEntityUtils.convertToElasticsearchAppTranslation(p)));

            while (page.hasNext()) {
                request = new PageRequest(request.getPageNumber() + 1, 1000);

                log.debug("we are indexing page: " + (request.getPageNumber() + 1));

                page = findAllByPage(request);
                page.forEach(p -> appTranslationElasticsearchRepository.index(AppTranslationEntityUtils.convertToElasticsearchAppTranslation(p)));
            }
        } catch (Exception e) {
            log.error("", e);
        }

        log.debug("REST request to index all AppTranslations, EXIT");
    }

    /**
     * Search with ElasticSearch.
     */
    @RequestMapping(value = "/esearch/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<com.jaxio.demo.elasticsearch.model.AppTranslation> searchAppTranslations(@PathVariable String query) {
        return StreamSupport.stream(appTranslationElasticsearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
    }

    /**
     * Count AppTranslation.
     * FIXME: this method should be asynchronous because it can take times to count all records !
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> count() throws URISyntaxException {
        log.debug("Count appTranslations");
        long count = appTranslationJpaRepository.count();

        return new ResponseEntity<>(count, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Check if a AppTranslation exists via its id.
     */
    @RequestMapping(value = "/exists/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> exists(@PathVariable Integer id) throws URISyntaxException {
        log.debug("Check appTranslation existence via its id: {}.", id);
        Boolean exists = appTranslationJpaRepository.exists(id);

        return new ResponseEntity<>(exists, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Search appTranslations.
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AppTranslation> search(@RequestBody AppTranslation appTranslation, Pageable pageable) throws URISyntaxException {
        log.debug("Search appTranslations, page: " + pageable.getPageNumber() + ", size: " + pageable.getPageSize());
        log.debug("appTranslation: " + appTranslation);

        long total = appTranslationJpaRepository.count();

        String sqlMainPart = "select * from (select ID, LANGUAGE, KEY, VALUE from APP_TRANSLATION where 1=1";
        String sqlSecondaryPart = "";

        List<Object> values = new ArrayList<Object>();

        if (appTranslation.getId() != null) {
            sqlSecondaryPart += " and id = ? ";
            values.add(appTranslation.getId());
        }
        if (appTranslation.getLanguage() != null) {
            sqlSecondaryPart += " and upper(language) like ? ";
            values.add(appTranslation.getLanguage().toUpperCase() + "%");
        }
        if (appTranslation.getKey() != null) {
            sqlSecondaryPart += " and upper(key) like ? ";
            values.add(appTranslation.getKey().toUpperCase() + "%");
        }
        if (appTranslation.getValue() != null) {
            sqlSecondaryPart += " and upper(value) like ? ";
            values.add(appTranslation.getValue().toUpperCase() + "%");
        }

        sqlSecondaryPart += ") where rownum <= ?";
        values.add(pageable.getPageSize());

        log.debug("SQL: " + sqlMainPart + " " + sqlSecondaryPart);
        List<AppTranslation> appTranslations = jdbcTemplate.query(sqlMainPart + " " + sqlSecondaryPart, values.toArray(),
                new BeanPropertyRowMapper<AppTranslation>(AppTranslation.class));

        Page<AppTranslation> page = new PageImpl<AppTranslation>(appTranslations, pageable, total);

        return page;
    }

}