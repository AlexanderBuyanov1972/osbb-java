package com.example.osbb.controller.account;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.dto.response.InvoiceNotification;
import com.example.osbb.service.pdf.IPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiConstants.PAYMENT)
public class PrintController {
    @Autowired
    IPdfService service;
    @Autowired
    private HelpMethodsForController response;

    @PostMapping(ApiConstants.DEBT + ApiConstants.PRINT)
    public ResponseEntity<?> printPdfDebtByApartment(@RequestBody InvoiceNotification invoiceNotification) {
        return response.returnResponse(service.printPdfDebtByApartment(invoiceNotification));
    }
    @GetMapping(ApiConstants.DEBT + ApiConstants.PRINT + ApiConstants.ALL)
    public ResponseEntity<?> printListPdfDebtAllApartment() {
        return response.returnResponse(service.printListPdfDebtAllApartment());
    }
}
