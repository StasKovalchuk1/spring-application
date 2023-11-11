package com.example.semestralka.services;

import com.example.semestralka.data.EventRepository;
import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.data.PersonRepository;
import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private FavoriteRepository favoriteRepo;

    @Test
    public void saveCreatesNewFavorite(){
        Event event = Generator.generateUpcomingEvent();
        User user = Generator.generateUser();
        eventRepo.save(event);
        personRepository.save(user);

        favoriteService.save(event, user);

        List<Favorite> userFavorites = favoriteRepo.findAllByUserId(user.getId());
        Favorite favoriteResult = favoriteRepo.findByUserAndEvent(user, event);
        assertTrue(userFavorites.contains(favoriteResult));

    }
}
