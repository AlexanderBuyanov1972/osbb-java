package com.example.osbb.entity;

import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@EqualsAndHashCode
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

}

//    id
//    value
//    owner
//    ownership
