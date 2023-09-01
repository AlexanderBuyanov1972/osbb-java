package com.example.osbb.dto;

import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneOwnershipAndListOwner {
    private Ownership ownership;
    private List<Owner> owners;
}

