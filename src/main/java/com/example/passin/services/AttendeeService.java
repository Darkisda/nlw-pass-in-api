package com.example.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.passin.domain.attendee.Attendee;
import com.example.passin.domain.checkin.Checkin;
import com.example.passin.dto.attendee.AttendeeDetailsDTO;
import com.example.passin.dto.attendee.AttendeesListResponseDTO;
import com.example.passin.repositories.AttendeeRepository;
import com.example.passin.repositories.CheckinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final AttendeeRepository repository;
  private final CheckinRepository checkinRepository;

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    return this.repository.findByEventId(eventId);
  }

  public AttendeesListResponseDTO getEventsAttendee(String eventId) {
    List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);
    List<AttendeeDetailsDTO> attendeeDtos = attendees.stream().map(attendee -> {
      Optional<Checkin> checkin = this.checkinRepository.findByAttendeeId(attendee.getId());
      LocalDateTime checkedInAt = checkin.<LocalDateTime>map(Checkin::getCreatedAt).orElse(null);
      return new AttendeeDetailsDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(),
          checkedInAt);
    }).toList();
    return new AttendeesListResponseDTO(attendeeDtos);
  }
}
