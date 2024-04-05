package com.example.passin.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.passin.dto.attendee.AttendeesListResponseDTO;
import com.example.passin.dto.event.EventIdDTO;
import com.example.passin.dto.event.EventRequestDTO;
import com.example.passin.dto.event.EventResponseDTO;
import com.example.passin.services.AttendeeService;
import com.example.passin.services.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
  private final EventService service;
  private final AttendeeService attendeeService;

  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getEvents() {
    var response = this.service.getEvents();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
    var event = this.service.getEventDetails(id);
    return ResponseEntity.ok().body(event);
  }

  @PostMapping
  public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO dto,
      UriComponentsBuilder uriComponentsBuilder) {
    var createdEvent = this.service.createEvent(dto);
    var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(createdEvent.eventId()).toUri();

    return ResponseEntity.created(uri).body(createdEvent);
  }

  @GetMapping("/attendees/{id}")
  public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {
    var events = this.attendeeService.getEventsAttendee(id);
    return ResponseEntity.ok().body(events);
  }
}
