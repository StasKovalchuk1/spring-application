package com.example.semestralka.services;

import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.FavoriteId;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final UserRepository userRepo;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepo, UserRepository userRepo) {
        this.favoriteRepo = favoriteRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public Favorite find(FavoriteId id){
        Objects.requireNonNull(id);
        return favoriteRepo.findById(id).orElse(null);
    }

//    @Transactional(readOnly = true)
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PostFilter("filterObject.user.username == principal.username")
//    public Iterable<Favorite> findAll(){
//        try {
//            return favoriteRepo.findAll();
//        } catch (DataAccessException e) {
//            throw new NotFoundException("There are no favorite events");
//        }
//    }

    @Transactional(readOnly = true)
    public List<Event> getAllFavoriteEvents(User user){
        try {
            List<Favorite> favorites = favoriteRepo.findAllByUserId(user.getId());
            List<Event> favoriteEvents = new ArrayList<>();
            for (Favorite favorite : favorites) {
                favoriteEvents.add(favorite.getEvent());
            }
            return favoriteEvents;
        } catch (DataAccessException e) {
            throw new NotFoundException("There are no favorite events");
        }
    }

    @Transactional(readOnly = true)
    public List<Event> getAllFavoriteUpcomingEvents(User user){
        try {
            List<Favorite> favorites = favoriteRepo.findAllByUserId(user.getId());
            List<Event> favoriteEvents = new ArrayList<>();
            for (Favorite favorite : favorites) {
                if (favorite.getEvent().getEventDate().isAfter(LocalDateTime.now())) {
                    favoriteEvents.add(favorite.getEvent());
                }
            }
            return favoriteEvents;
        } catch (DataAccessException e) {
            throw new NotFoundException("There are no favorite events");
        }
    }

    @Transactional
    public void save(Event event, User user){
        Objects.requireNonNull(event);
        Objects.requireNonNull(user);
        if (userRepo.existsById(user.getId()) && event.isAccepted()) {
            final Favorite f = new Favorite();
            final FavoriteId favoriteId = new FavoriteId();
            favoriteId.setUserId(user.getId());
            favoriteId.setEventId(event.getId());
            f.setId(favoriteId);
            f.setUser(user);
            f.setEvent(event);
            favoriteRepo.save(f);
        }
    }

    @Transactional
    public void update(Favorite favorite){
        Objects.requireNonNull(favorite);
        if (exists(favorite.getId())) {
            favoriteRepo.save(favorite);
        }
    }

    @Transactional
    public void delete(Favorite favorite){
        Objects.requireNonNull(favorite);
        if (exists(favorite.getId())) {
            favoriteRepo.delete(favorite);
        }
    }

    @Transactional
    public void delete(Event event, User user){
        Objects.requireNonNull(event);
        Objects.requireNonNull(user);
        List<Favorite> favorites = favoriteRepo.findAllByUserId(user.getId());
        for (Favorite favorite : favorites) {
            if (favorite.getEvent().getId().equals(event.getId())) {
                favoriteRepo.delete(favorite);
                break;
            }
        }
    }

    public boolean exists(FavoriteId id){
        Objects.requireNonNull(id);
        return  favoriteRepo.existsById(id);
    }
}
