package com.example.semestralka.services;

import com.example.semestralka.data.UserRepository;
import com.example.semestralka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User find(Integer id){
        Objects.requireNonNull(id);
        return userRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll(){
        try {
            return userRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no users");
        }
    }
    @Transactional
    public void save(User user){
        Objects.requireNonNull(user);
        userRepo.save(user);
    }

    @Transactional
    public void update(User user){
        Objects.requireNonNull(user);
        if (exists(user.getId())) {
            userRepo.save(user);
        }
    }

    @Transactional
    public void delete(User user){
        Objects.requireNonNull(user);
        if (exists(user.getId())) {
            userRepo.delete(user);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  userRepo.existsById(id);
    }
}
