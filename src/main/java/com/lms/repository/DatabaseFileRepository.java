package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.model.DatabaseFile;

public interface DatabaseFileRepository extends JpaRepository<DatabaseFile, Integer>{

}
