package com.example.semestralka.data;

import com.example.semestralka.SemestralkaApplication;
import com.example.semestralka.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import static com.example.semestralka.enviroment.Generator.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackageClasses = SemestralkaApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Test
    public void getByUsernameTest() {
        User user = generateUser();
        userRepository.save(user);

        User result = userRepository.getByUsername(user.getUsername());
        assertEquals(user.getUsername(), result.getUsername());
    }
}
