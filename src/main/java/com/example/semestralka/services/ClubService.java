package com.example.semestralka.services;

import com.example.semestralka.data.ClubRepository;
import com.example.semestralka.data.EventRepository;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
public class ClubService {

    private final ClubRepository clubRepo;
    private final EventRepository eventRepo;

    @Autowired
    public ClubService(ClubRepository clubRepo, EventRepository eventRepo) {
        this.clubRepo = clubRepo;
        this.eventRepo = eventRepo;
    }

    @Transactional(readOnly = true)
    public Club find(Integer id){
        Objects.requireNonNull(id);
        return clubRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Club> findAll(){
        try {
            return clubRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no clubs");
        }
    }
    @Transactional
    public void save(Club club){
        Objects.requireNonNull(club);
        clubRepo.save(club);
    }

    @Transactional
    public void update(Club club){
        Objects.requireNonNull(club);
        if (exists(club.getId())) {
            clubRepo.save(club);
        }
    }

    @Transactional
    public void delete(Club club){
        Objects.requireNonNull(club);
        if (exists(club.getId())) {
            clubRepo.delete(club);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  clubRepo.existsById(id);
    }

    @Transactional
    public void removeEvent(Club club, Event event){
        Objects.requireNonNull(club);
        Objects.requireNonNull(event);
        if (exists(club.getId())&&eventRepo.existsById(event.getId())){
            club.removeEvent(event);
            clubRepo.save(club);
        }
    }
}
