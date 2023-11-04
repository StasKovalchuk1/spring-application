package com.example.semestralka.data;

import com.example.semestralka.SemestralkaApplication;
import com.example.semestralka.enviroment.Generator;
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

import static com.example.semestralka.enviroment.Generator.*;
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
        final Club club = generateClub();

        final Event event = generateEvent();
        event.setClub(club);

        eventRepository.save(event);
        assertNotNull(event.getId());
    }

    @Test
    public void findUpcomingEventsByGenres() {
        final Event event1 = generateEvent();

        final Event event2 = generateEvent();

        final Genre genre1 = generateGenre();
        final Genre genre2 = generateGenre();
        genreRepository.save(genre1);
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
