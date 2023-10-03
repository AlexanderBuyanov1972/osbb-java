package com.example.osbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "value")
    private Double value;

    @ManyToOne
    @JoinColumn(name = "ownership_id")
    private Ownership ownership;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Share(Double value) {
        this.value = value;
    }
}

//    id
//    value
//    owner
//    ownership
