package com.example.osbb.service.payment;

import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.EntryBalanceHouse;
import com.example.osbb.entity.Payment;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentService {
    // one -----------------------
    ResponseEntity<?> createPayment(Payment payment);

    ResponseEntity<?> getPayment(Long id);

    ResponseEntity<?> deletePayment(Long id);

    // all -----------------------

    ResponseEntity<?> createAllPayment(List<Payment> payments);

    ResponseEntity<?> getAllPayment();

    ResponseEntity<?> deleteAllPayment();

    // select ------------------------

    ResponseEntity<?> getAllPaymentByBill(String bill);


    ResponseEntity<?> getAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to
    );

    // summa -------------------------
    ResponseEntity<?> getBalanceAllPayment();

    ResponseEntity<?> getBalanceHouse();


    ResponseEntity<?> getSummaAllPaymentByBill(String bill);


    ResponseEntity<?> getSummaAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to);

    // summa ------------------------------------
    // get invoice notification by ID --------
    ResponseEntity<?> getDebtById(Long id);

    // get invoice notification by ID and last month --------
    ResponseEntity<?> getDetailsDebtById(Long id);

    DebtDetails getDebtByBill(String bill);

    DebtDetails getDetailsDebtByBill(String bill);

    List<EntryBalanceHouse> getListEntryBalanceHouse();


}
