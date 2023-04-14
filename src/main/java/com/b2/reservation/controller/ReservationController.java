package com.b2.reservation.controller;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.service.ReservasiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservasiService reservasiService;

    @PostMapping("/create")
    public ResponseEntity<Reservasi> createReservation(@RequestBody ReservasiRequest reservationRequest) {
        Reservasi response = reservasiService.create(reservationRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Reservasi>> getAllReservation() {
        List<Reservasi> response= reservasiService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-self")
    public ResponseEntity<List<Reservasi>> getAllReservationByUser(@RequestParam String emailUser) {
        List<Reservasi> response= reservasiService.findAllByEmailUser(emailUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/stat-update/{id}")
    public ResponseEntity<Reservasi> updateReservation(@PathVariable Integer id,
                                                       @RequestBody ReservasiRequest request) {
        Reservasi response = reservasiService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/bukti-bayar/{id}")
    public ResponseEntity<Reservasi> putProofOfPayment(@PathVariable Integer id,
                                                       @RequestBody ReservasiRequest request) {
        Reservasi response = reservasiService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        reservasiService.delete(id);
        return ResponseEntity.ok(String.format("Deleted reservation with id %d", id));
    }

}
