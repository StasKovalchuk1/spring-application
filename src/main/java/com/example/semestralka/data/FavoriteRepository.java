package com.example.semestralka.data;

import com.example.semestralka.model.Favorite;
import com.example.semestralka.model.FavoriteId;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<Favorite, FavoriteId> {
}
