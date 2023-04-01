package com.b2.reservation.model.lapangan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_lapangan_reservasi")
public class LapanganReservasi {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer idLapangan;
    private Integer idReservasi;
    private LocalTime jam_mulai;
    private LocalTime jam_berakhir;

}
