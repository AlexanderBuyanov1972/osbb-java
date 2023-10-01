package com.example.osbb.dto.pojo;

import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    private long id;
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

    public Room(Ownership one) {
        this.id = one.getId();
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
        this.heatSupply = one.getHeatSupply();
        this.heatMeter = one.getHeatMeter();
    }

}
