package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.security.PublicKey;
import java.util.List;

@Entity
@Data
@DiscriminatorValue("USER")
public class User extends Person{

    @OneToMany(mappedBy = "user")
    public List<Favorite> favorites;

}
