package com.example.semestralka.services;

import com.example.semestralka.data.ClubRepository;
import com.example.semestralka.data.EventRepository;
import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    private final EventRepository eventRepo;
    private final FavoriteRepository favoriteRepo;
    private final ClubRepository clubRepo;

    @Autowired
    public EventService(EventRepository eventRepo, FavoriteRepository favoriteRepo, ClubRepository clubRepo) {
        this.eventRepo = eventRepo;
        this.favoriteRepo = favoriteRepo;
        this.clubRepo = clubRepo;
    }

    public void acceptEvent(Event event, Club club){
        Objects.requireNonNull(event);
        Objects.requireNonNull(club);

        event.setAccepted(true);
        club.addEvent(event);
        eventRepo.save(event);
        clubRepo.save(club);
    }

    public List<Event> getAllFavorite(User user){
        List<Favorite> favorites = favoriteRepo.findAllByUserId(user.getId());
        List<Event> favoriteEvents = new ArrayList<>();
        for (Favorite favorite : favorites) {
            favoriteEvents.add(favorite.getEvent());
        }
        return favoriteEvents;
    }

    public List<Event> getAllNotAccepted(){
        return eventRepo.getAllByAcceptedIsFalse();
    }

    public List<Event> getAllByClub(Club club){
        Objects.requireNonNull(club);
        return eventRepo.getAllByClub(club);
    }

    public List<Event> getAllNotFinished(){
        return eventRepo.getAllByFinishedIsFalse();
    }

    public List<Event> getUpcomingByGenre(List<Genre> genres) {
        Objects.requireNonNull(genres);
        return eventRepo.getUpcomingEventsByGenres(genres);
    }

    public Event find(Integer id){
        Objects.requireNonNull(id);
        return eventRepo.findById(id).orElse(null);
    }

    public Iterable<Event> findAll(){
        try {
            return eventRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no events");
        }
    }
    @Transactional
    public void save(Event event){
        Objects.requireNonNull(event);
        eventRepo.save(event);
    }

    @Transactional
    public void update(Event event){
        Objects.requireNonNull(event);
        if (exists(event.getId())) {
            eventRepo.save(event);
        }
    }

    @Transactional
    public void delete(Event event){
        Objects.requireNonNull(event);
        if (exists(event.getId())) {
            eventRepo.delete(event);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  eventRepo.existsById(id);
    }
}
