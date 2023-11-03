package com.example.semestralka.data;

import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.FavoriteId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, FavoriteId> {

    List<Favorite> findAllByUserId(Integer user_id);
}
