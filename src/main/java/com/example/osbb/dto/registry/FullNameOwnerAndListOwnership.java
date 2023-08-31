package com.example.osbb.dto.registry;

import com.example.osbb.entity.Ownership;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullNameOwnerAndListOwnership {
    private int id;
    private FullNameOwner fullNameOwner;
    private List<Ownership> listOwnership;

}
