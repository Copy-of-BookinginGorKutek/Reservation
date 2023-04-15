package com.b2.reservation.model.lapangan;

import com.b2.reservation.model.reservasi.Reservasi;
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
@Table(name = "_lapangan")
public class Lapangan {
    private static Integer cost = 50000;
    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(mappedBy = "lapangan", cascade = CascadeType.ALL)
    private List<Reservasi> reservasiList;

    public static Integer getCost(){ return cost; }
    public static void setCost(Integer harga){ cost = harga; }

}
