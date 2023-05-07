package com.b2.reservation.exceptions.advice;

import com.b2.reservation.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {DateTimeIsNotValidException.class})
    public ResponseEntity<Object> dateTimeIsNotValid(){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorTemplate baseException = new ErrorTemplate(
                "Date-time is not valid",
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, status);
    }

    @ExceptionHandler(value = {LapanganDoesNotExistException.class, ReservasiDoesNotExistException.class, KuponDoesNotExistException.class})
    public ResponseEntity<Object> lapanganAndReservasiAndKuponDoesNotExist(Exception exception){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, status);
    }

    @ExceptionHandler(value = {LapanganIsNotAvailableException.class})
    public ResponseEntity<Object> lapanganIsNotAvailable(){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                "Lapangan is not available for inputted datetime",
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, status);
    }
}
