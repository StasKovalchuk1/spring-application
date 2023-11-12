package com.example.semestralka.services;

import com.example.semestralka.data.CommentRepository;
import com.example.semestralka.data.EventRepository;
import com.example.semestralka.data.UserRepository;
import com.example.semestralka.exceptions.NotFoundException;
import com.example.semestralka.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final EventRepository eventRepo;
    private final UserRepository userRepo;

    @Autowired
    public CommentService(CommentRepository commentRepo, EventRepository eventRepo, UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void save(Comment comment, User user, Event event){
        Objects.requireNonNull(comment);
        Objects.requireNonNull(user);
        if (userRepo.existsById(user.getId()) && eventRepo.existsById(event.getId())) {
            comment.setUser(user);
            comment.setEvent(event);
            user.addComment(comment);
            event.addComment(comment);
            commentRepo.save(comment);
            userRepo.save(user);
            eventRepo.save(event);
        }
    }

    @Transactional(readOnly = true)
    public Comment find(Integer id){
        Objects.requireNonNull(id);
        return commentRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Comment> findAll(){
        try {
            return commentRepo.findAll();
        } catch (DataAccessException e) {
            throw new NotFoundException("There are no comments");
        }
    }

    @Transactional
    public void update(Comment comment){
        Objects.requireNonNull(comment);
        if (exists(comment.getId())) {
            commentRepo.save(comment);
        }
    }

    @Transactional
    public void delete(Comment comment){
        Objects.requireNonNull(comment);
        if (exists(comment.getId())) {
            commentRepo.delete(comment);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  commentRepo.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllByUser(User user){
        List<Comment> comments = user.getComments();
        if (comments.isEmpty()){
            throw new NotFoundException("There are no comments");
        }
        return comments;
    }


}