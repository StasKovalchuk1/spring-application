package com.example.semestralka.data;

import com.example.semestralka.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person getByUsername(String username);
}
