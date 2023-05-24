package com.b2.reservation.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class KuponRequest {
    private String name;
    private Integer percentageDiscounted;
}
