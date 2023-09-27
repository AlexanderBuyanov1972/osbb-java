package com.example.osbb.dto;

import com.example.osbb.entity.Owner;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoListRoomsAndOwner {
    private List<Room> rooms;
    private Owner owner;

}
