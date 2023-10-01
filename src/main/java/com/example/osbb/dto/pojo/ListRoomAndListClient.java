package com.example.osbb.dto.pojo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListRoomAndListClient {
    private List<Room> rooms;
    private List<Client> clients;

 }
