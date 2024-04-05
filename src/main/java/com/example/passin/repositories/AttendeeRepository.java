package com.example.passin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.passin.domain.attendee.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
  List<Attendee> findByEventId(String eventId);

}
