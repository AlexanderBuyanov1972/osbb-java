package com.example.osbb.service.payment;

import com.example.osbb.entity.account.Payment;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentService {
    // one -----------------------
    public Object createPayment(Payment payment);

    public Object getPayment(Long id);

    public Object deletePayment(Long id);

    // all -----------------------

    public Object createAllPayment(List<Payment> payments);

    public Object getAllPayment();

    public Object deleteAllPayment();

    // select ------------------------

    public Object getAllPaymentByBill(String bill);
    public Object getAllPaymentByDescription(String description);


    public Object getAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to
    );
    public Object getAllPaymentByDescriptionAndDateBetween(
            String description,
            LocalDateTime from,
            LocalDateTime to
    );
    // summa -------------------------
    public Object getBalanceAllPayment();
    public Object getBalanceHouse();
    public Object getSummaAllPaymentByBill(String bill);
    public Object getSummaAllPaymentByDescription(String description);
    public Object getSummaAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to);
    public Object getSummaAllPaymentByDescriptionAndDateBetween(
            String description,
            LocalDateTime from,
            LocalDateTime to);

    // summa ------------------------------------
    // get invoice notification by apartment --------
    public Object getDebtByApartment(String apartment);

    // get invoice notification by apartment and last month --------
    public Object getDetailsDebtByApartment(String apartment);


}
