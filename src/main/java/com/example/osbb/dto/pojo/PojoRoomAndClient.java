package com.example.osbb.dto.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoRoomAndClient {
    private Room room;
    private Client client;
}
