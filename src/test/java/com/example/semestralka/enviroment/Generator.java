package com.example.semestralka.enviroment;
import com.example.semestralka.model.Club;
import com.example.semestralka.model.Event;
import com.example.semestralka.model.Genre;
import com.example.semestralka.model.User;

import java.util.Random;

public class Generator {
    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static int randomInt(int max) {
        return RAND.nextInt(max);
    }

    public static int randomInt(int min, int max) {
        assert min >= 0;
        assert min < max;

        int result;
        do {
            result = randomInt(max);
        } while (result < min);
        return result;
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateUser() {
        final User user = new User();
        user.setUsername("username" + randomInt());
        user.setEmail("email" + randomInt());
        user.setPhoneNumber("number" + randomInt());
        return user;
    }

    public static Genre generateGenre() {
        final Genre p = new Genre();
        p.setName("Genre" + randomInt());
        return p;
    }

    public static Event generateEvent() {
        final Event e = new Event();
        e.setName("name" + randomInt());
        e.setPrice(randomInt());
        e.setDescription("description" + randomInt());
        return e;
    }

    public static Club generateClub() {
        final Club c = new Club();
        c.setName("name" + randomInt());
        return c;
    }

}
