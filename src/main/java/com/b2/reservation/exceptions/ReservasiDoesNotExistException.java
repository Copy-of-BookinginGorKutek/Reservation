package com.b2.reservation.exceptions;

import lombok.Generated;

@Generated
public class ReservasiDoesNotExistException extends RuntimeException {
    public ReservasiDoesNotExistException(Integer id) {
        super("Reservasi dengan ID: " + id + " tidak ditemukan!");
    }
}
