package com.b2.reservation.repository;

import com.b2.reservation.model.lapangan.Lapangan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LapanganRepository extends JpaRepository<Lapangan, Integer>{
    @NonNull
    List<Lapangan> findAll();
    @NonNull
    Optional<Lapangan> findById(@NonNull Integer id);


}
