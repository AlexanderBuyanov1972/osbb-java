package com.example.osbb.controller.account;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.service.pdf.IPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiConstants.PRINT)
public class PrintController {
    @Autowired
    IPdfService service;
    @Autowired
    private HelpMethodsForController response;

    // print questionnaire result --------------------------------
    @GetMapping(ApiConstants.QUESTIONNAIRES + ApiConstants.RESULT + ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> printResultQuestionnaire(@PathVariable String title) {
        return response.returnResponse(service.printResultQuestionnaire(title));
    }
    // debt --------------
    // печатать одну квитанцию по конкретному помещению за последний месяц
    @PostMapping(ApiConstants.PAYMENT + ApiConstants.DEBT)
    public ResponseEntity<?> printPdfDebtByApartment(@RequestBody InvoiceNotification invoiceNotification) {
        return response.returnResponse(service.printPdfDebtByApartment(invoiceNotification));
    }

    // печатать квитанции для всех помещений за послений месяц
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.ALL)
    public ResponseEntity<?> printListPdfDebtAllApartment() {
        return response.returnResponse(service.printListPdfDebtAllApartment());
    }

    // печатать квитанции для всех помещений за послений месяц в одном файле (для разноски по квартирам)
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.ALL_IN_ONE)
    public ResponseEntity<?> printAllToOnePdfDebtAllApartment() {
        return response.returnResponse(service.printAllTInOnePdfDebtAllApartment());
    }

    // debt details --------------------
    // печатать одну детализированную распечатку долга по конкретной квартире за всё время
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.DETAILS + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> printPdfDebtDetailsByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.printPdfDebtDetailsByApartment(apartment));
    }

    // печатать детализированную распечатку долга за всё время для всех квартир на разных файлах
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.DETAILS)
    public ResponseEntity<?> printPdfDebtDetailsAllApartment() {
        return response.returnResponse(service.printPdfDebtDetailsAllApartment());
    }
    // balance -------------------
    // печатать баланса
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.BALANCE)
    public ResponseEntity<?> printPdfBalanceHouse() {
        return response.returnResponse(service.printPdfBalanceHouse());
    }


}
