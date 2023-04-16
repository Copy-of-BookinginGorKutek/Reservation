package com.b2.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperasionalLapanganRequest {
    private Integer idLapangan;
    private Date tanggalLibur;
}
