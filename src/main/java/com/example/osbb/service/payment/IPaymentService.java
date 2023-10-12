package com.example.osbb.service.payment;

import com.example.osbb.entity.account.Payment;
import com.example.osbb.entity.owner.Photo;

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

    public Object getAllPaymentByPersonalAccount(String personalAccount);

    public Object getAllPaymentByPersonalAccountAndDateLessThan(String personalAccount, LocalDateTime date);

    public Object getAllPaymentByPersonalAccountAndDateBetween(
            String personalAccount,
            LocalDateTime from,
            LocalDateTime to
    );

    // summa -------------------------

    public Object getSummaAllPayment();

    public Object getSummaAllPaymentByPersonalAccount(String personalAccount);

    public Object getSummaAllPaymentByPersonalAccountAndDateLessThan(
            String personalAccount,
            LocalDateTime date);

    public Object getSummaAllPaymentByPersonalAccountAndDateBetween(
            String personalAccount,
            LocalDateTime from,
            LocalDateTime to);

    // get invoice notification by apartment --------
    public Object getDebtByApartment(String apartment);

    // get invoice notification by apartment and last month --------
    public Object getDetailsDebtByApartment(String apartment);

}
