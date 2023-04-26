package com.b2.reservation.repository;

import com.b2.reservation.model.kupon.Kupon;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface KuponRepository extends JpaRepository<Kupon, Integer> {
    @NonNull
    List<Kupon> findAll();

    @NonNull
    Optional<Kupon> findById(@NonNull Integer id);
}
