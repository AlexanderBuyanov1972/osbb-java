package com.example.osbb.dto.pojo;

import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoRoomAndListClients {
    private Room room;
    private List<Client> clients;

 }
