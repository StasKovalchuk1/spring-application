package com.example.semestralka.data;

import com.example.semestralka.SemestralkaApplication;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackageClasses = SemestralkaApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
@ActiveProfiles("test")
public class EventRepositoryTest {

    @Autowired
    public EventRepository eventRepository;

    @Autowired
    public GenreRepository genreRepository;

    @Test
    public void createEvent() {
        final Club club = new Club();
        club.setName("xyz");

        final Event event = new Event();
        event.setName("best party");
        event.setClub(club);
        event.setDescription("blablabla");
        event.setPrice(500);

        eventRepository.save(event);
        assertNotNull(event.getId());
    }

    @Test
    public void findUpcomingEventsByGenres() {
        final Event event1 = new Event();
        event1.setName("party1");
        event1.setDescription("blablabla");
        event1.setPrice(500);

        final Event event2 = new Event();
        event2.setName("party2");
        event2.setDescription("blablabla");
        event2.setPrice(500);

        final Genre genre1 = new Genre();
        genre1.setName("1");
        genreRepository.save(genre1);
        final Genre genre2 = new Genre();
        genre2.setName("2");
        genreRepository.save(genre2);

        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);

        event1.addGenre(genre1);

        eventRepository.save(event1);
        eventRepository.save(event2);

        //test with 1 upcoming event
        List<Event> eventsByGenres = eventRepository.getUpcomingEventsByGenres(genres);
        assertTrue(eventsByGenres.size() == 1);

        //test with 2 upcoming events
        event2.addGenre(genre2);
        eventRepository.save(event2);

        eventsByGenres = eventRepository.getUpcomingEventsByGenres(genres);
        assertTrue(eventsByGenres.size() == 2);
    }
}
