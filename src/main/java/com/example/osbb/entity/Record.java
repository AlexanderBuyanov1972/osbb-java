package com.example.osbb.entity;

import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
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

    // many to one --------------------------

    @ManyToOne
    @JoinColumn(name = "ownership_id")
    private Ownership ownership;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

}

//    id
//    createAt
//    updateAt
//    ownership
//    owner
