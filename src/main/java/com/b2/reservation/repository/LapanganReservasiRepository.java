package com.b2.reservation.repository;

import com.b2.reservation.model.lapangan.LapanganReservasi;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface LapanganReservasiRepository extends JpaRepository<LapanganReservasi, Integer>{
    @NonNull
    List<LapanganReservasi> findAll();
    @NonNull
    Optional<LapanganReservasi> findById(@NonNull Integer id);
    List<LapanganReservasi> findAllByIdReservasi(Integer idReservasi);
    List<LapanganReservasi> findAllByIdLapangan(Integer idLapangan);
}
