package com.example.semestralka.controllers;

import com.example.semestralka.environment.Generator;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.User;
import com.example.semestralka.security.model.UserDetails;
import com.example.semestralka.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest extends BaseControllerTestRunner{

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private UserController sut;

    @BeforeEach
    public void setUp() {
        super.setUp(sut);
    }

    @Test
    public void getAllGetsAllUsersByUsingUserService() throws Exception {
        List<User> users = new ArrayList<>();
        for (int i = 0; i<5; ++i){
            User user = Generator.generateUser();
            user.setId(i);
            users.add(user);
        }
        when(userServiceMock.findAll()).thenReturn(users);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/users")).andReturn();
        final List<User> result = readValue(mvcResult, new TypeReference<>() {
        });
        assertNotNull(result);
        assertEquals(result.size(), users.size());
        verify(userServiceMock).findAll();
    }
    @Test
    public void registerSavesByUsingUserService() throws Exception {
        User user = Generator.generateUser();
        mockMvc.perform(post("/rest/users")
                .content(toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(userServiceMock).save(user);
    }

    @Test
    public void updateUserUpdatesByUsingUserService() throws Exception{
        User currentUser = Generator.generateUser();
        currentUser.setUsername("Stas");
        currentUser.setId(1337);
        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUser()).thenReturn(currentUser);
        when(userServiceMock.exists(currentUser.getId())).thenReturn(true);

        User updatedUser = new User();
        //only username was updated
        updatedUser.setUsername("Daniil");
        updatedUser.setId(currentUser.getId());
        updatedUser.setPhoneNumber(currentUser.getPhoneNumber());
        updatedUser.setEmail(currentUser.getEmail());
        updatedUser.setPassword(updatedUser.getPassword());
        updatedUser.setRole(currentUser.getRole());

        mockMvc.perform(put("/rest/users/myProfile/update")
                .content(toJson(updatedUser))
                .contentType(MediaType.APPLICATION_JSON)
                .principal(authMock))
                .andExpect(status().isOk());
        verify(userServiceMock).update(any(User.class));
    }

    @Test
    public void updateUserThrowsUnauthorizedIfIdOfUpdatedUserIsDifferent() throws Exception{
        User currentUser = Generator.generateUser();
        currentUser.setUsername("Stas");
        currentUser.setId(1337);
        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUser()).thenReturn(currentUser);

        User updatedUser = new User();
        //username was updated, id is different
        updatedUser.setUsername("Daniil");
        updatedUser.setId(111);
        updatedUser.setPhoneNumber(currentUser.getPhoneNumber());
        updatedUser.setEmail(currentUser.getEmail());
        updatedUser.setPassword(updatedUser.getPassword());
        updatedUser.setRole(currentUser.getRole());
        mockMvc.perform(put("/rest/users/myProfile/update")
                        .content(toJson(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(authMock))
                .andExpect(status().isUnauthorized());
        verify(userServiceMock, never()).update(any());
    }

    @Test
    public void deleteUserDeletesByUsingUserService() throws Exception{
        User userToDelete = Generator.generateUser();
        userToDelete.setId(1337);
        when(userServiceMock.find(userToDelete.getId())).thenReturn(userToDelete);

        mockMvc.perform(delete("/rest/users/" + userToDelete.getId()))
                .andExpect(status().isNoContent());
        verify(userServiceMock).delete(userToDelete);
    }

    @Test
    public void deleteAccountDeletesByUsingUserService() throws Exception{
        User currentUser = Generator.generateUser();
        currentUser.setId(1337);
        Authentication authMock = mock(Authentication.class);
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(authMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUser()).thenReturn(currentUser);

        mockMvc.perform(delete("/rest/users/myProfile/delete")
                        .principal(authMock))
                .andExpect(status().isNoContent());
        verify(userServiceMock).delete(currentUser);
    }

}
