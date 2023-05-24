package com.b2.reservation.request;

import com.b2.reservation.model.reservasi.StatusPembayaran;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class ReservasiRequest {
    private String emailUser;
    private StatusPembayaran statusPembayaran;
    private String buktiTransfer;
    private String waktuMulai;
    private String waktuBerakhir;
    private Map<String, Integer> tambahanQuantity;
    private Integer kuponId;
}
