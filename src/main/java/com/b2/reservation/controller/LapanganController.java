package com.b2.reservation.controller;

import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.request.OperasionalLapanganRequest;
import com.b2.reservation.service.LapanganService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gor")
@RequiredArgsConstructor
public class LapanganController {
    private final LapanganService lapanganService;

    @PostMapping("/create-lapangan")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Lapangan> createLapangan() {
        Lapangan response = lapanganService.create();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/close-lapangan")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<OperasionalLapangan> createCloseDate(@RequestBody OperasionalLapanganRequest request) {
        OperasionalLapangan response = lapanganService.createCloseDate(request);
        return ResponseEntity.ok(response);
    }

}
