package com.example.passin.services;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.passin.domain.event.Event;
import com.example.passin.domain.event.exceptions.EventNotFoundException;
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

  public List<EventResponseDTO> getEvents() {
    var events = this.eventRepository.findAll();
    return events.stream()
        .map(event -> new EventResponseDTO(event, event.getMaximumAttendees())).toList();

  }

  public EventResponseDTO getEventDetails(String eventId) {
    var event = this.eventRepository.findById(eventId)
        .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
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

  private String createSlug(String text) {
    return Normalizer.normalize(text, Form.NFD).replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
        .replaceAll("[^\\w\\s]", "").replaceAll("[\\s+]", "-").toLowerCase();
  }
}
