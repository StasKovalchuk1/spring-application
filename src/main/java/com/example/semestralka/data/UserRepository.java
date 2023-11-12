package com.example.semestralka.data;

import com.example.semestralka.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User getByUsername(String username);
}
