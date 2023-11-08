package com.example.semestralka.controllers;

import com.example.semestralka.model.Club;
import com.example.semestralka.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rest/clubs")
public class ClubController {

    private ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Club> getAll(){
        return clubService.findAll();
    }

//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Club getById(@PathVariable Integer id){
//        Club result = clubService.find(id);
//        if (result==null){
//            throw NotFoundException.create("Club", id);
//        }
//        return result;
//    }
}
