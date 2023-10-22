package com.example.semestralka.data;


import com.example.semestralka.model.Ticket;
import com.example.semestralka.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends CrudRepository<Ticket, Integer> {

}
