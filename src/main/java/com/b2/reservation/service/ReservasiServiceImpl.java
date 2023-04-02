package com.b2.reservation.service;

import com.b2.reservation.exceptions.ReservasiDoesNotExistException;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.request.ReservasiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservasiServiceImpl implements ReservasiService {
    private final ReservasiRepository reservasiRepository;
    @Override
    public List<Reservasi> findAll() {
        return reservasiRepository.findAll();
    }

    @Override
    public Reservasi findById(Integer id) {
        Reservasi reservasi = reservasiRepository.findById(id).orElse(null);
        if (reservasi == null) {
            throw new ReservasiDoesNotExistException(id);
        }
        return reservasi;
    }

    @Override
    public Reservasi create(ReservasiRequest request) {
        Reservasi reservasi = new Reservasi(null, request.getEmailUser(),
                request.getStatusPembayaran(), request.getBuktiTransfer(),
                null);
        return reservasiRepository.save(reservasi);
    }

    @Override
    public Reservasi update(Integer id, ReservasiRequest request) {
        if (isReservasiDoesNotExist(id)) {
            throw new ReservasiDoesNotExistException(id);
        }
        Reservasi reservasi = new Reservasi(id, request.getEmailUser(),
                request.getStatusPembayaran(), request.getBuktiTransfer(),
                null);
        return this.reservasiRepository.save(reservasi);
    }

    @Override
    public void delete(Integer id) {
        if (isReservasiDoesNotExist(id)) {
            throw new ReservasiDoesNotExistException(id);
        }
        reservasiRepository.deleteById(id);
    }

    private boolean isReservasiDoesNotExist(Integer id) {
        return reservasiRepository.findById(id).isEmpty();
    }
}
