package com.b2.reservation.model.lapangan;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_operasional_lapangan")
@Generated
public class OperasionalLapangan {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer idLapangan;
    private Date tanggalLibur;
}
