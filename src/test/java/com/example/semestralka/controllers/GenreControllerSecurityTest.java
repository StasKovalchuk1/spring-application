package com.example.semestralka.controllers;

import com.example.semestralka.config.SecurityConfig;
import com.example.semestralka.environment.Environment;
import com.example.semestralka.environment.Generator;
import com.example.semestralka.environment.TestConfiguration;
import com.example.semestralka.environment.WithCustomMockUser;
import com.example.semestralka.model.Genre;
import com.example.semestralka.model.Role;
import com.example.semestralka.model.User;
import com.example.semestralka.services.EventService;
import com.example.semestralka.services.GenreService;
import com.example.semestralka.services.UserService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(
        classes = {GenreControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class GenreControllerSecurityTest extends BaseControllerTestRunner{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreService genreService;

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
        Mockito.reset(genreService, eventService);
    }

    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private GenreService genreService;

        @MockBean
        private EventService eventService;

        @Bean
        public GenreController genreController() {
            return new GenreController(genreService, eventService);
        }
    }

    @WithCustomMockUser(id = 228, username = "testUsername", role = Role.ADMIN)
    @Test
    public void addGenreWorksForAdmin() throws Exception {
        final Genre toAdd = Generator.generateGenre();
        mockMvc.perform(post("/rest/genres")
                        .content(toJson(toAdd)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        final ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);
        verify(genreService).save(captor.capture());
        assertEquals(toAdd.getName(), captor.getValue().getName());
    }

    @WithCustomMockUser(id = 228, username = "testUsername", role = Role.USER)
    @Test
    public void addGenreThrowsForbiddenForRegularUser() throws Exception {
        final Genre toAdd = Generator.generateGenre();
        mockMvc.perform(post("/rest/genres")
                        .content(toJson(toAdd)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        final ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);
        verify(genreService, never()).save(captor.capture());
    }

    @WithAnonymousUser
    @Test
    public void addGenreThrowsUnauthorizedForUnauthorizedUser() throws Exception {
        final Genre toAdd = Generator.generateGenre();
        mockMvc.perform(post("/rest/genres")
                        .content(toJson(toAdd)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
        final ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);
        verify(genreService, never()).save(captor.capture());
    }
}
