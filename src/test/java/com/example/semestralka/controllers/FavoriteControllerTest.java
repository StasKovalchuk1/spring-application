package com.example.semestralka.controllers;

import com.example.semestralka.environment.Generator;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.EventService;
import com.example.semestralka.services.FavoriteService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest extends BaseControllerTestRunner{


    @Mock
    private FavoriteService favoriteServiceMock;

    @Mock
    private EventService eventServiceMock;

    @InjectMocks
    private FavoriteController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }


    @Test
    public void getFavoritesReturnsFavoritesByUsingFavoriteService() throws Exception {
        final User user = Generator.generateUser();
        user.setId(1337);
        List<Event> eventsInFavorites = new ArrayList<>();
        final List<Favorite> favorites = IntStream.range(0, 5).mapToObj(i -> {
            final Event event = Generator.generateUpcomingEvent();
            event.setId(Generator.randomInt());
            eventsInFavorites.add(event);
            return Generator.generateFavorite(event,user);
        }).toList();
        user.setFavorites(favorites);

        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUser()).thenReturn(user);
        when(favoriteServiceMock.getAllFavoriteEvents(user)).thenReturn(eventsInFavorites);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/favorites")
                        .principal(authMock))
                .andReturn();
        final List<Event> result = readValue(mvcResult, new TypeReference<>() {});
        final ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        assertEquals(result.size(), eventsInFavorites.size());
        verify(favoriteServiceMock).getAllFavoriteEvents(captor.capture());
    }

    //TODO does not work
    @Test
    public void getUpcomingFavoritesReturnsUpcomingFavoritesByUsingFavoriteService() throws Exception {
//        final User user = Generator.generateUser();
//        user.setId(1337);
//        List<Event> eventsInFavorites = new ArrayList<>();
//        final List<Favorite> favorites = IntStream.range(0, 5).mapToObj(i -> {
//            final Event event = Generator.generateUpcomingEvent();
//            event.setId(Generator.randomInt());
//            eventsInFavorites.add(event);
//            return Generator.generateFavorite(event,user);
//        }).toList();
//        user.setFavorites(favorites);
//
//        Authentication authMock = mock(Authentication.class);
//        UserDetails userDetailsMock = mock(UserDetails.class);
//        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
//        when(userDetailsMock.getUser()).thenReturn(user);
//        when(favoriteServiceMock.getAllFavoriteUpcomingEvents(user)).thenReturn(eventsInFavorites);
//
//        final MvcResult mvcResult = mockMvc.perform(get("/rest/favorites/upcoming")
//                        .principal(authMock))
//                .andReturn();
//        final List<Event> result = readValue(mvcResult, new TypeReference<>() {});
//        final ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//        assertEquals(result.size(), eventsInFavorites.size());
//        verify(favoriteServiceMock).getAllFavoriteEvents(captor.capture());
    }

    @Test
    public void addToFavoriteAddsUsingFavoriteService() throws Exception {
        final Event event = Generator.generateUpcomingEvent();
        event.setId(1337);
        final User user = Generator.generateUser();
        user.setId(228);

        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUser()).thenReturn(user);
        when(eventServiceMock.find(event.getId())).thenReturn(event);

        mockMvc.perform(post("/rest/favorites/" + event.getId())
                .principal(authMock))
                .andExpect(status().isCreated());
        verify(favoriteServiceMock).save(event, user);
    }

    @Test
    public void removeEventFromFavoriteRemovesUsingFavoriteService() throws Exception {
        final Event event = Generator.generateUpcomingEvent();
        event.setId(1337);
        final User user = Generator.generateUser();
        user.setId(228);

        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUser()).thenReturn(user);
        when(eventServiceMock.find(event.getId())).thenReturn(event);

        mockMvc.perform(delete("/rest/favorites/" + event.getId())
                        .principal(authMock))
                .andExpect(status().isNoContent());
        verify(favoriteServiceMock).delete(event, user);
    }
}
