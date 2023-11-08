package com.example.semestralka.services;

import com.example.semestralka.data.ClubRepository;
import com.example.semestralka.data.EventRepository;
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
    private EventRepository eventRepo;
    @Autowired
    private ClubRepository clubRepo;

    private Event event;
    private Club club;

    @BeforeEach
    public void setUp(){
        event = Generator.generateEvent();
        club = Generator.generateClub();
        eventRepo.save(event);
        clubRepo.save(club);
    }
    @Test
    public void acceptEventSetsAcceptedTrueToEvent(){
        eventService.acceptEvent(event,club);
        assertTrue(event.isAccepted());
    }

    @Test
    public void acceptEventAddsEventToClub(){
        eventService.acceptEvent(event,club);
        assertTrue(club.getEvents().contains(event));
    }

    @Test
    public void acceptEventSetsClubToEvent(){
        eventService.acceptEvent(event,club);
        assertEquals(event.getClub(), club);
    }

    @Test
    public void saveSetsClubToEventAndAddsEventToClub(){
        eventService.save(event,club);
        Event resEvent = eventRepo.findById(event.getId()).orElse(null);
        Club resClub = resEvent.getClub();
        assertEquals(resClub, club);
        assertTrue(resClub.getEvents().contains(resEvent));
    }
}
