package com.example.osbb.dto;

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

    public Room(Ownership one){
        this.id = one.getId();
        this.typeRoom = one.getTypeRoom();
        this.totalArea = one.getTotalArea();
        this.livingArea = one.getLivingArea();
        this.documentConfirmsRightOwn = one.getDocumentConfirmsRightOwn();
        this.numberRooms = one.getNumberRooms();
        this.loggia = one.isLoggia();
    }

}
