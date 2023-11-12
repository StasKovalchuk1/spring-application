package com.example.semestralka.services;

import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private ClubService clubService;

    private Event event;
    private Club club;

    @BeforeEach
    public void setUp(){
        event = Generator.generateUpcomingEvent();
        club = Generator.generateClub();
        clubService.save(club);
        eventService.save(event, club);
    }
    @Test
    public void acceptEventSetsAcceptedTrueToEvent(){
        eventService.acceptEvent(event);
        assertTrue(event.isAccepted());
    }

    @Test
    public void acceptEventAddsEventToClub(){
        eventService.acceptEvent(event);
        assertTrue(club.getEvents().contains(event));
    }


    @Test
    public void acceptEventSetsClubToEventAndAddsEventToClub(){
        eventService.acceptEvent(event);
        Event resEvent = eventService.find(event.getId());
        Club resClub = resEvent.getClub();
        assertEquals(resClub, club);
        assertTrue(resClub.getEvents().contains(resEvent));
    }
}
