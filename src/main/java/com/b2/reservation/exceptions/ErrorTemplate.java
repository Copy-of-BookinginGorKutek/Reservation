package com.b2.reservation.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ErrorTemplate(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
}
