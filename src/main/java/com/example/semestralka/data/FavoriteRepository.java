package com.example.semestralka.data;

import com.example.semestralka.model.Favorite;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {
}
