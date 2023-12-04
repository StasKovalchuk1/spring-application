package com.example.semestralka.services;

import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Comment;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    public final UserRepository userRepository;

    public final FavoriteRepository favoriteRepository;

    @Autowired
    public UserService(UserRepository userRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional(readOnly = true)
    public User find(Integer id){
        Objects.requireNonNull(id);
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<User> findAll(){
        try {
            return userRepository.findAll();
        } catch (DataAccessException e) {
            throw new NotFoundException("There are no users");
        }
    }

//    @Transactional(readOnly = true)
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PostFilter("filterObject.user.username == principal.username")
//    public List<Event> getAllFavoriteEvents(User user){
//        try {
//            List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());
//            List<Event> favoriteEvents = new ArrayList<>();
//            for (Favorite favorite : favorites) {
//                favoriteEvents.add(favorite.getEvent());
//            }
//            return favoriteEvents;
//        } catch (DataAccessException e) {
//            throw new NotFoundException("There are no favorite events");
//        }
//    }
//
//    @Transactional(readOnly = true)
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PostFilter("filterObject.user.username == principal.username")
//    public List<Event> getAllFavoriteUpcomingEvents(User user){
//        try {
//            List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());
//            List<Event> favoriteEvents = new ArrayList<>();
//            for (Favorite favorite : favorites) {
//                if (favorite.getEvent().getEventDate().isAfter(LocalDateTime.now())) {
//                    favoriteEvents.add(favorite.getEvent());
//                }
//            }
//            return favoriteEvents;
//        } catch (DataAccessException e) {
//            throw new NotFoundException("There are no favorite events");
//        }
//    }

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
