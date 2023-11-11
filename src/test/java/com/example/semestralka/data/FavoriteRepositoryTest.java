package com.example.semestralka.data;

import com.example.semestralka.SemestralkaApplication;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.FavoriteId;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.semestralka.enviroment.Generator.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackageClasses = SemestralkaApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
@ActiveProfiles("test")
public class FavoriteRepositoryTest {

    @Autowired
    public FavoriteRepository favoriteRepository;

    @Autowired
    public PersonRepository personRepository;

    @Autowired
    public EventRepository eventRepository;

    @Test
    public void findAllByUserIdTest() {
        User user = generateUser();
        Event event1 = generateUpcomingEvent();
        Event event2 = generateUpcomingEvent();
        Event event3 = generateUpcomingEvent();

        personRepository.save(user);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        Favorite favorite1 = generateFavorite(event1, user);
        Favorite favorite2 = generateFavorite(event2, user);


        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);

        List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());
        assertEquals(favorites.size(), 2);

    }
}
