package com.parcial.pagos.service;

import com.parcial.pagos.model.Pago;
import com.parcial.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;

    public Pago procesarPago(Pago pago) {
        pago.setStatus("PENDIENTE");
        pago.setCreatedAt(LocalDateTime.now());
        Pago saved = pagoRepository.save(pago);
        // Simulated payment processing
        saved.setStatus("COMPLETADO");
        saved.setUpdatedAt(LocalDateTime.now());
        Pago completed = pagoRepository.save(saved);
        log.info("Pago procesado con id: {} para orden: {}", completed.getId(), completed.getOrdenId());
        return completed;
    }

    public Optional<Pago> getPagoById(String id) {
        log.info("Buscando pago con id: {}", id);
        return pagoRepository.findById(id);
    }

    public Optional<Pago> getPagoByOrdenId(String ordenId) {
        log.info("Buscando pago para orden: {}", ordenId);
        return pagoRepository.findByOrdenId(ordenId);
    }

    public Optional<Pago> procesarReembolso(String id) {
        return pagoRepository.findById(id).map(pago -> {
            pago.setStatus("REEMBOLSADO");
            pago.setUpdatedAt(LocalDateTime.now());
            Pago updated = pagoRepository.save(pago);
            log.info("Reembolso procesado para pago: {}", id);
            return updated;
        });
    }
}
