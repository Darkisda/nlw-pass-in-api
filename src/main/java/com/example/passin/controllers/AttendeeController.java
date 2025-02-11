package com.example.passin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.example.passin.services.AttendeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {
  private final AttendeeService service;

  @GetMapping("/{attendeeId}/badge")
  public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId,
      UriComponentsBuilder uriComponentsBuilder) {
    var response = this.service.getAttendeeBadge(attendeeId, uriComponentsBuilder);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{attendeeId}/check-in")
  public ResponseEntity<Object> registerCheckIn(@PathVariable String attendeeId,
      UriComponentsBuilder uriComponentsBuilder) {
    this.service.checkInAttendee(attendeeId);

    var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();

    return ResponseEntity.created(uri).build();
  }
}
