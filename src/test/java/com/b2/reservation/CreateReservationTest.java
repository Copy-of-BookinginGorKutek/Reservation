package com.b2.reservation;

import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.model.lapangan.Lapangan;
import com.b2.reservation.model.lapangan.OperasionalLapangan;
import com.b2.reservation.model.reservasi.Reservasi;
import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.model.reservasi.Tambahan;
import com.b2.reservation.repository.*;
import com.b2.reservation.request.ReservasiRequest;
import com.b2.reservation.service.ReservasiServiceImpl;
import com.b2.reservation.util.TambahanService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CreateReservationTest {
    @Autowired
    ReservasiServiceImpl reservasiService;
    @Autowired
    ReservasiRepository reservasiRepository;
    @Autowired
    LapanganRepository lapanganRepository;
    @Autowired
    TambahanService tambahanService;
    @Autowired
    TambahanRepository tambahanRepository;
    @Autowired
    OperasionalLapanganRepository operasionalLapanganRepository;
    @Autowired
    KuponRepository kuponRepository;
    private ReservasiRequest request1;
    private ReservasiRequest request2;
    private LocalDate date1;
    private OperasionalLapangan operasionalLapangan;
    @BeforeEach
    void setup(){
        Lapangan lapangan = Lapangan.builder().build();
        lapanganRepository.save(lapangan);
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        date1 = dateTime.toLocalDate();
        String dateString = date1.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Map<String, Integer> tambahanQty = new HashMap<>();

        tambahanQty.put("AIR_MINERAL", 0);
        tambahanQty.put("RAKET", 1);
        tambahanQty.put("SHUTTLECOCK", 1);

        request1 = ReservasiRequest.builder()
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .waktuMulai(dateString + " 19:00")
                .waktuBerakhir(dateString + " 21:00")
                .tambahanQuantity(tambahanQty)
                .buktiTransfer(null)
                .kuponId(0)
                .build();
        Kupon kupon = Kupon.builder()
                .name("TEST")
                .id(1)
                .percentageDiscounted(50)
                .build();
        kuponRepository.save(kupon);
        request2 = ReservasiRequest.builder()
                .emailUser("test1@email.com")
                .statusPembayaran(StatusPembayaran.MENUNGGU_PEMBAYARAN)
                .waktuMulai(dateString + " 21:00")
                .waktuBerakhir(dateString + " 23:00")
                .tambahanQuantity(tambahanQty)
                .buktiTransfer(null)
                .kuponId(1)
                .build();
        operasionalLapangan = OperasionalLapangan.builder()
                .idLapangan(1)
                .tanggalLibur(Date.from(Instant.now()))
                .build();
    }
    @Test
    void testCreateReservasi(){
        Reservasi reservasi = reservasiService.create(request1);
        assertEquals("test1@email.com", reservasi.getEmailUser());
        assertTrue(reservasi.getWaktuMulai().toLocalDate().isEqual(date1));
        assertEquals(0, reservasi.getKuponId());
        assertEquals(StatusPembayaran.MENUNGGU_PEMBAYARAN, reservasi.getStatusPembayaran());
        List<Tambahan> tambahanList = tambahanRepository.findByReservasi(reservasi);
        assertEquals(2, tambahanList.size());

        operasionalLapanganRepository.save(operasionalLapangan);
        Reservasi reservasi2 = reservasiService.create(request2);
        assertEquals(1, reservasi2.getKuponId());
    }
}
