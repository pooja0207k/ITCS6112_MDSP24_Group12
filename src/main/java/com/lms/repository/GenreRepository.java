package com.lms.repository;

import org.springframework.data.repository.CrudRepository;

import com.lms.model.Genre;

public interface GenreRepository extends CrudRepository<Genre,Integer>
{

}
