package com.b2.reservation.util;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.Tambahan;
import com.b2.reservation.model.reservasi.TambahanCategory;
import com.b2.reservation.repository.TambahanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TambahanUtils {
    private final TambahanRepository tambahanRepository;

    public void createTambahanForReservasi(Reservasi reservasi, Map<String, Integer> tambahanQuantity){
        for (Map.Entry<String, Integer> entry : tambahanQuantity.entrySet()) {
            Integer quantity = entry.getValue();
            if (quantity > 0) {
                TambahanCategory tambahanCategory = TambahanCategory.valueOf(entry.getKey());
                Tambahan tambahan = createTambahan(reservasi, tambahanCategory, quantity);
                this.tambahanRepository.save(tambahan);
            }
        }
    }

    public Tambahan createTambahan(Reservasi reservasi, TambahanCategory tambahanCategory, Integer quantity){
        return Tambahan.builder()
                .reservasi(reservasi)
                .category(tambahanCategory)
                .quantity(quantity)
                .build();
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