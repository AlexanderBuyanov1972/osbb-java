package com.example.osbb.service.payment;

import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.EntryBalanceHouse;
import com.example.osbb.entity.Payment;

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


    public Object getAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to
    );

    // summa -------------------------
    public Object getBalanceAllPayment();

    public Object getBalanceHouse();


    public Object getSummaAllPaymentByBill(String bill);


    public Object getSummaAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to);

    // summa ------------------------------------
    // get invoice notification by ID --------
    public Object getDebtById(Long id);

    // get invoice notification by ID and last month --------
    public Object getDetailsDebtById(Long id);

    public DebtDetails getDebtByBill(String bill);

    public DebtDetails getDetailsDebtByBill(String bill);

    public List<EntryBalanceHouse> getListEntryBalanceHouse();


}
