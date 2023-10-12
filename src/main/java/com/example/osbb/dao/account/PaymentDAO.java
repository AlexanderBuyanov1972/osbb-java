package com.example.osbb.dao.account;

import com.example.osbb.entity.account.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
public interface PaymentDAO extends JpaRepository<Payment, Long> {
    List<Payment> findAllByPersonalAccount(String personalAccount);
    List<Payment> findAllPaymentByPersonalAccountAndDateLessThan(String personalAccount, LocalDateTime date);
    List<Payment> findAllPaymentByPersonalAccountAndDateBetween(
            String personalAccount,
            LocalDateTime from,
            LocalDateTime to
    );
}
