package com.parcial.pagos.controller;

import com.parcial.pagos.model.Pago;
import com.parcial.pagos.service.PagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@Slf4j
public class PagoController {

    private final PagoService pagoService;

    // POST /pagos/procesar
    @PostMapping("/procesar")
    public ResponseEntity<Pago> procesarPago(@RequestBody Pago pago) {
        log.info("POST /pagos/procesar - ordenId: {}", pago.getOrdenId());
        Pago processed = pagoService.procesarPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(processed);
    }

    // GET /pagos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPagoById(@PathVariable String id) {
        log.info("GET /pagos/{}", id);
        return pagoService.getPagoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /pagos/orden/{id}
    @GetMapping("/orden/{id}")
    public ResponseEntity<Pago> getPagoByOrdenId(@PathVariable String id) {
        log.info("GET /pagos/orden/{}", id);
        return pagoService.getPagoByOrdenId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pagos/{id}/reembolso
    @PutMapping("/{id}/reembolso")
    public ResponseEntity<Pago> procesarReembolso(@PathVariable String id) {
        log.info("PUT /pagos/{}/reembolso", id);
        return pagoService.procesarReembolso(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
