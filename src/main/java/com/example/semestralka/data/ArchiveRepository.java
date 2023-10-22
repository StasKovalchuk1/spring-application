package com.example.semestralka.data;

import com.example.semestralka.model.Archive;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends CrudRepository<Archive, Integer> {

}
