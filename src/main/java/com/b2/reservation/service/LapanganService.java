package com.b2.reservation.service;

import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.request.OperasionalLapanganRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface LapanganService {
    Lapangan create();
    OperasionalLapangan createCloseDate(OperasionalLapanganRequest request);
    List<OperasionalLapangan> getAllClosedLapangan();
    List<OperasionalLapangan> getAllClosedLapanganByDate(Date date);
    void deleteOperasionalLapangan(Integer id);
}
