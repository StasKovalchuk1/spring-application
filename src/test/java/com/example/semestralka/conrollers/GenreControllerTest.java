package com.example.semestralka.conrollers;

import com.example.semestralka.controllers.EventController;
import com.example.semestralka.controllers.GenreController;
import com.example.semestralka.controllers.handler.ErrorInfo;
import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Genre;
import com.example.semestralka.services.EventService;
import com.example.semestralka.services.GenreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GenreControllerTest extends BaseControllerTestRunner{
    @Mock
    private EventService eventServiceMock;

    @Mock
    private GenreService genreServiceMock;

    @InjectMocks
    private GenreController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }

    @Test
    public void getByIdReturnsEventWithMatchingId() throws Exception{
        final Genre genre = Generator.generateGenre();
        genre.setId(123);

        when(genreServiceMock.find(genre.getId())).thenReturn(genre);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/genres/" + genre.getId())).andReturn();
        final Event result = readValue(mvcResult, Event.class);
        assertNotNull(result);
        assertEquals(genre.getId(), result.getId());
        assertEquals(genre.getName(), result.getName());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/genres/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Genre identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));
    }

    @Test
    public void addGenreAddsGenreUsingService() throws Exception {
        final Genre toAdd = Generator.generateGenre();

        mockMvc.perform(post("/rest/genres").content(toJson(toAdd)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        final ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);
        verify(genreServiceMock).save(captor.capture());
        assertEquals(toAdd.getName(), captor.getValue().getName());
    }

    @Test
    public void getEventsByGenreReturnsEventsUsingService() throws Exception {
        final Genre genre = Generator.generateGenre();
        genre.setId(123);
        when(genreServiceMock.find(any())).thenReturn(genre);
        List<Event> events = Arrays.asList(Generator.generateUpcomingEvent(), Generator.generateUpcomingEvent());
        when(eventServiceMock.getAllByGenre(genre)).thenReturn(events);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/genres/" + genre.getId() + "/events"))
                .andExpect(status().isOk()).andReturn();
        final List<Event> result = readValue(mvcResult, new TypeReference<List<Event>>() {
        });

        assertNotNull(result);
        assertEquals(result.size(), events.size());
    }

    @Test
    public void addEventToGenreAddsEventToSpecifiedGenre() throws Exception {
        final Genre genre = Generator.generateGenre();
        genre.setId(123);
        when(genreServiceMock.find(any())).thenReturn(genre);

        final Event toAdd = Generator.generateUpcomingEvent();
        toAdd.setId(321);

        mockMvc.perform(post("/rest/genres/" + genre.getId() + "/events").content(toJson(toAdd)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();

        final ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(genreServiceMock).addEvent(eq(genre), captor.capture());
        assertEquals(toAdd.getId(), captor.getValue().getId());
    }

    @Test
    public void addEventToGenreThrowsNotFoundForUnknownGenre() throws Exception {
        final int genreId = 123;

        final Event toAdd = Generator.generateUpcomingEvent();
        toAdd.setId(321);

        mockMvc.perform(post("/rest/genres/" + genreId + "/events").content(toJson(toAdd))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        verify(genreServiceMock).find(genreId);
        verify(genreServiceMock, never()).addEvent(any(), any());
    }

    @Test
    public void removeEventRemovesEventFromGenre() throws Exception {
        final Genre genre = Generator.generateGenre();
        genre.setId(123);
        when(genreServiceMock.find(any())).thenReturn(genre);
        final Event event = Generator.generateUpcomingEvent();
        event.setId(Generator.randomInt());
        event.addGenre(genre);
        when(eventServiceMock.find(any())).thenReturn(event);
        mockMvc.perform(delete("/rest/genres/" + genre.getId() + "/events/" + event.getId()))
                .andExpect(status().isNoContent());
        verify(genreServiceMock).removeEvent(genre, event);
    }

    @Test
    public void removeEventThrowsNotFoundForUnknownEventId() throws Exception {
        final Genre genre = Generator.generateGenre();
        genre.setId(123);
        when(genreServiceMock.find(any())).thenReturn(genre);
        final int unknownId = 123;

        mockMvc.perform(delete("/rest/genres/" + genre.getId() + "/events/" + unknownId))
                .andExpect(status().isNotFound());
        verify(genreServiceMock).find(genre.getId());
        verify(genreServiceMock, never()).removeEvent(any(), any());
    }
}
