package com.b2.reservation.model.reservasi;

import com.b2.reservation.model.kupon.Kupon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran;
    private String buktiTransfer;
    private Integer harga;
    private Integer idLapangan;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuBerakhir;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "_kupon_id")
    private Kupon kupon;
    @OneToMany(mappedBy = "reservasi", cascade = CascadeType.ALL)
    private List<Tambahan> tambahanList;

}
