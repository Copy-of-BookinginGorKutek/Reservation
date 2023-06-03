package com.b2.reservation.util;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.model.reservasi.Tambahan;
import com.b2.reservation.model.reservasi.TambahanCategory;
import com.b2.reservation.repository.ReservasiRepository;
import com.b2.reservation.repository.TambahanRepository;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class TambahanServiceTest {
    @Autowired
    private TambahanService tambahanService;
    @Autowired
    private TambahanRepository tambahanRepository;
    @Autowired
    private ReservasiRepository reservasiRepository;
    private Reservasi reservasi;
    private HashMap<String, Integer> tambahanQuantity;
    @BeforeEach
    void setup(){
        reservasi = Reservasi.builder()
                .idLapangan(1)
                .emailUser("user@test.com")
                .buktiTransfer(null)
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .harga(100000)
                .kuponId(1)
                .waktuMulai(LocalDateTime.of(2020,11,11,5,0,0))
                .waktuBerakhir(LocalDateTime.of(2020, 11, 11, 6, 0, 0))
                .build();
        reservasi = reservasiRepository.save(reservasi);
        tambahanQuantity = new HashMap<>();
        tambahanQuantity.put("AIR_MINERAL", 1);
        tambahanQuantity.put("SHUTTLECOCK", 2);
        tambahanQuantity.put("RAKET", 3);
    }

    @Test
    void testCreateTambahanForReservasi(){
        tambahanService.createTambahanForReservasi(reservasi, tambahanQuantity);
        List<Tambahan> tambahanList = tambahanRepository.findByReservasi(reservasi);
        Assertions.assertEquals(3, tambahanList.size());
        for(Tambahan tambahan:tambahanList){
            if(tambahan.getCategory() == TambahanCategory.AIR_MINERAL)
                assertEquals(1, tambahan.getQuantity());
            else if(tambahan.getCategory() == TambahanCategory.SHUTTLECOCK)
                assertEquals(2, tambahan.getQuantity());
            else
                assertEquals(3, tambahan.getQuantity());
        }

    }
}
