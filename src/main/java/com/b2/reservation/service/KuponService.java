package com.b2.reservation.service;

import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.request.KuponRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface KuponService {
    Kupon create(KuponRequest kuponRequest);
    List<Kupon> findAll();
    void delete(Integer id);
}
