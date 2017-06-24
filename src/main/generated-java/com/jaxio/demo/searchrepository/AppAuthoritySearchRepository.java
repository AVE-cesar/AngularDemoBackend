/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/searchrepository/EntitySearchRepository.e.vm.java
 */
package com.jaxio.demo.searchrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.jaxio.demo.domain.AppAuthority;

/**
 * Spring Data ElasticSearch repository for the entity named: AppAuthority.
 */
public interface AppAuthoritySearchRepository extends ElasticsearchRepository<AppAuthority, Integer> {
}
