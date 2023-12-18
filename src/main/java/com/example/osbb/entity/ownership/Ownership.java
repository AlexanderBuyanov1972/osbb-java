package com.example.osbb.entity.ownership;

import com.example.osbb.entity.Record;
import com.example.osbb.enums.TypeOfHeatSupply;
import com.example.osbb.enums.TypeOfRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

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

    public Ownership() {
    }

    public Ownership(long id, TypeOfRoom typeRoom, double totalArea, double livingArea, String documentConfirmsRightOwn, int numberRooms, boolean loggia, String gasSupply, String gasMeter, String waterSupply, String waterMeter, String sewerage, TypeOfHeatSupply heatSupply, String heatMeter, String bill, String additionalInformation, Address address, List<Record> records) {
        this.id = id;
        this.typeRoom = typeRoom;
        this.totalArea = totalArea;
        this.livingArea = livingArea;
        this.documentConfirmsRightOwn = documentConfirmsRightOwn;
        this.numberRooms = numberRooms;
        this.loggia = loggia;
        this.gasSupply = gasSupply;
        this.gasMeter = gasMeter;
        this.waterSupply = waterSupply;
        this.waterMeter = waterMeter;
        this.sewerage = sewerage;
        this.heatSupply = heatSupply;
        this.heatMeter = heatMeter;
        this.bill = bill;
        this.additionalInformation = additionalInformation;
        this.address = address;
        this.records = records;
    }

    public long getId() {
        return id;
    }

    public TypeOfRoom getTypeRoom() {
        return typeRoom;
    }

    public double getTotalArea() {
        return totalArea;
    }

    public double getLivingArea() {
        return livingArea;
    }

    public String getDocumentConfirmsRightOwn() {
        return documentConfirmsRightOwn;
    }

    public int getNumberRooms() {
        return numberRooms;
    }

    public boolean isLoggia() {
        return loggia;
    }

    public String getGasSupply() {
        return gasSupply;
    }

    public String getGasMeter() {
        return gasMeter;
    }

    public String getWaterSupply() {
        return waterSupply;
    }

    public String getWaterMeter() {
        return waterMeter;
    }

    public String getSewerage() {
        return sewerage;
    }

    public TypeOfHeatSupply getHeatSupply() {
        return heatSupply;
    }

    public String getHeatMeter() {
        return heatMeter;
    }

    public String getBill() {
        return bill;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public Address getAddress() {
        return address;
    }

    public List<Record> getRecords() {
        return records;
    }

    public Ownership setId(long id) {
        this.id = id;
        return this;
    }

    public Ownership setTypeRoom(TypeOfRoom typeRoom) {
        this.typeRoom = typeRoom;
        return this;
    }

    public Ownership setTotalArea(double totalArea) {
        this.totalArea = totalArea;
        return this;
    }

    public Ownership setLivingArea(double livingArea) {
        this.livingArea = livingArea;
        return this;
    }

    public Ownership setDocumentConfirmsRightOwn(String documentConfirmsRightOwn) {
        this.documentConfirmsRightOwn = documentConfirmsRightOwn;
        return this;
    }

    public Ownership setNumberRooms(int numberRooms) {
        this.numberRooms = numberRooms;
        return this;
    }

    public Ownership setLoggia(boolean loggia) {
        this.loggia = loggia;
        return this;
    }

    public Ownership setGasSupply(String gasSupply) {
        this.gasSupply = gasSupply;
        return this;
    }

    public Ownership setGasMeter(String gasMeter) {
        this.gasMeter = gasMeter;
        return this;
    }

    public Ownership setWaterSupply(String waterSupply) {
        this.waterSupply = waterSupply;
        return this;
    }

    public Ownership setWaterMeter(String waterMeter) {
        this.waterMeter = waterMeter;
        return this;
    }

    public Ownership setSewerage(String sewerage) {
        this.sewerage = sewerage;
        return this;
    }

    public Ownership setHeatSupply(TypeOfHeatSupply heatSupply) {
        this.heatSupply = heatSupply;
        return this;
    }

    public Ownership setHeatMeter(String heatMeter) {
        this.heatMeter = heatMeter;
        return this;
    }

    public Ownership setBill(String bill) {
        this.bill = bill;
        return this;
    }

    public Ownership setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public Ownership setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Ownership setRecords(List<Record> records) {
        this.records = records;
        return this;
    }

    @Override
    public String toString() {
        return "Ownership = { id = " + id + ", typeRoom = " + typeRoom + ", totalArea = " + totalArea +
                ", livingArea = " + livingArea + ", documentConfirmsRightOwn = " + documentConfirmsRightOwn +
                ", numberRooms = " + numberRooms + ", loggia = " + loggia + ", gasSupply = " + gasSupply +
                ", gasMeter = " + gasMeter + ", waterSupply='" + waterSupply + ", waterMeter = " + waterMeter +
                ", sewerage = " + sewerage + ", heatSupply = " + heatSupply + ", heatMeter = " + heatMeter +
                ", bill = " + bill + ", additionalInformation = " + additionalInformation +
                ", address = " + address + ", records = " + records + " }";
    }
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
