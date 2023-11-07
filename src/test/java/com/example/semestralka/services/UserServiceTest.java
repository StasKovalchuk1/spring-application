package com.example.semestralka.services;


import com.example.semestralka.enviroment.Generator;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void findExistingUserFindsUser(){
        User user = Generator.generateUser();
        userService.save(user);

        User foundUser = userService.find(user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void findNotExistingUserReturnsNull(){
        User foundUser = userService.find(Generator.randomInt());
        assertNull(foundUser);
    }
}
