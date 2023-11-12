package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Table(name = "usersadd")
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
        if (this.comments==null){
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    public void addFavorite(Favorite favorite){
        if (this.favorites==null){
            this.favorites = new ArrayList<>();
        }
        this.favorites.add(favorite);
    }

}
