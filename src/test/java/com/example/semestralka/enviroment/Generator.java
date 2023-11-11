package com.example.semestralka.enviroment;
import com.example.semestralka.model.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public static Event generateUpcomingEvent() {
        final Event e = new Event();
        e.setName("name" + randomInt());
        e.setPrice(randomInt());
        e.setDescription("description" + randomInt());
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime eventDate = currentDate.plus(1, ChronoUnit.DAYS);
        e.setEventDate(eventDate);
        return e;
    }

    public static Event generateFinishedEvent() {
        final Event e = new Event();
        e.setName("name" + randomInt());
        e.setPrice(randomInt());
        e.setDescription("description" + randomInt());
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime eventDate = currentDate.minus(1, ChronoUnit.DAYS);
        e.setEventDate(eventDate);
        return e;
    }

    public static Club generateClub() {
        final Club c = new Club();
        c.setName("name" + randomInt());
        return c;
    }

    public static Favorite generateFavorite(Event event, User user) {
        final Favorite f = new Favorite();
        final FavoriteId favoriteId = new FavoriteId();
        favoriteId.setUserId(user.getId());
        favoriteId.setEventId(event.getId());
        f.setId(favoriteId);
        f.setUser(user);
        f.setEvent(event);
        return f;
    }

}
