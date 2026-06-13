# SaludLink

Backend para gestionar citas médicas, medicamentos, documentos, recordatorios y catálogo de especialidades (Spring Boot + PostgreSQL + JWT).

## Desarrollo local

1. Base de datos: desde esta carpeta, `docker compose up -d db` (puerto **5433**, usuario `postgres`, contraseña como en `docker-compose.yml` y variables en `application.yml`).
2. Arranque (Java **17+**, p. ej. Temurin 21): no hace falta tener Maven instalado; el repo incluye **Maven Wrapper**.
   - **Windows (PowerShell):** `.\mvnw.cmd spring-boot:run`
   - **macOS / Linux:** `./mvnw spring-boot:run`
   - La primera vez descarga Maven en tu carpeta de usuario (necesita red). Si falla por `JAVA_HOME`, instala un **JDK 17 o superior** (p. ej. 21) y define `JAVA_HOME` apuntando a la raíz del JDK (o usa *Run* sobre `SaludLinkApplication` desde el IDE).
   - Si ya tienes Maven en el PATH, `mvn spring-boot:run` sigue siendo válido.
3. La API queda en `http://localhost:8080`. El esquema JPA usa `ddl-auto: update` (p. ej. crea/ajusta tablas como `medication_intakes` al arrancar).

## Contrato HTTP y documentación

- **Swagger UI** : `http://localhost:8080/swagger-ui.html` — prueba endpoints y el esquema Bearer JWT desde la UI.
- **Swagger Editor** (editor online): útil para revisar o compartir el OpenAPI; en el editor, *File → Import URL* y pega `http://localhost:8080/v3/api-docs` (el backend debe estar levantado y accesible desde tu red; si no, exporta el JSON desde Swagger UI y ábrelo como archivo).

## Capas / arquitectura

En `docs/structurizr/workspace.dsl` hay un workspace mínimo para [Structurizr](https://structurizr.com/) (contexto + contenedores). Puedes subirlo a Structurizr Lite/Cloud.

## Secretos

Usa `.env.example` como referencia de variables. En despliegue (p. ej. Render) suelen definirse `JDBC_*`, `JWT_SECRET`, etc., como en `application.yml`.

## API (resumen de contratos útiles)

### Autenticación y perfil

- `POST /api/auth/register`, `POST /api/auth/login`
- `GET /api/auth/me` — usuario actual (JWT); incluye `patientId` / `doctorId` si aplica.
- `GET` / `PUT /api/patients/me/profile` — perfil de salud del paciente autenticado (**PATIENT**).

### Citas

- `PATCH /api/appointments/{id}/reschedule` — reprogramar (**PATIENT** dueño o **ADMIN**).
- Listado, creación y cancelación según roles (ver Swagger).

### Medicamentos

- Listado y alta del paciente; alta/listado por `patientId` para **PATIENT** / **ADMIN** / **DOCTOR**; baja lógica con `PUT` o `PATCH` en `/{id}/deactivate` (equivalentes).
- **Recordatorios:** `GET` / `POST /api/medications/{medicationId}/reminders`; `PATCH /api/medication-reminders/{id}/taken` (**PATIENT** dueño del medicamento, o **ADMIN** / **DOCTOR**).
- **Tomas (intakes):** `GET` / `POST /api/medications/{medicationId}/intakes` — historial y registro de toma (`takenAt` opcional, por defecto ahora; `notes` opcional; cuerpo opcional en el POST).

### Documentos médicos (metadatos / URL, sin subida de binario al servidor)

- `GET` / `POST /api/medical-documents`, `DELETE /api/medical-documents/{id}` (**PATIENT**; `fileUrl` obligatorio en el alta).

### Catálogo y administración

- `GET /api/doctors`, `GET /api/doctors/{id}`, especialidades en `/api/specialties` (según controladores del proyecto).
- `POST /api/admin/doctors` — alta de médico por **ADMIN** (usuario rol **DOCTOR** + fila en `doctors`; `verified` inicia en `false`).

## Funcionalidades ya cubiertas en backend

- Citas del paciente (listar, crear, cancelar, reprogramar según rol).
- Medicamentos del paciente (CRUD operativo y desactivar; alias PUT/PATCH en desactivar).
- Recordatorios de medicación y registro de tomas (intakes).
- Documentos médicos (metadatos y enlace a fichero almacenado fuera, p. ej. S3 o CDN).
- Alta de médicos vía administrador.
- Catálogo para agendar (especialidades y listado de médicos verificados en los endpoints públicos de catálogo).

## Próximas (backlog)

- Verificación de médicos tras el alta (`verified`, panel o flujo de revisión).
- Registro de doctor sin intervención de admin (si el producto lo requiere).
- Disponibilidad o reglas de agenda (slots, no solapamiento).
- Panel admin ampliado (usuarios, reportes simples).
- Notificaciones o recordatorios programados (stub + colas / cron).
- Integración / despliegue (Render, variables `JDBC_*`, health).
- Semillas de datos + pruebas automatizadas documentadas.
