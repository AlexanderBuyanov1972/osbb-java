package com.example.osbb.security.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.osbb.enums.TypoOfRoles;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "activationLink", nullable = false)
    private String activationLink;
    @Column(name = "activated")
    private boolean activated;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypoOfRoles role;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}