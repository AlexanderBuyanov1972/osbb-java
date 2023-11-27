package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.Payment;
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

    // one ---------
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

    // all -----------
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

    // list payment select --------------------

    //получить все платёжки по счёту
    @GetMapping(ApiConstants.ALL + ApiConstants.PARAM_BILL)
    public ResponseEntity<?> getAllPaymentComingByBill(@PathVariable String bill) {
        return response.returnResponse(service.getAllPaymentByBill(bill));
    }

    //получить все платёжки по счёту и за указанный период ...
    @GetMapping(ApiConstants.ALL + ApiConstants.PARAM_BILL + ApiConstants.PARAM_FROM + ApiConstants.PARAM_TO)
    public ResponseEntity<?> getAllPaymentByBillAndDateBetween(
            @PathVariable String bill,
            @PathVariable LocalDateTime from,
            @PathVariable LocalDateTime to
    ) {
        return response.returnResponse(service.getAllPaymentByBillAndDateBetween(bill, from, to));
    }
    // summa --------------------------------

    //получить сумму всех платежей по счёту
    @GetMapping(ApiConstants.ALL + ApiConstants.BILL + ApiConstants.SUMMA + ApiConstants.PARAM_BILL)
    public ResponseEntity<?> getSummaAllPaymentByBill(@PathVariable String bill) {
        return response.returnResponse(service.getSummaAllPaymentByBill(bill));
    }

    //получить сумму всех платежей по счёту за указанный период ... -----
    @GetMapping(ApiConstants.ALL + ApiConstants.BILL + ApiConstants.DATE + ApiConstants.SUMMA +
            ApiConstants.PARAM_BILL + ApiConstants.PARAM_FROM + ApiConstants.PARAM_TO)
    public ResponseEntity<?> getSummaAllPaymentByBillAndDateBetween(
            @PathVariable String bill,
            @PathVariable LocalDateTime from,
            @PathVariable LocalDateTime to
    ) {
        return response.returnResponse(service.getSummaAllPaymentByBillAndDateBetween(bill, from, to));
    }

    // получить баланс всех платежей и приходных, и расходных -----
    @GetMapping(ApiConstants.BALANCE)
    public ResponseEntity<?> getBalance() {
        return response.returnResponse(service.getBalanceAllPayment());
    }
    // получить лист помещений в разрезе оплаты (задолженость/переплата) за услуги ОСББ -----
    @GetMapping(ApiConstants.BALANCE + ApiConstants.ALL)
    public ResponseEntity<?> getBalanceHouse() {
        return response.returnResponse(service.getBalanceHouse());
    }

    // debt -----------------
    @GetMapping(ApiConstants.DEBT + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> getDebtByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.getDebtByApartment(apartment));
    }

    @GetMapping(ApiConstants.DEBT + ApiConstants.DETAILS + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> getDetailsDebtByApartment(@PathVariable String apartment) {
        return response.returnResponse(service.getDetailsDebtByApartment(apartment));
    }


}
