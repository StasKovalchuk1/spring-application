package com.example.semestralka.data;

import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository  extends CrudRepository<Event, Integer> {

    List<Event> getAllByAcceptedIsFalse();

    @Query("select distinct e from Event e where e.eventDate > CURRENT_TIMESTAMP")
    List<Event> getUpcomingEvents();

    @Query("select distinct e from Event e join e.genres g where e.eventDate > CURRENT_TIMESTAMP and g in :genres")
    List<Event> getUpcomingEventsByGenres(@Param("genres")List<Genre> genres);

    @Query("select distinct e from Event e where e.club = :club")
    List<Event> getAllByClub(Club club);

    @Query("select distinct e from Event e where e.eventDate > CURRENT_TIMESTAMP and e.club = :club")
    List<Event> getAllUpcomingByClub(Club club);

}

