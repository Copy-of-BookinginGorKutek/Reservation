package com.b2.reservation.model.kupon;

import com.b2.reservation.model.reservasi.Reservasi;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_kupon")
@Generated
public class Kupon {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer percentageDiscounted;
}
