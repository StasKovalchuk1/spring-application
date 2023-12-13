package com.example.semestralka.controllers;

import com.example.semestralka.controllers.util.RestUtils;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> getAll(){
        return userService.findAll();
    }

    @PreAuthorize("(!#user.isAdmin() && anonymous) || hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.save(user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Authentication auth) {
        assert auth.getPrincipal() instanceof UserDetails;
        return ((UserDetails) auth.getPrincipal()).getUser();
    }

    @PutMapping("/myProfile/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> updateUser(@RequestBody User updatedUser, Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        if (updatedUser.getId().equals(user.getId())) {
            if (userService.exists(updatedUser.getId())) {
                userService.save(updatedUser);
                final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
                return new ResponseEntity<>(headers, HttpStatus.OK);
            } else throw NotFoundException.create("User", updatedUser.getId());
        } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //Deleting user by admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        final User userToDelete = userService.find(userId);
        if (userToDelete != null) {
            userService.delete(userToDelete);
        } else throw NotFoundException.create("User", userId);
    }

    //User deletes his own account
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/myProfile/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(Authentication auth){
        User userToDelete = ((UserDetails) auth.getPrincipal()).getUser();
        if (userToDelete != null) {
            userService.delete(userToDelete);
        } else throw new NotFoundException("User does not exist");
    }
}
