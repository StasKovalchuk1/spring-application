package com.example.semestralka.controllers;

import com.example.semestralka.config.SecurityConfig;
import com.example.semestralka.environment.Environment;
import com.example.semestralka.environment.Generator;
import com.example.semestralka.environment.TestConfiguration;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Role;
import com.example.semestralka.model.User;
import com.example.semestralka.services.ClubService;
import com.example.semestralka.services.EventService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(
        classes = {ClubControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class ClubControllerSecurityTest extends BaseControllerTestRunner{

    @Autowired
    private MockMvc mockMvc;

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
        Mockito.reset(clubService);
    }

    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private ClubService clubService;

        @MockBean
        private EventService eventService;

        @Bean
        public ClubController clubController() {
            return new ClubController(clubService, eventService);
        }
    }

    @WithAnonymousUser
    @Test
    public void createClubThrowsUnauthorizedForAnonymousAccess() throws Exception {
        final Club toCreate = Generator.generateClub();
        mockMvc.perform(post("/rest/clubs").content(toJson(toCreate))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
        verify(clubService, never()).save(toCreate);
    }

    @WithMockUser
    @Test
    public void createClubThrowsForbiddenForRegularUser() throws Exception {
        Environment.setCurrentUser(user);
        final Club toCreate = Generator.generateClub();
        mockMvc.perform(post("/rest/clubs").content(toJson(toCreate))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        verify(clubService, never()).save(toCreate);
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createClubWorksWithAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Club toCreate = Generator.generateClub();
        mockMvc.perform(post("/rest/clubs").content(toJson(toCreate))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @WithAnonymousUser
    @Test
    public void removeClubThrowsUnauthorizedForAnonymousAccess() throws Exception{
        final Club toRemove = Generator.generateClub();
        toRemove.setId(1337);
        mockMvc.perform(delete("/rest/clubs/" + toRemove.getId()))
                .andExpect(status().isUnauthorized());
        verify(clubService, never()).delete(any());
    }

    @WithMockUser
    @Test
    public void removeClubThrowsForbiddenForRegularUser() throws Exception{
        final Club toRemove = Generator.generateClub();
        toRemove.setId(1337);
        mockMvc.perform(delete("/rest/clubs/" + toRemove.getId()))
                .andExpect(status().isForbidden());
        verify(clubService, never()).delete(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void removeClubWorksWithAdmin() throws Exception{
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final Club toRemove = Generator.generateClub();
        toRemove.setId(1337);
        mockMvc.perform(delete("/rest/clubs/" + toRemove.getId()))
                .andExpect(status().isNoContent());
        verify(clubService).delete(any());
    }
}
