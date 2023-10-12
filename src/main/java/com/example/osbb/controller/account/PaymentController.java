package com.example.osbb.controller.account;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.account.Payment;
import com.example.osbb.service.payment.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.PAYMENT)
public class PaymentController {

    @Autowired
    private IPaymentService service;

    @Autowired
    private HelpMethodsForController response;

    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        return response.returnResponse(service.createPayment(payment));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        return response.returnResponse(service.getPayment(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        return response.returnResponse(service.deletePayment(id));
    }

    // ---------------------- all ----------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllPayment(@RequestBody List<Payment> list) {
        return response.returnResponse(service.createAllPayment(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllPayment() {
        return response.returnResponse(service.getAllPayment());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllPayment() {
        return response.returnResponse(service.deleteAllPayment());
    }

    // select ------------------------

    //получить все платёжки по аккаунту
    @GetMapping(ApiConstants.ALL + ApiConstants.PARAM_PERSONAL_ACCOUNT)
    public ResponseEntity<?> getAllPaymentByPersonalAccount(@PathVariable String personalAccount) {
        return response.returnResponse(service.getAllPaymentByPersonalAccount(personalAccount));
    }

    //получить все платёжки по аккаунту и дате менее чем за предоставленную дату ...
    @GetMapping(ApiConstants.ALL + ApiConstants.PARAM_PERSONAL_ACCOUNT + ApiConstants.PARAM_DATE)
    public ResponseEntity<?> getAllPaymentByPersonalAccountAndDateLessThan(
            @PathVariable String personalAccount,
            @PathVariable LocalDateTime date) {
        return response.returnResponse(service.getAllPaymentByPersonalAccountAndDateLessThan(
                personalAccount, date));
    }

    //получить все платёжки по аккаунту и за указанный период ...
    @GetMapping(ApiConstants.ALL + ApiConstants.PARAM_PERSONAL_ACCOUNT + ApiConstants.PARAM_FROM + ApiConstants.PARAM_TO)
    public ResponseEntity<?> getAllPaymentByPersonalAccountAndDateBetween(
            @PathVariable String personalAccount,
            @PathVariable LocalDateTime from,
            @PathVariable LocalDateTime to
    ) {
        return response.returnResponse(service.getAllPaymentByPersonalAccountAndDateBetween(
                personalAccount, from, to));
    }
    // summa --------------------------------

    //получить сумму всех платежей
    @GetMapping(ApiConstants.ALL + ApiConstants.SUMMA)
    public ResponseEntity<?> getSummaAllPayment() {
        return response.returnResponse(service.getSummaAllPayment());
    }

    //получить сумму всех платежей по аккаунту
    @GetMapping(ApiConstants.ALL +
            ApiConstants.PERSONAL_ACCOUNT +
            ApiConstants.SUMMA +
            ApiConstants.PARAM_PERSONAL_ACCOUNT)
    public ResponseEntity<?> getSummaAllPaymentByPersonalAccount(@PathVariable String personalAccount) {
        return response.returnResponse(service.getSummaAllPaymentByPersonalAccount(personalAccount));
    }

    //получить сумму всех платежей по аккаунту и дате менее чем за предоставленную дату ...
    @GetMapping(ApiConstants.ALL +
            ApiConstants.PERSONAL_ACCOUNT +
            ApiConstants.DATE +
            ApiConstants.SUMMA +
            ApiConstants.PARAM_PERSONAL_ACCOUNT +
            ApiConstants.PARAM_DATE)
    public ResponseEntity<?> getSummaAllPaymentByPersonalAccountAndDateLessThan(
            @PathVariable String personalAccount,
            @PathVariable LocalDateTime date) {
        return response.returnResponse(service.getSummaAllPaymentByPersonalAccountAndDateLessThan(personalAccount, date));
    }

    @GetMapping(ApiConstants.ALL +
            ApiConstants.PERSONAL_ACCOUNT +
            ApiConstants.DATE +
            ApiConstants.SUMMA +
            ApiConstants.PARAM_PERSONAL_ACCOUNT +
            ApiConstants.PARAM_FROM +
            ApiConstants.PARAM_TO)
    public ResponseEntity<?> getSummaAllPaymentByPersonalAccountAndDateBetween(
            @PathVariable String personalAccount,
            @PathVariable LocalDateTime from,
            @PathVariable LocalDateTime to
    ) {
        return response.returnResponse(service.getSummaAllPaymentByPersonalAccountAndDateBetween(
                personalAccount,
                from,
                to));
    }

    @GetMapping(ApiConstants.DEBT + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> getDebtByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.getDebtByApartment(apartment));
    }
    @GetMapping(ApiConstants.DEBT + ApiConstants.DETAILS + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> getDetailsDebtByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.getDetailsDebtByApartment(apartment));
    }
}
