/*
 * Source code generated by Celerio, a Jaxio product.
 * Documentation: http://www.jaxio.com/documentation/celerio/
 * Follow us on twitter: @jaxiosoft
 * Need commercial support ? Contact us: info@jaxio.com
 * Template pack-MyCelerioPack:springboot/src/main/java/elasticsearch/repository/EntityElasticsearchRepository.e.vm.java
 */
package com.jaxio.demo.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.jaxio.demo.elasticsearch.model.AppUser;

public interface AppUserElasticsearchRepository extends ElasticsearchRepository<AppUser, String> {

}
