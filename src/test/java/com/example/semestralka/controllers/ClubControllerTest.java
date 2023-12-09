package com.example.semestralka.controllers;

import com.example.semestralka.controllers.handler.ErrorInfo;
import com.example.semestralka.environment.Generator;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.services.ClubService;
import com.example.semestralka.services.EventService;
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

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClubControllerTest extends BaseControllerTestRunner{

    @Mock
    private ClubService clubServiceMock;

    @Mock
    private EventService eventServiceMock;

    @InjectMocks
    private ClubController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }

    @Test
    public void getByIdReturnsClubWithMatchingId() throws Exception {
        final Club club = Generator.generateClub();
        club.setId(1337);
        when(clubServiceMock.find(club.getId())).thenReturn(club);

        final MvcResult mvcResult = mockMvc.perform(get("/rest/clubs/" + club.getId())).andReturn();
        final Event result = readValue(mvcResult, Event.class);

        assertNotNull(result);
        assertEquals(club.getId(), result.getId());
        assertEquals(club.getName(), result.getName());
    }

    @Test
    public void getByIdThrowsNotFoundForUnknownId() throws Exception {
        final int id = 123;
        final MvcResult mvcResult = mockMvc.perform(get("/rest/clubs/" + id)).andExpect(status().isNotFound())
                .andReturn();
        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("Club identified by "));
        assertThat(result.getMessage(), containsString(Integer.toString(id)));
    }

    @Test
    public void getAllUpcomingEventsByClubGetsAllEventsByUsingEventService() throws Exception {
        final List<Event> upcomingEvents = IntStream.range(0, 5).mapToObj(i -> {
            final Event event = Generator.generateUpcomingEvent();
            event.setId(Generator.randomInt());
            return event;
        }).toList();
        final Club club = Generator.generateClub();
        club.setEvents(upcomingEvents);
        club.setId(1337);

        when(eventServiceMock.getAllUpcomingByClub(club)).thenReturn(upcomingEvents);
        when(clubServiceMock.find(club.getId())).thenReturn(club);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/clubs/" + club.getId() + "/events")).andReturn();
        final List<Event> result = readValue(mvcResult, new TypeReference<>() {});
        final ArgumentCaptor<Club> captor = ArgumentCaptor.forClass(Club.class);
        assertEquals(result.size(), upcomingEvents.size());
        verify(eventServiceMock).getAllUpcomingByClub(captor.capture());
    }

    @Test
    public void createClubCreatesClubByUsingClubService() throws Exception {
        final Club club = Generator.generateClub();
        club.setId(1337);
        mockMvc.perform(post("/rest/clubs").content(toJson(club))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        final ArgumentCaptor<Club> captor = ArgumentCaptor.forClass(Club.class);
        verify(clubServiceMock).save(captor.capture());
        assertEquals(club.getName(), captor.getValue().getName());
    }

    @Test
    public void removeClubRemovesClubByUsingService() throws Exception {
        final Club club = Generator.generateClub();
        club.setId(1337);
        when(clubServiceMock.find(club.getId())).thenReturn(club);
        mockMvc.perform(delete("/rest/clubs/" + club.getId())).andExpect(status().isNoContent());
        verify(clubServiceMock).delete(club);
    }
}













