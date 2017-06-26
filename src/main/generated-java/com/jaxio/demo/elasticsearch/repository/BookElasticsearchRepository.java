package com.jaxio.demo.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.jaxio.demo.elasticsearch.model.Book;

public interface BookElasticsearchRepository extends ElasticsearchRepository<Book, String> {

}
