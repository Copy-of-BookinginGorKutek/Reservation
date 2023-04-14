package com.b2.reservation.model.reservasi;

import com.b2.reservation.model.lapangan.Lapangan;
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
    @ManyToOne
    @JoinColumn(name = "_lapangan_id")
    private Lapangan lapangan;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuBerakhir;


    @OneToMany(mappedBy = "reservasi", cascade = CascadeType.ALL)
    private List<Tambahan> tambahanList;
}
