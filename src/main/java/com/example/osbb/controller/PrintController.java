package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.service.pdf.IPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiPaths.PRINT)
public class PrintController {
    @Autowired
    IPdfService service;

    // print survey result --------------------------------
    @GetMapping(ApiPaths.SURVEYS + ApiPaths.RESULT + ApiPaths.PARAM_TITLE)
    public ResponseEntity<?> printResultSurvey(@PathVariable String title) {
        return service.printResultSurvey(title);
    }

    // debt --------------
    // печатать одну квитанцию по конкретному помещению за последний месяц
    @PostMapping(ApiPaths.PAYMENT + ApiPaths.DEBT)
    public ResponseEntity<?> printDebt(@RequestBody DebtDetails debt) {
        return service.printDebt(debt);
    }

    // печатать квитанции для всех помещений за послений месяц
    @GetMapping(ApiPaths.PAYMENT + ApiPaths.DEBT + ApiPaths.ALL)
    public ResponseEntity<?> printAllDebt() {
        return service.printAllDebt();
    }

    // печатать квитанции для всех помещений за послений месяц в одном файле (для разноски по квартирам)
    @GetMapping(ApiPaths.PAYMENT + ApiPaths.DEBT + ApiPaths.ALL_IN_ONE)
    public ResponseEntity<?> printAllToOnePdfDebtAllApartment() {
        return service.printAllInOneDebtAllApartment();
    }

    // debt details --------------------
    // печатать одну детализированную распечатку долга по конкретной квартире за всё время
    @GetMapping(ApiPaths.PAYMENT + ApiPaths.DEBT + ApiPaths.DETAILS + ApiPaths.PARAM_ID)
    public ResponseEntity<?> printDebtDetails(@PathVariable Long id) {
        return service.printDebtDetails(id);
    }

    // печатать детализированную распечатку долга за всё время для всех квартир на разных файлах
    @GetMapping(ApiPaths.PAYMENT + ApiPaths.DEBT + ApiPaths.DETAILS)
    public ResponseEntity<?> printAllDebtDetails() {
        return service.printAllDebtDetails();
    }


}
