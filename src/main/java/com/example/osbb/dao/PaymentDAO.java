package com.example.osbb.dao;

import com.example.osbb.entity.Payment;
import com.example.osbb.enums.TypeOfBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
public interface PaymentDAO extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBill(String bill);

    List<Payment> findAllByDescription(String description);

    List<Payment> findAllByTypeBill(TypeOfBill typeBill);

    List<Payment> findAllPaymentByBillAndDateBetween(
            String bill,
            LocalDateTime from,
            LocalDateTime to
    );

    List<Payment> findAllPaymentByDescriptionAndDateBetween(
            String description,
            LocalDateTime from,
            LocalDateTime to
    );
}
