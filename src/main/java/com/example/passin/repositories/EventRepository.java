package com.example.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.passin.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, String>{
  
}
