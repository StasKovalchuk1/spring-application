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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/clubs")
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
    public List<Event> getEventsByClub(@PathVariable Integer clubId){
        Club club = clubService.find(clubId);
        if (club==null){
            throw NotFoundException.create("Club", clubId);
        }
        return eventService.getAllByClub(club);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createClub(@RequestBody Club club){
        clubService.save(club);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", club.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeClub(@RequestBody Club club) {
        clubService.delete(club);
    }

    //Возможно должно быть в EventController
//    @PostMapping(value = "/{clubId}/events",consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> addEventToClubList(@PathVariable Integer clubId, @RequestBody Event event){
//        Club club = clubService.find(clubId);
//        if (club==null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        eventService.acceptEvent(event,club);
//        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri(
//                "/{clubId}/products", club.getId());
//        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping(value = "/{clubId}/events/{eventId}")
//    public ResponseEntity<Void> removeEventFromClubList(@PathVariable Integer clubId, @PathVariable Integer eventId){
//        Club club = clubService.find(clubId);
//        Event event = eventService.find(eventId);
//        if (club==null || event==null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        clubService.removeEventFromClubList(club, event);
//        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri(
//                "/{id}/events", club.getId());
//        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
//    }
}
