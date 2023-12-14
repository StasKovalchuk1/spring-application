package com.example.semestralka.controllers;

import com.example.semestralka.config.SecurityConfig;
import com.example.semestralka.environment.Environment;
import com.example.semestralka.environment.Generator;
import com.example.semestralka.environment.TestConfiguration;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Role;
import com.example.semestralka.model.User;
import com.example.semestralka.services.ClubService;
import com.example.semestralka.services.EventService;
import com.example.semestralka.services.GenreService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(
        classes = {EventControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class EventControllerSecurityTest extends BaseControllerTestRunner{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventService eventService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ClubService clubService;

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
        Mockito.reset(eventService, genreService);
    }


    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private EventService eventService;

        @MockBean
        private GenreService genreService;

        @MockBean
        private ClubService clubService;

        @Bean
        public EventController eventController() {
            return new EventController(eventService, genreService, clubService);
        }
    }

    @WithAnonymousUser
    @Test
    public void acceptEventThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Event toAccept = Generator.generateUpcomingEvent();
        mockMvc.perform(post("/rest/events/accept").content(toJson(toAccept))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
        verify(eventService, never()).acceptEvent(any());
    }

    @WithMockUser
    @Test
    public void acceptEventThrowsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        final Event toAccept = Generator.generateUpcomingEvent();
        mockMvc.perform(post("/rest/events/accept").content(toJson(toAccept))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        verify(eventService, never()).acceptEvent(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void acceptEventWorksWithAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Event toAccept = Generator.generateUpcomingEvent();
        mockMvc.perform(post("/rest/events/accept").content(toJson(toAccept))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(eventService).acceptEvent(toAccept);
    }

    @WithAnonymousUser
    @Test
    public void getAllNotAcceptedThrowsUnauthorizedForAnonymousAccess() throws Exception {
        mockMvc.perform(get("/rest/events/not_accepted")).andExpect(status().isUnauthorized());
        verify(eventService, never()).getAllNotAccepted();
    }

    @WithMockUser
    @Test
    public void getAllNotAcceptedThrowsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        mockMvc.perform(get("/rest/events/not_accepted")).andExpect(status().isForbidden());
        verify(eventService, never()).getAllNotAccepted();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void getAllNotAcceptedWorksWithAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        mockMvc.perform(get("/rest/events/not_accepted")).andExpect(status().isOk());
        verify(eventService).getAllNotAccepted();
    }

    @WithAnonymousUser
    @Test
    public void removeEventThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Event toRemove = Generator.generateUpcomingEvent();
        toRemove.setId(123);
        mockMvc.perform(delete("/rest/events/" + toRemove.getId()))
                .andExpect(status().isUnauthorized());
        verify(eventService, never()).delete(any());
    }

    @WithMockUser
    @Test
    public void removeEventThrowsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        final Event toRemove = Generator.generateUpcomingEvent();
        toRemove.setId(123);
        mockMvc.perform(delete("/rest/events/" + toRemove.getId()))
                .andExpect(status().isForbidden());
        verify(eventService, never()).delete(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void removeEventWorksWithAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Event toRemove = Generator.generateUpcomingEvent();
        toRemove.setId(123);
        when(eventService.find(toRemove.getId())).thenReturn(toRemove);
        mockMvc.perform(delete("/rest/events/" + toRemove.getId()))
                .andExpect(status().isNoContent());
        verify(eventService).delete(toRemove);
    }

    @WithAnonymousUser
    @Test
    public void createEventByUserThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Event toCreate = Generator.generateUpcomingEvent();
        final Club club = Generator.generateClub();
        mockMvc.perform(post("/rest/events/create/" + club.getName()).content(toJson(toCreate))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
        verify(eventService, never()).createEventByUser(any(), any());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void createEventByUserWorksWithRegularUser() throws Exception {
        user.setRole(Role.USER);
        Environment.setCurrentUser(user);
        final Event toCreate = Generator.generateUpcomingEvent();
        final Club club = Generator.generateClub();
        when(clubService.findByName(club.getName())).thenReturn(club);
        mockMvc.perform(post("/rest/events/create/" + club.getName()).content(toJson(toCreate))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(eventService).createEventByUser(toCreate, club);
    }
}
