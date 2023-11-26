package com.example.semestralka.controllers;

import com.example.semestralka.controllers.util.RestUtils;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/myProfile")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.save(user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Authentication auth) {
        assert auth.getPrincipal() instanceof UserDetails;
        return ((UserDetails) auth.getPrincipal()).getUser();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Event>> getFavoritesByUserId(@RequestBody User user) {
        List<Event> favorites = userService.getAllFavoriteEvents(user);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @GetMapping("/favorites/upcoming")
    public ResponseEntity<List<Event>> getUpcomingFavoritesByUserId(@RequestBody User user) {
        List<Event> favorites = userService.getAllFavoriteUpcomingEvents(user);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        if (userService.exists(user.getId())) {
            userService.save(user);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            throw NotFoundException.create("User", user.getId());
        }
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        final User user = userService.find(userId);
        if (user == null) {
            throw NotFoundException.create("User", userId);
        }
        userService.delete(user);
    }
}
