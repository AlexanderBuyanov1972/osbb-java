package com.example.osbb.dto.pojo;

import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    private long id;
    private String bill;
    private TypeOfRoom typeRoom;
    private double totalArea;
    private double livingArea;
    private String documentConfirmsRightOwn;
    private int numberRooms;
    private boolean loggia;
    private String apartment;
    private String entrance;
    private String floor;
    // ----------------------
    private String gasSupply;
    private String gasMeter;
    private String waterSupply;
    private String waterMeter;
    private String sewerage;
    private String heatSupply;
    private String heatMeter;
    private Double share;

    public Room(Ownership one, Double share) {
        this.id = one.getId();
        this.bill = one.getBill();
        this.typeRoom = one.getTypeRoom();
        this.totalArea = one.getTotalArea();
        this.livingArea = one.getLivingArea();
        this.documentConfirmsRightOwn = one.getDocumentConfirmsRightOwn();
        this.numberRooms = one.getNumberRooms();
        this.loggia = one.isLoggia();
        this.apartment = one.getAddress().getApartment();
        this.entrance = one.getAddress().getEntrance();
        this.floor = one.getAddress().getFloor();
        //----------------------------
        this.gasSupply = one.getGasSupply();
        this.gasMeter = one.getGasMeter();
        this.waterSupply = one.getWaterSupply();
        this.waterMeter = one.getWaterMeter();
        this.sewerage = one.getSewerage();
        this.heatSupply = one.getHeatSupply().toString();
        this.heatMeter = one.getHeatMeter();
        this.share = share;
    }

}
