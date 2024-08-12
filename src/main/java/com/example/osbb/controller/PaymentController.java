package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.Payment;
import com.example.osbb.service.payment.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(value = ApiPaths.PAYMENT)
public class PaymentController {

    @Autowired
    private IPaymentService service;

    // one ---------
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {
        return service.createPayment(payment);
    }

    @GetMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        return service.getPayment(id);
    }

    @DeleteMapping(ApiPaths.PARAM_ID)
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        return service.deletePayment(id);
    }

    // all -----------
    @PostMapping(ApiPaths.ALL)
    public ResponseEntity<?> createAllPayment(@RequestBody List<Payment> list) {
        return service.createAllPayment(list);
    }

    @GetMapping(ApiPaths.ALL)
    public ResponseEntity<?> getAllPayment() {
        return service.getAllPayment();
    }

    @DeleteMapping(ApiPaths.ALL)
    public ResponseEntity<?> deleteAllPayment() {
        return service.deleteAllPayment();
    }

    // list payment select --------------------

    //получить все платёжки по счёту
    @GetMapping(ApiPaths.ALL + ApiPaths.PARAM_BILL)
    public ResponseEntity<?> getAllPaymentByBill(@PathVariable String bill) {
        return service.getAllPaymentByBill(bill);
    }

    //получить все платёжки по счёту и за указанный период ...
    @GetMapping(ApiPaths.ALL + ApiPaths.PARAM_BILL + ApiPaths.PARAM_FROM + ApiPaths.PARAM_TO)
    public ResponseEntity<?> getAllPaymentByBillAndDateBetween(
            @PathVariable String bill,
            @PathVariable LocalDateTime from,
            @PathVariable LocalDateTime to
    ) {
        return service.getAllPaymentByBillAndDateBetween(bill, from, to);
    }
    // summa --------------------------------

    //получить сумму всех платежей по счёту
    @GetMapping(ApiPaths.ALL + ApiPaths.BILL + ApiPaths.SUMMA + ApiPaths.PARAM_BILL)
    public ResponseEntity<?> getSummaAllPaymentByBill(@PathVariable String bill) {
        return service.getSummaAllPaymentByBill(bill);
    }

    // получить сумму всех платежей по счёту за указанный период ... -----
    @GetMapping(ApiPaths.ALL + ApiPaths.BILL + ApiPaths.DATE + ApiPaths.SUMMA +
            ApiPaths.PARAM_BILL + ApiPaths.PARAM_FROM + ApiPaths.PARAM_TO)
    public ResponseEntity<?> getSummaAllPaymentByBillAndDateBetween(
            @PathVariable String bill,
            @PathVariable LocalDateTime from,
            @PathVariable LocalDateTime to
    ) {
        return service.getSummaAllPaymentByBillAndDateBetween(bill, from, to);
    }

    //PAYMENT + BALANCE -----------------------
    // получить баланс всех платежей и приходных, и расходных -----
    @GetMapping(ApiPaths.BALANCE)
    public ResponseEntity<?> getBalance() {
        return service.getBalanceAllPayment();
    }

    // получить лист помещений в разрезе оплаты (задолженость/переплата) за услуги ОСББ -----
    @GetMapping(ApiPaths.BALANCE + ApiPaths.ALL)
    public ResponseEntity<?> getBalanceHouse() {
        return service.getBalanceHouse();
    }

    // PAYMENT + DEBT -----------------------------------------------------------------------
    // долг помещения по ID
    @GetMapping(ApiPaths.DEBT + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getDebtById(@PathVariable Long id) {
        return service.getDebtById(id);
    }

    // детализация долга помещения по ID
    @GetMapping(ApiPaths.DEBT + ApiPaths.DETAILS + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getDetailsDebtById(@PathVariable Long id) {
        return service.getDetailsDebtById(id);
    }


}
