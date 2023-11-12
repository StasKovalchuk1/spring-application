package com.example.semestralka.services;


import com.example.semestralka.data.EventRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Comment;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EventRepository eventRepo;

    private User user;
    private Comment comment;
    private Event event;

    @BeforeEach
    public void setUp(){
        user = Generator.generateUser();
        event = Generator.generateUpcomingEvent();
        comment = Generator.generateComment();
        userRepo.save(user);
        eventRepo.save(event);
        commentService.save(comment, user, event);
    }

    @Test
    public void saveSetsUserToComment(){
        User resUser = comment.getUser();
        assertEquals(resUser, user);
    }

    @Test
    public void saveSetsEventToComment(){
        Event resEvent = comment.getEvent();
        assertEquals(resEvent, event);
    }

    @Test
    public void saveAddsCommentToUser(){
        List<Comment> userComments = user.getComments();
        assertTrue(userComments.contains(comment));
    }

    @Test
    public void saveAddsCommentToEvent(){
        List<Comment> eventComments = event.getComments();
        assertTrue(eventComments.contains(comment));
    }
}
