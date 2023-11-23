package com.example.semestralka.services;

import com.example.semestralka.data.ClubRepository;
import com.example.semestralka.data.EventRepository;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
        } catch (DataAccessException e) {
            throw new NotFoundException("There are no clubs");
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
            for (Event e : eventRepo.getAllUpcomingByClub(club)){
                e.setClub(null);
                e.setAccepted(false);
                eventRepo.save(e);
            }
            clubRepo.delete(club);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  clubRepo.existsById(id);
    }
}
