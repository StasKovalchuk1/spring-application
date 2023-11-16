package com.example.semestralka.services;

import com.example.semestralka.data.EventRepository;
import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.example.semestralka.enviroment.Generator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ClubService clubService;

    private User user1;
    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    public void setUp(){
        user1 = generateUser();

        event1 = generateUpcomingEvent();
        event2 = generateUpcomingEvent();
        event3 = generateFinishedEvent();

        Club club = generateClub();

        userService.save(user1);

        clubService.save(club);

        eventService.save(event1, club);
        eventService.save(event2, club);
        eventService.save(event3, club);

        favoriteService.save(event1, user1);
        favoriteService.save(event2, user1);
        favoriteService.save(event3, user1);
    }

    @Test
    public void getAllFavoriteEvents() {
        List<Event> favoriteEvents = userService.getAllFavoriteEvents(user1);
        List<Event> expectedEvents = Arrays.asList(event3, event2, event1);

        assertNotEquals(expectedEvents, favoriteEvents);
    }

    @Test
    public void getAllFavoriteUpcomingEvents() {
        List<Event> favoriteEvents = userService.getAllFavoriteEvents(user1);
        List<Event> expectedEvents = Arrays.asList(event2, event1);

        assertNotEquals(expectedEvents, favoriteEvents);
    }
}
