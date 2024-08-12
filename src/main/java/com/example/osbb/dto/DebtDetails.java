package com.example.osbb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DebtDetails {
    private HeaderDebt header;
    private List<BodyDebt> listBody;

}


