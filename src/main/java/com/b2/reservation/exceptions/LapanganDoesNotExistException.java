package com.b2.reservation.exceptions;

import lombok.Generated;

@Generated
public class LapanganDoesNotExistException extends RuntimeException{
    public LapanganDoesNotExistException(Integer id) {
        super("Lapangan dengan ID: " + id + " tidak ditemukan!");
    }
}
