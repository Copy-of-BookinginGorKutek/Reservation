package com.b2.reservation.model.lapangan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_opersional_lapangan")
public class OperasionalLapangan {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer idLapangan;
    private Date tanggalLibur;
}
