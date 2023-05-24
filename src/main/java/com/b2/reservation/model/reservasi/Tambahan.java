package com.b2.reservation.model.reservasi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_tambahan")
@Generated
public class Tambahan {
    @Id
    @GeneratedValue
    private Integer tambahanId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "_reservasi_id")
    private Reservasi reservasi;
    @Enumerated(EnumType.STRING)
    private TambahanCategory category;
    private Integer quantity;
}
