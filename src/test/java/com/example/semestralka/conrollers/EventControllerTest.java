package com.example.semestralka.conrollers;

import com.example.semestralka.controllers.EventController;
import com.example.semestralka.controllers.handler.ErrorInfo;
import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.Club;
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
import org.mockito.internal.matchers.Find;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.awt.datatransfer.FlavorListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest extends BaseControllerTestRunner{

    @Mock
    private EventService eventServiceMock;

    @Mock
    private GenreService genreServiceMock;

    @InjectMocks
    private EventController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }

    @Test
    public void getByIdReturnsEventWithMatchingId() throws Exception{
        final Event event = Generator.generateUpcomingEvent();
        event.setId(123);

        when(eventServiceMock.find(event.getId())).thenReturn(event);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/events/" + event.getId())).andReturn();
        final Event result = readValue(mvcResult, Event.class);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getName(), result.getName());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/events/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Event identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));

    }

    @Test
    public void acceptEventAcceptsEventUsingService() throws Exception{
        final Event event = Generator.generateUpcomingEvent();
        event.setAccepted(false);
        mockMvc.perform(post("/rest/events/accept").content(toJson(event))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isAccepted());
        verify(eventServiceMock).acceptEvent(event);
    }

    @Test
    public void getAllNotAcceptedReturnsNotAcceptedEvents() throws Exception{
        final Event event1 = Generator.generateUpcomingEvent();
        event1.setAccepted(false);
        final Event event2 = Generator.generateUpcomingEvent();
        event2.setAccepted(false);
        List<Event> events = Arrays.asList(event1, event2);
        when(eventServiceMock.getAllNotAccepted()).thenReturn(events);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/events/not_accepted")).andReturn();

        final List<Event> result = readValue(mvcResult, new TypeReference<List<Event>>() {
        });
        assertNotNull(result);
        assertEquals(result.size(), events.size());
        verify(eventServiceMock).getAllNotAccepted();
    }

    @Test
    public void getAllUpcomingEventsReturnsUpcomingEvents() throws Exception{
        final Event event1 = Generator.generateUpcomingEvent();
        final Event event2 = Generator.generateUpcomingEvent();
        List<Event> events = Arrays.asList(event1, event2);
        when(eventServiceMock.getAllUpcomingEvents()).thenReturn(events);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/events/")).andReturn();
        final List<Event> result = readValue(mvcResult, new TypeReference<List<Event>>() {
        });
        assertNotNull(result);
        assertEquals(result.size(), events.size());
        verify(eventServiceMock).getAllUpcomingEvents();
    }

    @Test
    public void getAllUpcomingByGenreReturnEventsForGenre() throws Exception{
        final Genre genre = Generator.generateGenre();
        genre.setId(123);
        when(genreServiceMock.findByName(genre.getName())).thenReturn(genre);
        final Event event1 = Generator.generateUpcomingEvent();
        event1.addGenre(genre);
        final Event event2 = Generator.generateUpcomingEvent();
        event2.addGenre(genre);
        final List<Event> events = Arrays.asList(event1, event2);
        when(eventServiceMock.getAllUpcomingByGenres(any())).thenReturn(events);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/events/by_genres")
                .param("genres", genre.getName())).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        final List<Event> result = readValue(mvcResult, new TypeReference<List<Event>>() {
        });
        assertNotNull(result);
        assertEquals(result.size(), events.size());
        verify(eventServiceMock).getAllUpcomingByGenres(any());
        verify(genreServiceMock).findByName(genre.getName());
    }

    // TODO
    @Test
    public void createEventByUserCreatesEventUsingService() throws Exception {
        final Event event = Generator.generateUpcomingEvent();
        event.setClub(null);
        event.setAccepted(false);
        final Club club = Generator.generateClub();
        mockMvc.perform(post("/rest/events/create").contentType(toJson(event) + toJson(club))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        final ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventServiceMock).createEventByUser(event, club);
        assertEquals(event.getName(), captor.getValue().getName());
    }

    @Test
    public void removeEventRemovesUsingService() throws Exception {
        final Event event = Generator.generateUpcomingEvent();
        event.setId(123);
        mockMvc.perform(delete("/rest/events/" + event.getId()));
        verify(eventServiceMock).delete(event.getId());
    }

}
