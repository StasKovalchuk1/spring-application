package com.example.semestralka.services;

import com.example.semestralka.data.PersonRepository;
import com.example.semestralka.model.Person;
import com.example.semestralka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PersonService {

    public final PersonRepository personRepo;

    @Autowired
    public PersonService(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @Transactional(readOnly = true)
    public Iterable<Person> findAll(){
        try {
            return personRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no users");
        }
    }
    @Transactional
    public void save(Person person){
        Objects.requireNonNull(person);
        personRepo.save(person);
    }

    @Transactional
    public void update(Person person){
        Objects.requireNonNull(person);
        if (exists(person.getId())) {
            personRepo.save(person);
        }
    }

    @Transactional
    public void delete(Person person){
        Objects.requireNonNull(person);
        if (exists(person.getId())) {
            personRepo.delete(person);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  personRepo.existsById(id);
    }
}
