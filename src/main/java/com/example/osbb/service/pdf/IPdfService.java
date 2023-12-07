package com.example.osbb.service.pdf;


import com.example.osbb.dto.ApartmentBillFullNamePhoneNumber;
import com.example.osbb.dto.DebtDetails;

import java.util.List;

public interface IPdfService {
    // debt ---------------------------
    public Object printDebt(DebtDetails debt);

    public Object printAllDebt();

    public Object printAllInOneDebtAllApartment();

    // debt details -----------------------
    public Object printDebtDetails(Long id);

    public Object printAllDebtDetails();

     // result survey -------------
    public Object printResultSurvey(String title);

    public void printQueryListApartmentBillFullNamePhoneNumber(List<ApartmentBillFullNamePhoneNumber> list);
}
