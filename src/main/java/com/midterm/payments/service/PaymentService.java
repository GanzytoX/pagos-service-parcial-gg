package com.midterm.payments.service;

import com.midterm.payments.model.Payment;
import com.midterm.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository pagoRepository;

    public Payment processPayment(Payment payment) {
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());
        Payment saved = pagoRepository.save(payment);
        // Simulated payment processing
        saved.setStatus("COMPLETED");
        saved.setUpdatedAt(LocalDateTime.now());
        Payment completed = pagoRepository.save(saved);
        log.info("Payment procesado con id: {} para order: {}", completed.getId(), completed.getOrderId());
        return completed;
    }

    public Optional<Payment> getPaymentById(String id) {
        log.info("Buscando payment con id: {}", id);
        return pagoRepository.findById(id);
    }

    public Optional<Payment> getPaymentByOrderId(String orderId) {
        log.info("Buscando payment para order: {}", orderId);
        return pagoRepository.findByOrderId(orderId);
    }

    public Optional<Payment> processRefund(String id) {
        return pagoRepository.findById(id).map(payment -> {
            payment.setStatus("REFUNDED");
            payment.setUpdatedAt(LocalDateTime.now());
            Payment updated = pagoRepository.save(payment);
            log.info("Reembolso procesado para payment: {}", id);
            return updated;
        });
    }
}
