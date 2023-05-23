package com.b2.reservation.util;


import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.model.reservasi.Tambahan;
import com.b2.reservation.model.reservasi.TambahanCategory;
import com.b2.reservation.repository.TambahanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class TambahanUtilsTest {
    @Mock
    private TambahanRepository tambahanRepository;
    @InjectMocks
    private TambahanUtils tambahanUtils;
    private Map<String, Integer> tambahanQuantity;
    private Reservasi reservasi;

    private Tambahan tambahan;
    private List<Tambahan> tambahanList;

    @BeforeEach
    void setup(){
        this.tambahanQuantity = new HashMap<>();
        this.tambahanQuantity.put("AIR_MINERAL", 1);
        this.tambahanQuantity.put("SHUTTLECOCK", 1);
        this.tambahanQuantity.put("RAKET", 1);

        this.reservasi = Reservasi.builder()
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

        this.tambahan = Tambahan.builder().build();

        Tambahan raket = Tambahan.builder()
                .tambahanId(1)
                .quantity(1)
                .reservasi(this.reservasi)
                .quantity(1)
                .category(TambahanCategory.RAKET)
                .build();
        Tambahan air = Tambahan.builder()
                .tambahanId(1)
                .quantity(1)
                .reservasi(this.reservasi)
                .quantity(1)
                .category(TambahanCategory.AIR_MINERAL)
                .build();
        Tambahan shuttleCock = Tambahan.builder()
                .tambahanId(1)
                .quantity(1)
                .reservasi(this.reservasi)
                .quantity(1)
                .category(TambahanCategory.SHUTTLECOCK)
                .build();
        this.tambahanList = new ArrayList<>();
        this.tambahanList.add(raket);
        this.tambahanList.add(air);
        this.tambahanList.add(shuttleCock);
    }

    @Test
    void testCreateTambahanForReservasi(){
        when(tambahanRepository.save(any())).thenReturn(this.tambahan);
        tambahanUtils.createTambahanForReservasi(this.reservasi, this.tambahanQuantity);
        verify(tambahanRepository, times(3)).save(any());
    }

    @Test
    void testCalculateTambahanCost(){
        when(tambahanRepository.findByReservasi(any())).thenReturn(tambahanList);
        Integer cost = tambahanUtils.calculateTambahanCost(this.reservasi);
        assertEquals(25000, cost);
        verify(tambahanRepository, times(1)).findByReservasi(any());
    }
}
