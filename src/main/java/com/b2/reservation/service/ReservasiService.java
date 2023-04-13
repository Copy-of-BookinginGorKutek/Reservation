package com.b2.reservation.service;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.request.UpdateReservasiRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservasiService {
    List<Reservasi> findAll();
    Reservasi findById(Integer id);
    Reservasi create(ReservasiRequest request);
    Reservasi update(Integer id, UpdateReservasiRequest request);
    List<Reservasi> findAllByEmailUser(String email);
    void delete(Integer id);
}
