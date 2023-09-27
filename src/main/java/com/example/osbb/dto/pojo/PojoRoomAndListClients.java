package com.example.osbb.dto.pojo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoRoomAndListClients {
    private Room room;
    private List<Client> clients;

}
