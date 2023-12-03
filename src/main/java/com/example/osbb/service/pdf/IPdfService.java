package com.example.osbb.service.pdf;

import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.queries.ApartmentHeatSupply;

import java.util.List;
import java.util.Map;

public interface IPdfService {
    // debt ---------------------------
    public Object printPdfDebtByApartment(InvoiceNotification invoiceNotification);

    public Object printListPdfDebtAllApartment();

    public Object printAllTInOnePdfDebtAllApartment();

    // debt details -----------------------
    public Object printPdfDebtDetailsByApartment(String apartment);

    public Object printPdfDebtDetailsAllApartment();

    // balance house -------------
    public Object printPdfBalanceHouse();

    // result questionnaire -------------
    public Object printResultQuestionnaire(String title);

    public Object fillPdfNewBillForPayServiceOSBB();

    // возвращает отсортированный лист номер квартиры - тип отопления
    public void printQueryListHeatSupplyForApartment(Map<String, List<ApartmentHeatSupply>> map);

    public void printQueryReport_2023_11();
}
