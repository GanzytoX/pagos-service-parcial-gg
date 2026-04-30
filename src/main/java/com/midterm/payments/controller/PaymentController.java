package com.midterm.payments.controller;

import com.midterm.payments.model.Payment;
import com.midterm.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // POST /payments/process
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment, @RequestHeader(value = "X-Retry", required = false) String isRetry) {
        try {
            log.info("POST /payments/process - orderId: {}", payment.getOrderId());
            Payment processed = paymentService.processPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            log.error("Error in POST /payments/process - sending to topic: {}", payment, e);
            if (!"true".equals(isRetry)) {
                try {
                    kafkaTemplate.send("payments_retry_jobs", objectMapper.writeValueAsString(payment));
                } catch (Exception kafkaEx) {
                    log.error("Error sending to Kafka", kafkaEx);
                }
            } else {
                log.warn("This is a failed retry, NOT sending to Kafka again to avoid infinite loop.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id) {
        log.info("GET /payments/{}", id);
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /payments/order/{id}
    @GetMapping("/order/{id}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable String id) {
        log.info("GET /payments/order/{}", id);
        return paymentService.getPaymentByOrderId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /payments/{id}/refund
    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> processRefund(@PathVariable String id) {
        log.info("PUT /payments/{}/refund", id);
        return paymentService.processRefund(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
