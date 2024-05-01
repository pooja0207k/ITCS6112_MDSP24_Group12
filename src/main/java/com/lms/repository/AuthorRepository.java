package com.lms.repository;

import org.springframework.data.repository.CrudRepository;

import com.lms.model.Author;

public interface AuthorRepository extends CrudRepository<Author,Integer>{

}
