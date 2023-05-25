package com.b2.reservation.util;

import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TambahanServiceTest {
    @Spy
    @InjectMocks
    private TambahanService tambahanService;
    @Mock
    private TambahanUtils tambahanUtils;
    private Reservasi reservasi;
    private HashMap<String, Integer> tambahanQuantity;
    @BeforeEach
    void setup(){
        reservasi = Reservasi.builder()
                .id(1)
                .idLapangan(1)
                .emailUser("user@test.com")
                .buktiTransfer(null)
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .harga(100000)
                .kuponId(1)
                .waktuMulai(LocalDateTime.of(2020,11,11,5,0,0))
                .waktuBerakhir(LocalDateTime.of(2020, 11, 11, 6, 0, 0))
                .build();
        tambahanQuantity = new HashMap<>();
        tambahanQuantity.put("AIR_MINERAL", 1);
        tambahanQuantity.put("SHUTTLECOCK", 1);
        tambahanQuantity.put("RAKET", 1);
    }

    @Test
    void testCreateTambahanForReservasi(){
        tambahanService.createTambahanForReservasi(reservasi, tambahanQuantity);
    }
}
