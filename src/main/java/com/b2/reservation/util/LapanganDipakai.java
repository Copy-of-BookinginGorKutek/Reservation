package com.b2.reservation.util;

import com.b2.reservation.model.lapangan.Lapangan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class LapanganDipakai {
    @Setter
    @Getter
    private Lapangan lapangan;
    @Setter
    @Getter
    private LocalDateTime waktuMulai;
    @Setter
    @Getter
    private LocalDateTime waktuSelesai;
}
