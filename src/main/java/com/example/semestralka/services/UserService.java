package com.example.semestralka.services;

import com.example.semestralka.data.UserRepository;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Comment;
import com.example.semestralka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    public final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll(){
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new NotFoundException("There are no users");
        }
    }
    @Transactional
    public void save(User user){
        Objects.requireNonNull(user);
        userRepository.save(user);
    }

    @Transactional
    public void update(User user){
        Objects.requireNonNull(user);
        if (exists(user.getId())) {
            userRepository.save(user);
        }
    }

    @Transactional
    public void delete(User user){
        Objects.requireNonNull(user);
        if (exists(user.getId())) {
            userRepository.delete(user);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return userRepository.existsById(id);
    }
}
