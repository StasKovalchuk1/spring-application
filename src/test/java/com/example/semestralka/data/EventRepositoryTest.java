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

    @Autowired
    public ClubRepository clubRepository;

    @Test
    public void createEventTest() {
        final Club club = generateClub();

        final Event event = generateEvent();
        event.setClub(club);

        eventRepository.save(event);
        assertNotNull(event.getId());
    }

    @Test
    public void findUpcomingEventsByGenresTest() {
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
        assertEquals(eventsByGenres.size(), 1);
        assertEquals(eventsByGenres.get(0).getId(), event1.getId());

        //test with 2 upcoming events
        event2.addGenre(genre2);
        eventRepository.save(event2);

        eventsByGenres = eventRepository.getUpcomingEventsByGenres(genres);
        assertEquals(eventsByGenres.size(), 2);
        assertEquals(eventsByGenres.get(0).getId(), event1.getId());
        assertEquals(eventsByGenres.get(1).getId(), event2.getId());
    }

    @Test
    public void getAllByClubTest() {
        Event event1 = generateEvent();
        Event event2 = generateEvent();
        Event event3 = generateEvent();
        event3.setFinished(true);

        Club club = generateClub();

        event1.setClub(club);
        event2.setClub(club);
        event3.setClub(club);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        clubRepository.save(club);

        List<Event> eventsByClub = eventRepository.getAllByClub(club);
        assertEquals(eventsByClub.size(), 3);
    }

    @Test
    public void getAllUpcomingByClubTest() {
        Event event1 = generateEvent();
        Event event2 = generateEvent();
        Event event3 = generateEvent();
        event3.setFinished(true);

        Club club = generateClub();

        event1.setClub(club);
        event2.setClub(club);
        event3.setClub(club);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        clubRepository.save(club);

        List<Event> eventsByClub = eventRepository.getAllUpcomingByClub(club);
        assertEquals(eventsByClub.size(), 2);
    }
}
