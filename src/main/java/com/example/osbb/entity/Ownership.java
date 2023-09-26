package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "ownerships")
public class Ownership {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "type_room", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfRoom typeRoom;

    @Column(name = "total_area")
    private double totalArea;

    @Column(name = "living_area")
    private double livingArea;

    @Column(name = "document_confirms_right_own")
    private String documentConfirmsRightOwn;

    @Column(name = "number_rooms")
    private int numberRooms;

   @Column(name = "loggia", nullable = false)
    private boolean loggia;

    // -----------  one to one -------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ownership_id", referencedColumnName = "id")
    private Owner owner;

}