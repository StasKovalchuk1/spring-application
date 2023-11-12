package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.security.PublicKey;
import java.util.List;

@Entity
@Data
public class User extends AbstractEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    public List<Favorite> favorites;

    @OneToMany(mappedBy = "user")
    public List<Comment> comments;

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void addFavorite(Favorite favorite){
        this.favorites.add(favorite);
    }

}
