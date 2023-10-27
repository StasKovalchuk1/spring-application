package com.example.semestralka.services;

import com.example.semestralka.data.EventRepository;
import com.example.semestralka.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
public class EventService {

    private final EventRepository eventRepo;

    @Autowired
    public EventService(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public Event find(Integer id){
        Objects.requireNonNull(id);
        return eventRepo.findById(id).orElse(null);
    }

    public Iterable<Event> findAll(){
        try {
            return eventRepo.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("There are no events");
        }
    }
    @Transactional
    public void save(Event event){
        Objects.requireNonNull(event);
        eventRepo.save(event);
    }

    @Transactional
    public void update(Event event){
        Objects.requireNonNull(event);
        if (exists(event.getId())) {
            eventRepo.save(event);
        }
    }

    @Transactional
    public void delete(Event event){
        Objects.requireNonNull(event);
        if (exists(event.getId())) {
            eventRepo.delete(event);
        }
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return  eventRepo.existsById(id);
    }
}
