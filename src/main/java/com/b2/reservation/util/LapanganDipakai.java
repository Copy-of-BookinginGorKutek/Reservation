package com.b2.reservation.util;

import com.b2.reservation.model.lapangan.Lapangan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
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

    public double getHours(){
        Duration duration = Duration.between(waktuMulai, waktuSelesai);
        return (duration.getSeconds()+1)/3600.0;
    }
}
