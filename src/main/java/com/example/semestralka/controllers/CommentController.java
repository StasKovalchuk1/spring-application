package com.example.semestralka.controllers;

import com.example.semestralka.controllers.util.RestUtils;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.Comment;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.CommentService;
import com.example.semestralka.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/events")
@PreAuthorize("permitAll()")
public class CommentController {

    private final EventService eventService;
    private final CommentService commentService;

    @Autowired
    public CommentController(EventService eventService, CommentService commentService) {
        this.eventService = eventService;
        this.commentService = commentService;
    }

    @GetMapping(value = "/{eventId}/comments/all")
    public List<Comment> getAllCommentsByEventId(@PathVariable Integer eventId){
        Event event = eventService.find(eventId);
        if (event==null){
            throw NotFoundException.create("Event", eventId);
        }
        return commentService.getAllByEvent(event);
    }

    @GetMapping(value = "/{eventId}/comments/from_first")
    public List<Comment> getAllCommentsByEventIdFromFirst(@PathVariable Integer eventId){
        Event event = eventService.find(eventId);
        if (event==null){
            throw NotFoundException.create("Event", eventId);
        }
        return commentService.getAllByEventFromFirst(event);
    }

    @GetMapping(value = "/{eventId}/comments/from_last")
    public List<Comment> getAllCommentsByEventIdFromLast(@PathVariable Integer eventId){
        Event event = eventService.find(eventId);
        if (event==null){
            throw NotFoundException.create("Event", eventId);
        }
        return commentService.getAllByEventFromLast(event);
    }

    @PostMapping(value = "/{eventId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addComment(Authentication auth,
                                           @RequestBody Comment comment,
                                           @PathVariable Integer eventId){
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        Event event = eventService.find(eventId);
        if (event == null) {
            throw NotFoundException.create("Event", eventId);
        }
        commentService.save(comment, user, event);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", comment.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteCommentByEventIdAndCommentId(@PathVariable Integer eventId,
                                                   @PathVariable Integer commentId){
        Event event = eventService.find(eventId);
        if (event == null) {
            throw NotFoundException.create("Event", eventId);
        }
        Comment comment = commentService.find(commentId);
        if (comment == null || !comment.getEvent().equals(event)) {
            throw NotFoundException.create("Comment", commentId);
        }
        commentService.delete(comment);
    }

    @PutMapping(value = "/{eventId}/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void editComment(@PathVariable Integer eventId,
                            @PathVariable Integer commentId,
                            @RequestBody Comment updatedComment){
        Event event = eventService.find(eventId);
        Comment existingComment = commentService.find(commentId);
        if (event == null) {
            throw NotFoundException.create("Event", eventId);
        }
        if (existingComment == null || !existingComment.getEvent().equals(event)) {
            throw NotFoundException.create("Comment", commentId);
        }
        existingComment.setText(updatedComment.getText());
        commentService.update(existingComment);
    }
}
