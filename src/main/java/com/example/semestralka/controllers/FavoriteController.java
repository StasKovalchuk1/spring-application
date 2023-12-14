package com.example.semestralka.controllers;

import com.example.semestralka.controllers.util.RestUtils;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.EventService;
import com.example.semestralka.services.FavoriteService;
import com.example.semestralka.services.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final EventService eventService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, EventService eventService) {
        this.favoriteService = favoriteService;
        this.eventService = eventService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Event> getFavorites(Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        return favoriteService.getAllFavoriteEvents(user);
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Event>  getUpcomingFavorites(Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        return favoriteService.getAllFavoriteUpcomingEvents(user);
    }

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> addToFavorite(@PathVariable Integer eventId, Authentication auth) {
        Event event = eventService.find(eventId);
        favoriteService.save(event, ((UserDetails) auth.getPrincipal()).getUser());
        // перенаправляет на список всех избранных
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/upcoming");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEventFromFavorite(@PathVariable Integer eventId, Authentication auth) {
        Event eventToRemove = eventService.find(eventId);
        if (eventToRemove != null) {
            favoriteService.delete(eventToRemove, ((UserDetails) auth.getPrincipal()).getUser());
        } else throw NotFoundException.create("Event", eventId);
    }

}
