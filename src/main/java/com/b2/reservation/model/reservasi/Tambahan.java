package com.b2.reservation.model.reservasi;

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
@Table(name = "_tambahan")
public class Tambahan {
    @Id
    @GeneratedValue
    private Integer tambahanId;
    @ManyToOne
    @JoinColumn(name = "_reservasi_id")
    private Reservasi reservasi;
    @Enumerated(EnumType.STRING)
    private TambahanCategory category;
    private Integer quantity;
}
