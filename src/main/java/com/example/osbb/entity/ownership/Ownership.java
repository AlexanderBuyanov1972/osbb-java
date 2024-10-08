package com.example.osbb.entity.ownership;

import com.example.osbb.entity.Record;
import com.example.osbb.enums.TypeOfHeatSupply;
import com.example.osbb.enums.TypeOfRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Enumerated(EnumType.STRING)
    private TypeOfHeatSupply heatSupply;
    //тепловой счётчик
    @Column(name = "heat_meter")
    private String heatMeter;
    @Column(name = "bill")
    private String bill;
    @Column(name = "additional_information")
    private String additionalInformation;

    // one to one -------------------------
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    // one to many ------------------------
    @OneToMany(mappedBy = "ownership", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<Record> records;

}

//    id
//    typeRoom
//    totalArea
//    livingArea
//    documentConfirmsRightOwn
//    numberRooms
//    loggia
//    gasSupply
//    gasMeter
//    waterSupply
//    waterMeter
//    sewerage
//    heatSupply
//    heatMeter
//    additionalInformation
//    address
//    records
