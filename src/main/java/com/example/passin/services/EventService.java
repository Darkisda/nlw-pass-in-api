package com.example.passin.services;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.passin.domain.attendee.Attendee;
import com.example.passin.domain.event.Event;
import com.example.passin.domain.event.exceptions.EventFullException;
import com.example.passin.domain.event.exceptions.EventNotFoundException;
import com.example.passin.dto.attendee.AttendeeIdDTO;
import com.example.passin.dto.attendee.AttendeeRequestDTO;
import com.example.passin.dto.event.EventIdDTO;
import com.example.passin.dto.event.EventRequestDTO;
import com.example.passin.dto.event.EventResponseDTO;
import com.example.passin.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;
  private final AttendeeService attendeeService;

  public List<EventIdDTO> getEvents() {
    var events = this.eventRepository.findAll();
    return events.stream()
        .map(event -> new EventIdDTO(event.getId())).toList();

  }

  public EventResponseDTO getEventDetails(String eventId) {
    var event = this.getEventById(eventId);
    var attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
    return new EventResponseDTO(event, attendeeList.size());
  }

  public EventIdDTO createEvent(EventRequestDTO eventDto) {
    var newEvent = new Event();
    newEvent.setTitle(eventDto.title());
    newEvent.setDetails(eventDto.details());
    newEvent.setMaximumAttendees(eventDto.maximumAttendees());
    newEvent.setSlug(this.createSlug(eventDto.title()));
    var event = this.eventRepository.save(newEvent);

    return new EventIdDTO(event.getId());
  }

  public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {
    this.attendeeService.verifyAttendeeSubscription(eventId, attendeeRequestDTO.email());

    var event = this.getEventById(eventId);
    var attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

    if (event.getMaximumAttendees() <= attendeeList.size())
      throw new EventFullException("Event is full");

    var newAttendee = new Attendee();
    newAttendee.setName(attendeeRequestDTO.name());
    newAttendee.setEmail(attendeeRequestDTO.email());
    newAttendee.setEvent(event);
    newAttendee.setCreatedAt(LocalDateTime.now());

    var createdAttendee = this.attendeeService.registerAttendee(newAttendee);
    return new AttendeeIdDTO(createdAttendee.getId());
  }

  private Event getEventById(String eventId) {
    return this.eventRepository.findById(eventId)
        .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
  }

  private String createSlug(String text) {
    return Normalizer.normalize(text, Form.NFD).replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
        .replaceAll("[^\\w\\s]", "").replaceAll("[\\s+]", "-").toLowerCase();
  }
}
