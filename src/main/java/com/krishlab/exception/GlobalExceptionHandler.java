package com.krishlab.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.krishlab.dto.ErrorResponse;

@ControllerAdvice 
public class GlobalExceptionHandler {

	// Handle specific Exceptions

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
		Instant instant = Instant.now();
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(),
				request.getDescription(false), instant.toEpochMilli());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleResourceNotFoundException(AccessDeniedException exception, WebRequest request) {
		Instant instant = Instant.now();
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage(),
				request.getDescription(false), instant.toEpochMilli());
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	// Handle global Exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {

		Instant instant = Instant.now();
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(),
				request.getDescription(false), instant.toEpochMilli());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
