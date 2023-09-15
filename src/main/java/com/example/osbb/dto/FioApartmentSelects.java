package com.example.osbb.dto;

import com.example.osbb.entity.Select;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FioApartmentSelects {
    private String fio;
    private String apartment;
    private List<Select> selectList;
}
