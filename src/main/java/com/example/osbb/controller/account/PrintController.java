package com.example.osbb.controller.account;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.dto.InvoiceNotification;
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

    // debt --------------
    // печатать одну квитанцию по конкретному помещению за последний месяц
    @PostMapping(ApiConstants.DEBT + ApiConstants.PRINT)
    public ResponseEntity<?> printPdfDebtByApartment(@RequestBody InvoiceNotification invoiceNotification) {
        return response.returnResponse(service.printPdfDebtByApartment(invoiceNotification));
    }

    // печатать квитанции для всех помещений за послений месяц
    @GetMapping(ApiConstants.DEBT + ApiConstants.PRINT + ApiConstants.ALL)
    public ResponseEntity<?> printListPdfDebtAllApartment() {
        return response.returnResponse(service.printListPdfDebtAllApartment());
    }

    // печатать квитанции для всех помещений за послений месяц в одном файле (для разноски по квартирам)
    @GetMapping(ApiConstants.DEBT + ApiConstants.PRINT + ApiConstants.ALL_IN_ONE)
    public ResponseEntity<?> printAllToOnePdfDebtAllApartment() {
        return response.returnResponse(service.printAllTInOnePdfDebtAllApartment());
    }

    // debt details --------------------
    // печатать одну детализированную распечатку долга по конкретной квартире за всё время
    @GetMapping(ApiConstants.DEBT + ApiConstants.PRINT + ApiConstants.DETAILS + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> printPdfDebtDetailsByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.printPdfDebtDetailsByApartment(apartment));
    }

    // печатать детализированную распечатку долга за всё время для всех квартир на разных файлах
    @GetMapping(ApiConstants.DEBT + ApiConstants.PRINT + ApiConstants.DETAILS)
    public ResponseEntity<?> printPdfDebtDetailsAllApartment() {
        return response.returnResponse(service.printPdfDebtDetailsAllApartment());
    }
    // balance -------------------
    // печатать баланса
    @GetMapping(ApiConstants.DEBT + ApiConstants.PRINT + ApiConstants.BALANCE)
    public ResponseEntity<?> printPdfBalanceHouse() {
        return response.returnResponse(service.printPdfBalanceHouse());
    }


}
