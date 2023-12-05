package com.example.osbb.service.pdf;

import com.example.osbb.dto.Debt;

public interface IPdfService {
    // debt ---------------------------
    public Object printDebt(Debt debt);

    public Object printAllDebt();

    public Object printAllInOneDebtAllApartment();

    // debt details -----------------------
    public Object printDebtDetails(Long id);

    public Object printAllDebtDetails();

    // balance house -------------
    public Object printBalanceHouse();

    // result questionnaire -------------
    public Object printResultSurvey(String title);

    public Object printNewBillForPayServiceOSBB();

    public void printQueryReport_2023_11();
}
