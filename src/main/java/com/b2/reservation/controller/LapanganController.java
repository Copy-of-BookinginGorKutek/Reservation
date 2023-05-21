package com.b2.reservation.controller;

import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.request.OperasionalLapanganRequest;
import com.b2.reservation.service.LapanganService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/gor")
@RequiredArgsConstructor
@CrossOrigin
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

    @GetMapping("/closed-lapangan")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<OperasionalLapangan>> findClosedLapangan() {
        List<OperasionalLapangan> response = lapanganService.getAllClosedLapangan();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/closed-lapangan/by-date/{dateAsString}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<OperasionalLapangan>> findClosedLapanganByDate(@PathVariable String dateAsString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateAsString);
        System.out.println(date);
        List<OperasionalLapangan> response = lapanganService.getAllClosedLapanganByDate(date);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/closed-lapangan/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteOperasionalLapangan(@PathVariable Integer id){
        lapanganService.deleteOperasionalLapangan(id);
        return ResponseEntity.ok(String.format("Deleted operasional lapangan with ID " + id));
    }
}
