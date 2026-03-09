# Pagos Service

Microservicio para el procesamiento de pagos asociados a órdenes. Soporta múltiples métodos de pago y permite solicitar reembolsos. Los logs son enviados a **AWS CloudWatch** (o LocalStack en desarrollo).

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
| Pagos Service | `8083` |

## Endpoints

| Método | Ruta                     | Descripción                  |
| ------ | ------------------------ | ---------------------------- |
| `POST` | `/pagos/procesar`        | Procesar un pago             |
| `GET`  | `/pagos/{id}`            | Obtener pago por ID          |
| `GET`  | `/pagos/orden/{ordenId}` | Obtener pago por ID de orden |
| `PUT`  | `/pagos/{id}/reembolso`  | Solicitar reembolso          |

### Métodos de pago

`TARJETA` · `TRANSFERENCIA` · `EFECTIVO`

### Estados posibles

`PENDIENTE` · `COMPLETADO` · `REEMBOLSADO` · `FALLIDO`

### Ejemplo de body (POST /pagos/procesar)

```json
{
  "ordenId": "abc123",
  "usuarioId": "user-001",
  "monto": 3000.0,
  "metodo": "TARJETA"
}
```

## Variables de entorno

| Variable                | Descripción                    | Default                           |
| ----------------------- | ------------------------------ | --------------------------------- |
| `MONGODB_URI`           | URI de conexión a MongoDB      | `mongodb://localhost:27030/pagos` |
| `EUREKA_URI`            | URL del servidor Eureka        | `http://localhost:8761/eureka`    |
| `AWS_ACCESS_KEY_ID`     | Credencial AWS                 | `test`                            |
| `AWS_SECRET_ACCESS_KEY` | Credencial AWS                 | `test`                            |
| `AWS_DEFAULT_REGION`    | Región AWS                     | `us-east-1`                       |
| `AWS_ENDPOINT_URL`      | Endpoint override (LocalStack) | —                                 |

## CloudWatch

Los logs se envían al log group `pagos-log-group`.

```bash
aws --endpoint-url=http://localhost:4566 logs describe-log-streams \
  --log-group-name pagos-log-group --region us-east-1
```

## Ejecución local

```bash
mvn spring-boot:run
```

## Ejecución con Docker Compose

```bash
docker compose up --build pagos-service
```
