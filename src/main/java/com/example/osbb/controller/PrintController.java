package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.dto.Debt;
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

    // print survey result --------------------------------
    @GetMapping(ApiConstants.SURVEYS + ApiConstants.RESULT + ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> printResultSurvey(@PathVariable String title) {
        return response.returnResponse(service.printResultSurvey(title));
    }
    // debt --------------
    // печатать одну квитанцию по конкретному помещению за последний месяц
    @PostMapping(ApiConstants.PAYMENT + ApiConstants.DEBT)
    public ResponseEntity<?> printDebt(@RequestBody Debt debt) {
        return response.returnResponse(service.printDebt(debt));
    }

    // печатать квитанции для всех помещений за послений месяц
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.ALL)
    public ResponseEntity<?> printAllDebt() {
        return response.returnResponse(service.printAllDebt());
    }

    // печатать квитанции для всех помещений за послений месяц в одном файле (для разноски по квартирам)
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.ALL_IN_ONE)
    public ResponseEntity<?> printAllToOnePdfDebtAllApartment() {
        return response.returnResponse(service.printAllInOneDebtAllApartment());
    }

    // debt details --------------------
    // печатать одну детализированную распечатку долга по конкретной квартире за всё время
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.DETAILS + ApiConstants.PARAM_ID)
    public ResponseEntity<?> printDebtDetails(@PathVariable Long id) {
        return response.returnResponse(service.printDebtDetails(id));
    }

    // печатать детализированную распечатку долга за всё время для всех квартир на разных файлах
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.DETAILS)
    public ResponseEntity<?> printAllDebtDetails() {
        return response.returnResponse(service.printAllDebtDetails());
    }
    // balance -------------------
    // печатать баланса
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.DEBT + ApiConstants.BALANCE)
    public ResponseEntity<?> printBalanceHouse() {
        return response.returnResponse(service.printBalanceHouse());
    }
    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    @GetMapping(ApiConstants.PAYMENT + ApiConstants.NEW_BILL)
    public ResponseEntity<?> printNewBillForPayServiceOSBB() {
        return response.returnResponse(service.printNewBillForPayServiceOSBB());
    }


}
