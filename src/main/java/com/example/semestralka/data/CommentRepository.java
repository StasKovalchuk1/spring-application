package com.example.semestralka.data;

import com.example.semestralka.model.Comment;
import com.example.semestralka.model.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Query("select distinct c from Comment c where c.event = :event order by c.created")
    List<Comment> getAllByEventFromFirst(Event event);

    @Query("select distinct c from Comment c where c.event = :event order by c.created desc")
    List<Comment> getAllByEventFromLast(Event event);

}
