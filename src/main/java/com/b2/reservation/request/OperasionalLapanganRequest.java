package com.b2.reservation.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date tanggalLibur;
}
