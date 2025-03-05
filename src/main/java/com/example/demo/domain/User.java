package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class User {
    private String id;
    private String email;
    private String password;
    private String fullName;
    private LocalDate dateBirth;
    private Rol rol;
    private UserStatus status;
}
