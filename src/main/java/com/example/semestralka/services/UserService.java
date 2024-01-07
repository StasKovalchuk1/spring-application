package com.example.semestralka.services;

import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.exceptions.ValidationException;
import com.example.semestralka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserService {

    public final UserRepository userRepo;

    public final FavoriteRepository favoriteRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, FavoriteRepository favoriteRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.favoriteRepository = favoriteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User find(Integer id){
        Objects.requireNonNull(id);
        return userRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll(){
        try {
            return userRepo.findAll();
        } catch (DataAccessException e) {
            throw new NotFoundException("There are no users");
        }
    }

    @Transactional
    public void save(User user){
        Objects.requireNonNull(user);
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new ValidationException("This user already exists");
        }
        user.encodePassword(passwordEncoder);
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
        return userRepo.existsById(id);
    }
}
