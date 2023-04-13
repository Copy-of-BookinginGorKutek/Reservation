package com.b2.reservation.request;

import com.b2.reservation.model.reservasi.StatusPembayaran;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservasiRequest {
    private String emailUser;
    private StatusPembayaran statusPembayaran;
    private String buktiTransfer;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuBerakhir;
    private Map<String, Integer> tambahanQuantity;
}
