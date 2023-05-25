package com.b2.reservation.util;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.Tambahan;
import com.b2.reservation.model.reservasi.TambahanCategory;
import com.b2.reservation.repository.TambahanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TambahanUtils {
    private final TambahanRepository tambahanRepository;

    public void createTambahan(Reservasi reservasi, TambahanCategory tambahanCategory, Integer quantity){
        if(quantity == 0)
            return;
        Tambahan tambahan =  Tambahan.builder()
                .reservasi(reservasi)
                .category(tambahanCategory)
                .quantity(quantity)
                .build();
        tambahanRepository.save(tambahan);
    }

    public Integer calculateTambahanCost(Reservasi reservasi){
        List<Tambahan> tambahanList = tambahanRepository.findByReservasi(reservasi);
        int cost = 0;
        for(Tambahan tambahan:tambahanList){
            cost += tambahan.getCategory().getPrice() * tambahan.getQuantity();
        }
        return cost;
    }


}