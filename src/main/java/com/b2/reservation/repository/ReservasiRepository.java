package com.b2.reservation.repository;

import com.b2.reservation.model.reservasi.Reservasi;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ReservasiRepository extends JpaRepository<Reservasi, Integer> {
    @NonNull
    List<Reservasi> findAll();
    @NonNull
    Optional<Reservasi> findById(@NonNull Integer id);
    List<Reservasi> findAllByEmailUser(String emailUser);
}
