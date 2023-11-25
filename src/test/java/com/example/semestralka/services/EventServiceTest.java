package com.example.semestralka.services;

import com.example.semestralka.data.ClubRepository;
import com.example.semestralka.data.CommentRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Comment;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EventServiceTest {

    @Autowired
    private EventService eventService;
    @Autowired
    private ClubRepository clubRepo;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private UserRepository userRepo;

    private Event event;
    private Club club;
    private User user;

    @BeforeEach
    public void setUp(){
        event = Generator.generateUpcomingEvent();
        club = Generator.generateClub();
        user = Generator.generateUser();
        userRepo.save(user);
        clubRepo.save(club);
        eventService.createEventByUser(event, club);
    }

    @Test
    public void deleteRemovesAllCommentsInEvent(){
        List<Comment> comments = Arrays.asList(Generator.generateComment(), Generator.generateComment(), Generator.generateComment());
        for (Comment c : comments){
            c.setUser(user);
            c.setEvent(event);
        }
        commentRepo.saveAll(comments);
        event.setComments(comments);
        club.addEvent(event);

        eventService.delete(event);

        for (Comment comment : comments) {
            assertFalse(commentRepo.existsById(comment.getId()));
        }
    }

    @Test
    public void deleteRemovesEventFromClub(){
        club.addEvent(event);
        assertTrue(club.getEvents().contains(event));

        eventService.delete(event);
        assertFalse(club.getEvents().contains(event));
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
