package com.example.semestralka.controllers;

import com.example.semestralka.controllers.util.RestUtils;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.services.ClubService;
import com.example.semestralka.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    getAllUpcomingEventsByClub хочется убрать до event Controller,
    добвить update()
 */
@RestController
@RequestMapping("/rest/clubs")
@PreAuthorize("permitAll()")
public class ClubController {

    private final ClubService clubService;

    private final EventService eventService;

    @Autowired
    public ClubController(ClubService clubService, EventService eventService) {
        this.clubService = clubService;
        this.eventService = eventService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Club> getAll(){
        return clubService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Club getById(@PathVariable Integer id){
        Club result = clubService.find(id);
        if (result==null){
            throw NotFoundException.create("Club", id);
        }
        return result;
    }

    @GetMapping(value = "/{clubId}/events")
    public List<Event> getAllUpcomingEventsByClub(@PathVariable Integer clubId){
        Club club = clubService.find(clubId);
        if (club==null){
            throw NotFoundException.create("Club", clubId);
        }
        return eventService.getAllUpcomingByClub(club);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createClub(@RequestBody Club club){
        clubService.save(club);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", club.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClub(@PathVariable Integer id) {
        final Club clubToRemove = clubService.find(id);
        if (clubToRemove!=null) {
            clubService.delete(clubToRemove);
        } else throw NotFoundException.create("Club", id);
    }

}
