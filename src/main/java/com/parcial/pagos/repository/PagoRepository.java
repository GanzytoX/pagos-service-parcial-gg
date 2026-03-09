package com.parcial.pagos.repository;

import com.parcial.pagos.model.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {
    Optional<Pago> findByOrdenId(String ordenId);
    List<Pago> findByUsuarioId(String usuarioId);
    List<Pago> findByStatus(String status);
}
