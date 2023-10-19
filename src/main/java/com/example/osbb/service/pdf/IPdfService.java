package com.example.osbb.service.pdf;

import com.example.osbb.dto.response.InvoiceNotification;

import java.util.List;

public interface IPdfService {
    public Object printPdfDebtByApartment(InvoiceNotification invoiceNotification);
    public Object printListPdfDebtAllApartment();
}
