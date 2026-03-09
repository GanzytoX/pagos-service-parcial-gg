package com.parcial.pagos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    private String id;

    private String ordenId;

    private String usuarioId;

    private Double monto;

    /** Métodos: TARJETA, TRANSFERENCIA, EFECTIVO */
    private String metodo;

    /** Estados: PENDIENTE, COMPLETADO, REEMBOLSADO, FALLIDO */
    private String status = "PENDIENTE";

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
