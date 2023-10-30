package com.example.semestralka.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class FavoriteId implements Serializable {

    private int eventId;
    private int userId;

}
