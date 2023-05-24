package com.b2.reservation.exceptions;

import lombok.Generated;

@Generated
public class KuponDoesNotExistException extends RuntimeException {
    public KuponDoesNotExistException(Integer id) {
        super("Kupon dengan ID: " + id + " tidak ditemukan!");
    }
}
