package com.example.osbb.service.pdf;

import com.example.osbb.dto.InvoiceNotification;

public interface IPdfService {
    // debt ---------------------------
    public Object printPdfDebtByApartment(InvoiceNotification invoiceNotification);

    public Object printListPdfDebtAllApartment();

    public Object printAllTInOnePdfDebtAllApartment();

    // debt details -----------------------
    public Object printPdfDebtDetailsByApartment(String apartment);

    public Object printPdfDebtDetailsAllApartment();
}
