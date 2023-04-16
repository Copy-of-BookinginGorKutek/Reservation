package com.b2.reservation.model.lapangan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_operasional_lapangan")
public class OperasionalLapangan {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer idLapangan;
    private Date tanggalLibur;
}
