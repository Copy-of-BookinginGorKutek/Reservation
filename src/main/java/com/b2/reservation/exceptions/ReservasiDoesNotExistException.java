package com.b2.reservation.exceptions;

public class ReservasiDoesNotExistException extends RuntimeException {
    public ReservasiDoesNotExistException(Integer id) {
        super("Reservasi dengan ID: " + id + " tidak ditemukan!");
    }
}
