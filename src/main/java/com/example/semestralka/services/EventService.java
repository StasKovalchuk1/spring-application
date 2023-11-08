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

    @Transactional
    public void acceptEvent(Event event, Club club){
        Objects.requireNonNull(event);
        Objects.requireNonNull(club);
        if (clubRepo.existsById(club.getId())&&eventRepo.existsById(event.getId())){
            event.setAccepted(true);
            club.addEvent(event);
            event.setClub(club);
            eventRepo.save(event);
            clubRepo.save(club);
        }
    }

    @Transactional(readOnly = true)
    public List<Event> getAllFavoriteEvents(User user){
        List<Favorite> favorites = favoriteRepo.findAllByUserId(user.getId());
        List<Event> favoriteEvents = new ArrayList<>();
        for (Favorite favorite : favorites) {
            favoriteEvents.add(favorite.getEvent());
        }
        return favoriteEvents;
    }

    @Transactional(readOnly = true)
    public List<Event> getAllNotAccepted(){
        return eventRepo.getAllByAcceptedIsFalse();
    }

    @Transactional(readOnly = true)
    public List<Event> getAllByClub(Club club){
        Objects.requireNonNull(club);
        try {
            return eventRepo.getAllByClub(club);
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no events in this club");
        }
    }

    @Transactional(readOnly = true)
    public List<Event> getAllNotFinished(){
        try {
            return eventRepo.getAllByFinishedIsFalse();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no not finished events");
        }
    }

    @Transactional(readOnly = true)
    public List<Event> getUpcomingByGenre(List<Genre> genres) {
        Objects.requireNonNull(genres);
        try {
            return eventRepo.getUpcomingEventsByGenres(genres);
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no upcoming events by this genre");
        }
    }

    @Transactional(readOnly = true)
    public Event find(Integer id){
        Objects.requireNonNull(id);
        return eventRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Event> findAll(){
        try {
            return eventRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no events");
        }
    }
    @Transactional
    public void save(Event event, Club club){
        Objects.requireNonNull(event);
        Objects.requireNonNull(club);
        if (clubRepo.existsById(club.getId())) {
            event.setClub(club);
            club.addEvent(event);
            clubRepo.save(club);
            eventRepo.save(event);
        }
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
