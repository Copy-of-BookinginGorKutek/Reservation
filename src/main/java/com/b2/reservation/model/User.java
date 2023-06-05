package com.b2.reservation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class User {
    private Integer id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String role;
    private boolean active;
}
