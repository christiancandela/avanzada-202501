package com.example.demo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document("usuarios")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String email;
    private String password;
    private String fullName;
    private LocalDate dateBirth;
    private Rol rol;
    private UserStatus status;
}
