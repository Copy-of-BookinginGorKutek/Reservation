package com.b2.reservation.controller;

import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.request.KuponRequest;
import com.b2.reservation.service.KuponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gor")
@RequiredArgsConstructor
public class KuponController {
    private final KuponService kuponService;
    @PostMapping("/create-kupon")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Kupon> createKupon(@RequestBody KuponRequest kuponRequest){
        Kupon response = kuponService.create(kuponRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-kupon")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<Kupon>> getAllKupon(){
        List<Kupon> listAllKupon = kuponService.findAll();
        return ResponseEntity.ok(listAllKupon);
    }

    @DeleteMapping("/delete-kupon/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteKuponById(@PathVariable Integer id){
        kuponService.delete(id);
        return ResponseEntity.ok(String.format("Deleted kupon with id %d", id));
    }
}
