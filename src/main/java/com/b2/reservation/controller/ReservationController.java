package com.b2.reservation.controller;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.service.ReservasiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<Reservasi>> getAllReservationByUser() {
        String email = getCurrentUserEmail();
        List<Reservasi> response= reservasiService.findAllByEmailUser(email);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/stat-update/{id}")
    public ResponseEntity<Reservasi> updateReservation(@PathVariable Integer id,
                                                       @RequestBody ReservasiRequest reservationRequest) {
        Reservasi response = reservasiService.update(id, reservationRequest);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        reservasiService.delete(id);
        return ResponseEntity.ok(String.format("Deleted reservation with id %d", id));
    }

    private static String getCurrentUserEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }


}
