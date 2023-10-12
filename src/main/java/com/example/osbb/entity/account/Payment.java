package com.example.osbb.entity.account;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "personal_account")
    private String personalAccount;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "summa")
    private Double summa;

}
