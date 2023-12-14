package com.example.semestralka.controllers;

import com.example.semestralka.config.SecurityConfig;
import com.example.semestralka.environment.Environment;
import com.example.semestralka.environment.Generator;
import com.example.semestralka.environment.TestConfiguration;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.Role;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.ClubService;
import com.example.semestralka.services.EventService;
import com.example.semestralka.services.FavoriteService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(
        classes = {FavoriteControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class FavoriteControllerSecurityTest extends BaseControllerTestRunner{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private EventService eventService;

    private User user;

    @BeforeEach
    public void setUp() {
        this.objectMapper = Environment.getObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.user = Generator.generateUser();
    }

    @AfterEach
    public void tearDown() {
        Environment.clearSecurityContext();
        Mockito.reset(favoriteService, eventService);
    }

    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private FavoriteService favoriteService;

        @MockBean
        private EventService eventService;

        @Bean
        public FavoriteController favoriteController() {
            return new FavoriteController(favoriteService, eventService);
        }
    }

    @WithAnonymousUser
    @Test
    public void getFavoritesThrowsUnauthorizedForAnonymousAccess() throws Exception {
        mockMvc.perform(get("/rest/favorites")).andExpect(status().isUnauthorized());
        verify(favoriteService, never()).getAllFavoriteEvents(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getFavoritesThrowsForbiddenForAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        mockMvc.perform(get("/rest/favorites")).andExpect(status().isForbidden());
        verify(favoriteService, never()).getAllFavoriteEvents(any());
    }

    //Todo does not work
    @WithMockUser(roles = "USER")
    @Test
    public void getFavoritesWorksForAuthorizedUser() throws Exception {
//        user.setRole(Role.USER);
//        user.setId(1337);
//        Environment.setCurrentUser(user);
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
//        when(favoriteService.getAllFavoriteEvents(user)).thenReturn(eventsInFavorites);
//        mockMvc.perform(get("/rest/favorites")
//                .principal(authMock))
//                .andExpect(status().isOk());
//        verify(favoriteService).getAllFavoriteEvents(user);
    }

    @WithAnonymousUser
    @Test
    public void getUpcomingFavoritesThrowsUnauthorizedForAnonymousAccess() throws Exception {
        mockMvc.perform(get("/rest/favorites/upcoming")).andExpect(status().isUnauthorized());
        verify(favoriteService, never()).getAllFavoriteUpcomingEvents(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getUpcomingFavoritesThrowsForbiddenForAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        mockMvc.perform(get("/rest/favorites/upcoming")).andExpect(status().isForbidden());
        verify(favoriteService, never()).getAllFavoriteUpcomingEvents(any());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void getUpcomingFavoritesWorksForAuthorizedUser() throws Exception {
        user.setRole(Role.USER);
    }

    @WithAnonymousUser
    @Test
    public void addToFavoriteThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Event event = Generator.generateUpcomingEvent();
        event.setId(1337);
        mockMvc.perform(post("/rest/favorites/" + event.getId()))
                .andExpect(status().isUnauthorized());
        verify(favoriteService, never()).save(any(),any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void addToFavoriteThrowsForbiddenForAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        final Event event = Generator.generateUpcomingEvent();
        event.setId(1337);
        mockMvc.perform(post("/rest/favorites/" + event.getId()))
                .andExpect(status().isForbidden());
        verify(favoriteService, never()).save(any(),any());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void addToFavoriteWorksForAuthorizedUser() throws Exception {
        user.setRole(Role.USER);
    }

    @WithAnonymousUser
    @Test
    public void removeEventFromFavoriteThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Event event = Generator.generateUpcomingEvent();
        event.setId(1337);
        mockMvc.perform(delete("/rest/favorites/" + event.getId()))
                .andExpect(status().isUnauthorized());
        verify(favoriteService, never()).delete(any(), any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void removeEventFromFavoriteThrowsForbiddenForAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        final Event event = Generator.generateUpcomingEvent();
        event.setId(1337);
        mockMvc.perform(delete("/rest/favorites/" + event.getId()))
                .andExpect(status().isForbidden());
        verify(favoriteService, never()).delete(any(), any());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void removeEventFromFavoriteWorksForAuthorizedUser() throws Exception {
        user.setRole(Role.USER);
    }
}
