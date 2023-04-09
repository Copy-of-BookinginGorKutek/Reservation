package com.b2.reservation.request;

import com.b2.reservation.model.reservasi.StatusPembayaran;
import com.b2.reservation.model.reservasi.Tambahan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservasiRequest {
    private String emailUser;
    private StatusPembayaran statusPembayaran;
    private String buktiTransfer;
    private List<Tambahan> tambahanList;
}
