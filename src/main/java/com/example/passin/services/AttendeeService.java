package com.example.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.passin.domain.attendee.Attendee;
import com.example.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.example.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.example.passin.domain.checkin.Checkin;
import com.example.passin.dto.attendee.AttendeeBadgeDTO;
import com.example.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.example.passin.dto.attendee.AttendeeDetailsDTO;
import com.example.passin.dto.attendee.AttendeesListResponseDTO;
import com.example.passin.repositories.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final AttendeeRepository repository;
  private final CheckInService checkInService;

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    return this.repository.findByEventId(eventId);
  }

  public AttendeesListResponseDTO getEventsAttendee(String eventId) {
    List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);
    List<AttendeeDetailsDTO> attendeeDtos = attendees.stream().map(attendee -> {
      Optional<Checkin> checkin = this.checkInService.getCheckIn(attendee.getId());
      LocalDateTime checkedInAt = checkin.<LocalDateTime>map(Checkin::getCreatedAt).orElse(null);
      return new AttendeeDetailsDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(),
          checkedInAt);
    }).toList();
    return new AttendeesListResponseDTO(attendeeDtos);
  }

  public Attendee registerAttendee(Attendee newAttendee) {
    this.repository.save(newAttendee);
    return newAttendee;
  }

  public void verifyAttendeeSubscription(String eventId, String email) {
    var isAttendeeRegistered = this.repository.findByEventIdAndEmail(eventId, email);
    if (isAttendeeRegistered.isPresent())
      throw new AttendeeAlreadyExistException("Attendee already registered");
  }

  public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    var attendee = this.getAttendeeById(attendeeId);

    var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toString();

    return new AttendeeBadgeResponseDTO(
        new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId()));
  }

  public void checkInAttendee(String attendeeId) {
    var attendee = this.getAttendeeById(attendeeId);
    this.checkInService.registerCheckIn(attendee);
  }

  private Attendee getAttendeeById(String attendeeId) {
    return this.repository.findById(attendeeId)
        .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found"));
  }
}
