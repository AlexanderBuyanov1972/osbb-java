package com.example.osbb.dto.pojo;

import com.example.osbb.dto.pojo.Client;
import com.example.osbb.dto.pojo.Room;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoClientAndListRooms {
    private Client client;
    private List<Room> rooms;


}
