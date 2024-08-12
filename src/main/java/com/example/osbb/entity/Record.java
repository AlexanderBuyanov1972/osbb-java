package com.example.osbb.entity;

import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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

    @JoinColumn(name = "share")
    private Double share;


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
