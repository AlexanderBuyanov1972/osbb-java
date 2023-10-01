package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    // газоснабжение
    @Column(name = "gas_supply")
    private String gasSupply;
    // газовый счётчик
    @Column(name = "gas_meter")
    private String gasMeter;
    //водоснабжение
    @Column(name = "water_supply")
    private String waterSupply;
    //водяной счётчик
    @Column(name = "water_meter")
    private String waterMeter;
    //канализация
    @Column(name = "sewerage")
    private String sewerage;
    //теплоснабжение
    @Column(name = "heat_supply")
    private String heatSupply;
    //тепловой счётчик
    @Column(name = "heat_meter")
    private String heatMeter;


    // -----------  one to one -------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(mappedBy = "ownership")
    @JsonIgnore
    private Record record;

}