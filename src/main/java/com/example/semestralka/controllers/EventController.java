package com.example.semestralka.controllers;

import com.example.semestralka.controllers.util.RestUtils;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Genre;
import com.example.semestralka.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/events")
@PreAuthorize("permitAll()")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Event getById(@PathVariable Integer id){
        Event result = eventService.find(id);
        if (result==null){
            throw NotFoundException.create("Event", id);
        }
        return result;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> acceptEvent(@RequestBody Event event){
        eventService.acceptEvent(event);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", event.getId());
        return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/not_accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAllNotAccepted(){
        return eventService.getAllNotAccepted();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAllUpcomingEvents(){
        return eventService.getAllUpcomingEvents();
    }

    @GetMapping(value = "/by_genres/{genres}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAllUpcomingByGenres(@PathVariable List<Genre> genres) {
        return eventService.getAllUpcomingByGenres(genres);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createEventByUser(@RequestBody Event event, @RequestBody Club club){
        eventService.createEventByUser(event, club);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", event.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeEvent(@RequestBody Event event){
        eventService.delete(event);
    }
}
