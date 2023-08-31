package com.example.osbb.dto.registry;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullNameOwner {
    private Long ownerId;
    private String fullNameOwner;
}
