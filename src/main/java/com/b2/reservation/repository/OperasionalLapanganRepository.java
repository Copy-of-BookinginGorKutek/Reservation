package com.b2.reservation.repository;

import com.b2.reservation.model.lapangan.OperasionalLapangan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Date;
@Repository
public interface OperasionalLapanganRepository extends JpaRepository<OperasionalLapangan, Integer>{
    @NonNull
    List<OperasionalLapangan> findAll();
    @NonNull
    Optional<OperasionalLapangan> findById(@NonNull Integer id);
    void deleteOperasionalLapanganById(@NonNull Integer id);
}
