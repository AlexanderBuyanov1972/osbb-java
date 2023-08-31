package com.example.osbb.dto.registry;

import com.example.osbb.entity.Ownership;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnershipAndListFullNameOwner {
    private int id;
    private Ownership ownership;
    private List<FullNameOwner> listFullNameOwner;
}
