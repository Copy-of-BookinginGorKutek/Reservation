package com.b2.reservation.model.lapangan;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_lapangan")
@Generated
public class Lapangan {
    private static Integer cost = 50000;
    @Id
    @GeneratedValue
    private Integer id;

    public static Integer getCost(){ return cost; }
    public static void setCost(Integer harga){ cost = harga; }

}
