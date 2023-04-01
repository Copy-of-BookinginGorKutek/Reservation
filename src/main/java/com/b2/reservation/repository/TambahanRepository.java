package com.b2.reservation.repository;

import com.b2.reservation.model.reservasi.Tambahan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TambahanRepository extends JpaRepository<Tambahan, Integer> {
    @NonNull
    List<Tambahan> findAll();

    @NonNull
    Optional<Tambahan> findById(@NonNull Integer id);
}
