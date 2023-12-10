package com.example.semestralka.controllers;

import com.example.semestralka.environment.Generator;
import com.example.semestralka.model.User;
import com.example.semestralka.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

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
    public void registerSavesByUsingUserService() throws Exception {
        User user = Generator.generateUser();
        mockMvc.perform(post("/rest/myProfile")
                .content(toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(userServiceMock).save(user);
    }

    @Test
    public void getCurrentGetsCurrentByUsingUserService() throws Exception{}

    @Test
    public void updateUserUpdatesByUsingUserService() throws Exception{}

    @Test
    public void deleteUserDeletesByUsingUserService() throws Exception{}

}
