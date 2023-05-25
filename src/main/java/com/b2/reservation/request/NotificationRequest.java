package com.b2.reservation.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class NotificationRequest {
    private String emailUser;
    private Integer statusId;
    private String message;
}
