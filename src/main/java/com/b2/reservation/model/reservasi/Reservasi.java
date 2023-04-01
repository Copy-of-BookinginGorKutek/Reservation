package com.b2.reservation.model.reservasi;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_reservasi")
public class Reservasi {
    @Id
    @GeneratedValue
    private Integer id;
    private String emailUser;
    private Integer idStatusPembayaran;
    private String buktiTransfer;
    @OneToMany(mappedBy = "reservasi", cascade = CascadeType.ALL)
    private List<Tambahan> tambahanList;
}
