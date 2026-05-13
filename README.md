# SaludLink

Backend para gestionar citas médicas, medicamentos y catálogo de especialidades (Spring Boot + PostgreSQL + JWT).

## Desarrollo local

1. Base de datos: desde esta carpeta, `docker compose up -d db` (puerto **5433**, usuario `postgres`, contraseña como en `docker-compose.yml` y variables en `application.yml`).
2. Arranque (Java **17+**, p. ej. Temurin 21): no hace falta tener Maven instalado; el repo incluye **Maven Wrapper**.
   - **Windows (PowerShell):** `.\mvnw.cmd spring-boot:run`
   - **macOS / Linux:** `./mvnw spring-boot:run`
   - La primera vez descarga Maven en tu carpeta de usuario (necesita red). Si falla por `JAVA_HOME`, instala un **JDK 17 o superior** (p. ej. 21) y define `JAVA_HOME` apuntando a la raíz del JDK (o usa *Run* sobre `SaludLinkApplication` desde el IDE).
   - Si ya tienes Maven en el PATH, `mvn spring-boot:run` sigue siendo válido.
3. La API queda en `http://localhost:8080`.

## Contrato HTTP y documentación

- **Swagger UI** : `http://localhost:8080/swagger-ui.html` — prueba endpoints y el esquema Bearer JWT desde la UI.
- **Swagger Editor** (editor online): útil para revisar o compartir el OpenAPI; en el editor, *File → Import URL* y pega `http://localhost:8080/v3/api-docs` (el backend debe estar levantado y accesible desde tu red; si no, exporta el JSON desde Swagger UI y ábrelo como archivo).

## Capas / arquitectura

En `docs/structurizr/workspace.dsl` hay un workspace mínimo para [Structurizr](https://structurizr.com/) (contexto + contenedores). Puedes subirlo a Structurizr Lite/Cloud.

## Secretos

Usa `.env.example` como referencia de variables. En despliegue (p. ej. Render) suelen definirse `JDBC_*`, `JWT_SECRET`, etc., como en `application.yml`.

## API (lote TB2 reciente)

- `GET /api/auth/me` — usuario actual (JWT); incluye `patientId` / `doctorId` si aplica.
- `GET` / `PUT /api/patients/me/profile` — perfil de salud del paciente autenticado (rol **PATIENT**).
- `PATCH /api/appointments/{id}/reschedule` — reprogramar cita (**PATIENT** dueño o **ADMIN**).

## Primeras funcionalidades

- Citas del paciente (listar y cancelar según rol)
- Medicamentos del paciente (listar, alta, baja lógica; `PUT` y `PATCH` en desactivar son equivalentes)
- Catálogo para agendar (especialidades y listado de médicos)

## Próximas (backlog)

- Recordatorios de medicación
- Documentos médicos (subir/listar/metadatos; sin binario pesado)
- Gestión / alta de médicos (admin o registro doctor)
- Disponibilidad o reglas de agenda (slots, no solapamiento)
- Panel admin (usuarios, verificación médico, reportes simples)
- Notificaciones o recordatorios programados (stub + endpoint)
- Integración / despliegue (Render, variables `JDBC_*`, health)
- Semillas de datos + pruebas documentadas
