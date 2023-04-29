package com.b2.reservation.model.reservasi;

import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.model.lapangan.Lapangan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ManyToOne
    @JoinColumn(name = "_kupon_id")
    private Kupon kupon;
    @OneToMany(mappedBy = "reservasi", cascade = CascadeType.ALL)
    private List<Tambahan> tambahanList;

    public static String toJson(List<Reservasi> listReservasi) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, Object> map = new HashMap<>();

        for (int i = 0; i < listReservasi.size(); i++) {
            Reservasi reservasi = listReservasi.get(i);
            Map<String, Object> json = mapper.convertValue(reservasi, Map.class);
            mapper.convertValue(reservasi, Map.class);
            map.put(reservasi.getId(), json);
        }

        return mapper.writeValueAsString(map);
    }
}
