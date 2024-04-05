package com.example.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.example.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.example.passin.domain.checkin.exceptions.CheckInAlreadyExistException;
import com.example.passin.domain.event.exceptions.EventFullException;
import com.example.passin.domain.event.exceptions.EventNotFoundException;
import com.example.passin.dto.general.ErrorResponseDTO;

@ControllerAdvice
public class ExceptionEntityHandler {

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<Object> handleEventNotFound(EventNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(EventFullException.class)
  public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception) {
    return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
  }

  @ExceptionHandler(AttendeeNotFoundException.class)
  public ResponseEntity<Object> handleAttendeeNotFound(AttendeeNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeAlreadyExistException.class)
  public ResponseEntity<Object> handleAttendeeAlreadyExist(AttendeeAlreadyExistException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(CheckInAlreadyExistException.class)
  public ResponseEntity<Object> handleCheckInAlreadyExist(CheckInAlreadyExistException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
}
