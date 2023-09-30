package com.example.osbb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @JoinColumn(name = "create_at")
    private LocalDateTime createAt;

    @JoinColumn(name = "update_at")
    private LocalDateTime updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ownership_id")
    private Ownership ownership;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
