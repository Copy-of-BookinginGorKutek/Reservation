package com.b2.reservation.model.kupon;

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
@Table(name = "_kupon")
public class Kupon {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Double percentageDiscounted;
    @OneToMany(mappedBy = "kupon", cascade = CascadeType.ALL)
    private List<Reservasi> reservationsUseCoupon;
}
