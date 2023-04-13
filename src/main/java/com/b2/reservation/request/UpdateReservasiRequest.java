package com.b2.reservation.request;

import com.b2.reservation.model.reservasi.StatusPembayaran;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservasiRequest {
    private String emailUser;
    private StatusPembayaran statusPembayaran;
    private String buktiTransfer;
}
