package com.b2.reservation.exceptions;

public class KuponDoesNotExistException extends RuntimeException {
    public KuponDoesNotExistException(Integer id) {
        super("Kupon dengan ID: " + id + " tidak ditemukan!");
    }
}
