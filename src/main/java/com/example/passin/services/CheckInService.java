package com.example.passin.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.passin.domain.attendee.Attendee;
import com.example.passin.domain.checkin.Checkin;
import com.example.passin.domain.checkin.exceptions.CheckInAlreadyExistException;
import com.example.passin.repositories.CheckinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckInService {
  private final CheckinRepository repository;

  public void registerCheckIn(Attendee attendee) {
    this.verifyCheckInExists(attendee.getId());
    var newCheckin = new Checkin();
    newCheckin.setAttendee(attendee);
    newCheckin.setCreatedAt(LocalDateTime.now());
    this.repository.save(newCheckin);
  }

  public Optional<Checkin> getCheckIn(String attendeId) {
    return this.repository.findByAttendeeId(attendeId);
  }

  private void verifyCheckInExists(String attendeeId) {
    var checkin = this.repository.findByAttendeeId(attendeeId);
    if (checkin.isPresent())
      throw new CheckInAlreadyExistException("Attendee already check in");
  }
}
