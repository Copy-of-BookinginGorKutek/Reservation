package com.b2.reservation.controller;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.request.PaymentProofRequest;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.service.ReservasiService;
import com.b2.reservation.util.LapanganDipakai;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservasiService reservasiService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Reservasi> createReservation(@RequestBody ReservasiRequest reservationRequest) {
        Reservasi response = reservasiService.create(reservationRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<Reservasi>> getAllReservation() {
        List<Reservasi> response= reservasiService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Reservasi> getReservationById(@PathVariable Integer id){
        Reservasi reservasi = reservasiService.findById(id);
        return ResponseEntity.ok(reservasi);
    }

    @GetMapping("/get-self")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Reservasi>> getAllReservationByUser(@RequestParam(value = "emailUser") String emailUser) {
        List<Reservasi> response= reservasiService.findAllByEmailUser(emailUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/stat-update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Reservasi> updateReservation(@PathVariable Integer id,
                                                       @RequestBody ReservasiRequest request) {
        Reservasi response = reservasiService.updateStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/bukti-bayar/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Reservasi> putProofOfPayment(@PathVariable Integer id,
                                                       @RequestBody PaymentProofRequest buktiBayar) {
        Reservasi response = reservasiService.addPaymentProof(id, buktiBayar.getUrl());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        reservasiService.delete(id);
        return ResponseEntity.ok(String.format("Deleted reservation with id %d", id));
    }

    @GetMapping("/get-lapangan-dipakai")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<LapanganDipakai>> getLapanganDipakai() {
        List<LapanganDipakai> response = reservasiService.createLapanganDipakaiList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-reservasi-by-date/{date}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<Reservasi>> getReservasiByDate(@PathVariable String date){
        List<Reservasi> response = null;
        try {
            response = reservasiService.findReservasiByDate(date);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
