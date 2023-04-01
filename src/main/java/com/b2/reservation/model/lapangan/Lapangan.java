package com.b2.reservation.model.lapangan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_lapangan")
public class Lapangan {
    @Id
    @GeneratedValue
    private Integer id;

}
