package com.example.osbb.dto;

import com.example.osbb.entity.Owner;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoRoomAndListOwners {
    private List<Owner> owners;
    private Room room;
}
