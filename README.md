# Payments Service

Microservicio para el procesamiento de payments asociados a órdenes. Soporta múltiples métodos de payment y permite solicitar refunds. Los logs son enviados a **AWS CloudWatch** (o LocalStack en desarrollo).

## Tecnologías

- Java 17
- Spring Boot 3.5.11
- Spring Data MongoDB
- Spring Cloud Netflix Eureka Client
- AWS SDK v2 (CloudWatch Logs)
- Lombok

## Puerto

| Servicio      | Puerto |
| ------------- | ------ |
| Payments Service | `8083` |

## Endpoints

| Método | Ruta                     | Descripción                  |
| ------ | ------------------------ | ---------------------------- |
| `POST` | `/payments/procesar`        | Procesar un payment             |
| `GET`  | `/payments/{id}`            | Obtener payment por ID          |
| `GET`  | `/payments/order/{orderId}` | Obtener payment por ID de order |
| `PUT`  | `/payments/{id}/refund`  | Solicitar refund          |

### Métodos de payment

`TARJETA` · `TRANSFERENCIA` · `EFECTIVO`

### Estados posibles

`PENDIENTE` · `COMPLETADO` · `REEMBOLSADO` · `FALLIDO`

### Ejemplo de body (POST /payments/procesar)

```json
{
  "orderId": "abc123",
  "userId": "user-001",
  "amount": 3000.0,
  "method": "TARJETA"
}
```

## Variables de entorno

| Variable                | Descripción                    | Default                           |
| ----------------------- | ------------------------------ | --------------------------------- |
| `MONGODB_URI`           | URI de conexión a MongoDB      | `mongodb://localhost:27030/payments` |
| `EUREKA_URI`            | URL del servidor Eureka        | `http://localhost:8761/eureka`    |
| `AWS_ACCESS_KEY_ID`     | Credencial AWS                 | `test`                            |
| `AWS_SECRET_ACCESS_KEY` | Credencial AWS                 | `test`                            |
| `AWS_DEFAULT_REGION`    | Región AWS                     | `us-east-1`                       |
| `AWS_ENDPOINT_URL`      | Endpoint override (LocalStack) | —                                 |

## CloudWatch

Los logs se envían al log group `payments-log-group`.

```bash
aws --endpoint-url=http://localhost:4566 logs describe-log-streams \
  --log-group-name payments-log-group --region us-east-1
```

## Ejecución local

```bash
mvn spring-boot:run
```

## Ejecución con Docker Compose

```bash
docker compose up --build pagos-service
```
