package com.example.semestralka.controllers;

import com.example.semestralka.config.SecurityConfig;
import com.example.semestralka.environment.Environment;
import com.example.semestralka.environment.Generator;
import com.example.semestralka.environment.TestConfiguration;
import com.example.semestralka.model.Role;
import com.example.semestralka.model.User;
import com.example.semestralka.services.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(
        classes = {UserControllerSecurityTest.TestConfig.class, SecurityConfig.class})
public class UserControllerSecurityTest extends BaseControllerTestRunner {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

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
        Mockito.reset(userService);
    }

    @Configuration
    @TestConfiguration
    public static class TestConfig {

        @MockBean
        private UserService userService;



        @Bean
        public UserController userController() {
            return new UserController(userService);
        }
    }

    @WithAnonymousUser
    @Test
    public void registerWorksForAnonymousUser() throws Exception {
        final User toRegister = Generator.generateUser();
        mockMvc.perform(post("/rest/users")
                                .content(toJson(toRegister))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isCreated());
        verify(userService).save(toRegister);
    }

    @WithAnonymousUser
    @Test
    public void registerAdminThrowsUnauthorizedForAnonymousUser() throws Exception {
        final User toRegister = Generator.generateUser();
        toRegister.setRole(Role.ADMIN);

        mockMvc.perform(post("/rest/users")
                                .content(toJson(toRegister))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isUnauthorized());
        verify(userService, never()).save(any());
    }

    @WithMockUser
    @Test
    public void registerAdminThrowsForbiddenForNonAdmin() throws Exception {
        user.setRole(Role.USER);
        Environment.setCurrentUser(user);
        final User toRegister = Generator.generateUser();
        toRegister.setRole(Role.ADMIN);

        mockMvc.perform(post("/rest/users")
                        .content(toJson(toRegister))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isForbidden());
        verify(userService, never()).save(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void registerWorksForAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        Environment.setCurrentUser(user);
        final User toRegister = Generator.generateUser();
        toRegister.setRole(Role.ADMIN);

        mockMvc.perform(post("/rest/users")
                        .content(toJson(toRegister))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isCreated());
        verify(userService).save(toRegister);
    }

    @WithMockUser
    @Test
    public void updateUserWorkForAuthorizedUser() throws Exception {
        //todo
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateUserThrowsForbiddenForAdmin(){
        //todo
    }

    @WithAnonymousUser
    @Test
    public void updateUserThrowsUnauthorizedForAnonymousUser(){
        //todo
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void deleteUserWorksForAdmin() throws Exception {
        user.setRole(Role.ADMIN);
        final User toDelete = Generator.generateUser();
        toDelete.setId(1337);
        when(userService.find(toDelete.getId())).thenReturn(toDelete);

        mockMvc.perform(delete("/rest/users/" + toDelete.getId()))
                .andExpect(status().isNoContent());
        verify(userService).delete(toDelete);
    }

    @WithMockUser
    @Test
    public void deleteUserThrowsForbiddenForRegularUser() throws Exception {
        user.setRole(Role.USER);
        final User toDelete = Generator.generateUser();
        toDelete.setId(1337);
        when(userService.find(toDelete.getId())).thenReturn(toDelete);

        mockMvc.perform(delete("/rest/users/" + toDelete.getId()))
                .andExpect(status().isForbidden());
        verify(userService, never()).delete(any());
    }


    @WithAnonymousUser
    @Test
    public void deleteUserThrowsUnauthorizedForAnonymousUser() throws Exception {
        final User toDelete = Generator.generateUser();
        toDelete.setId(1337);
        when(userService.find(toDelete.getId())).thenReturn(toDelete);

        mockMvc.perform(delete("/rest/users/" + toDelete.getId()))
                .andExpect(status().isUnauthorized());
        verify(userService, never()).delete(any());
    }

    @WithMockUser
    @Test
    public void deleteAccountWorksForRegularUser(){
        //todo
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void deleteAccountThrowsForbiddenForAdmin(){
        //todo
    }

    @WithAnonymousUser
    @Test
    public void deleteAccountThrowsUnauthorizedForAnonymousUser(){
        //todo
    }
}
