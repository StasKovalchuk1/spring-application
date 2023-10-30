package com.example.semestralka.services;

import com.example.semestralka.data.FavoriteRepository;
import com.example.semestralka.model.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepo) {
        this.favoriteRepo = favoriteRepo;
    }

//    public Favorite find(Integer id){
//        Objects.requireNonNull(id);
//        return favoriteRepo.findById(id).orElse(null);
//    }

    public Iterable<Favorite> findAll(){
        try {
            return favoriteRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no favorite events");
        }
    }
    @Transactional
    public void save(Favorite favorite){
        Objects.requireNonNull(favorite);
        favoriteRepo.save(favorite);
    }

//    @Transactional
//    public void update(Favorite favorite){
//        Objects.requireNonNull(favorite);
//        if (exists(favorite.getId())) {
//            favoriteRepo.save(favorite);
//        }
//    }

//    @Transactional
//    public void delete(Favorite favorite){
//        Objects.requireNonNull(favorite);
//        if (exists(favorite.getId())) {
//            favoriteRepo.delete(favorite);
//        }
//    }

//    public boolean exists(Integer id){
//        Objects.requireNonNull(id);
//        return  favoriteRepo.existsById(id);
//    }
}
