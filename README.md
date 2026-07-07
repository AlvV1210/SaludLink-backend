# SaludLink

Backend para gestionar citas médicas, medicamentos, documentos, recordatorios, instituciones y catálogo de especialidades (Spring Boot + PostgreSQL + JWT).

## Stack

| Capa | Tecnología |
|---|---|
| Java | 21 |
| Framework | Spring Boot 3.2.6 |
| Base de datos | PostgreSQL (Docker local / Render) |
| Mapper | MapStruct 1.6.3 |
| Documentación | springdoc-openapi (Swagger UI) |
| Build | Maven Wrapper |

## Arquitectura

Organización **package by feature** (referencia: PagoYa). Cada bounded context agrupa `controller`, `service`, `repository`, `model`, `dto`, `mapper`, `exception`. Lo transversal vive en `shared/`.

```
src/main/java/com/saludlink/
├── SaludLinkApplication.java
├── shared/           config, exception, pagination, HealthController
├── auth/
├── patient/
├── doctor/
├── appointment/
├── medication/
├── medicalrecord/
├── institution/
├── adherence/
├── mentalhealth/
├── telemedicine/     videoconsulta, chat, emergencia
├── payment/
└── review/
```

## Desarrollo local

1. Copia `.env.example` a `.env` y ajusta valores.
2. Infraestructura local:
   ```powershell
   docker compose up -d saludlink_db pgadmin
   ```
3. Arranque del backend:
   - **Windows:** `mvn spring-boot:run`
   - **Windows:** `.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local`
   - **IntelliJ IDEA:** importar como Maven, JDK 21, Run `SaludLinkApplication` con perfil `local` y variables de `.env.example`.
4. URLs locales:

| Servicio | URL / puerto | Credenciales |
|---|---|---|
| API | `http://localhost:8080` | — |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | — |
| PostgreSQL (`saludlink_db`) | `localhost:5433` | usuario `postgres` / `admin123` |
| pgAdmin (`saludlink_pgadmin`) | `http://localhost:8082` | `admin@saludlink.com` / `admin` |

### pgAdmin (registrar el servidor)

Desde pgAdmin en Docker, agrega un servidor con:

| Campo | Valor |
|---|---|
| Host name/address | `saludlink_db` |
| Port | `5432` |
| Maintenance database | `saludlink_db` |
| Username | `postgres` |
| Password | `admin123` |

Si usas pgAdmin instalado en tu PC (fuera de Docker), conecta con host `localhost` y puerto `5433`.

### Base de datos legacy (desarrollo local)

Si migraste desde una versión anterior del proyecto y ves errores al crear citas (`appointments_modality_check` o `appointments_status_check`), ejecuta en Postgres:

```powershell
Get-Content docs/local-db-legacy-constraints.sql | docker exec -i saludlink_db psql -U postgres -d saludlink_db
```

O pega el contenido de `docs/local-db-legacy-constraints.sql` en pgAdmin.

## Despliegue en Render

El archivo [`render.yaml`](render.yaml) define un **Blueprint** que crea PostgreSQL (`saludlink-db`) y el servicio web Docker (`saludlink-api`), igual que en PagoYa.

### Pasos

1. Sube este repositorio a GitHub (rama `main`).
2. En [Render](https://render.com): **New +** → **Blueprint** → conecta el repo.
3. Tras el primer deploy, en el dashboard del servicio `saludlink-api` define **`CORS_ALLOWED_ORIGINS`** con la URL del frontend (ej. `https://tu-app.vercel.app`). Sin esto el navegador bloqueará peticiones CORS.
4. Verifica el health check:

   ```powershell
   curl https://saludlink-api.onrender.com/actuator/health
   ```

   (La URL exacta aparece en el dashboard de Render.)

### Variables que Render configura automáticamente

| Variable | Origen |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` (en `render.yaml`) |
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` | Base de datos vinculada |
| `JWT_SECRET` | Generado por Render |
| `PORT` | Asignado por Render en runtime |

### Perfil `prod`

`src/main/resources/application-prod.yml` usa `DB_*` (Render) y también acepta `JDBC_DATABASE_*` o `DB_URL` si despliegas en otra plataforma.

### Despliegue manual (sin Blueprint)

Crea un **Web Service** con runtime **Docker**, conecta el repo y define:

- `SPRING_PROFILES_ACTIVE=prod`
- Variables de BD (`DB_HOST`, etc. o `JDBC_DATABASE_URL`)
- `JWT_SECRET` (cadena larga aleatoria)
- `CORS_ALLOWED_ORIGINS`

## Despliegue Railway (alternativa)

Define en el dashboard: `SPRING_PROFILES_ACTIVE=prod`, `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME`, `JDBC_DATABASE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `PORT`, `CORS_ALLOWED_ORIGINS`.

## Convenciones del código

| Tema | Convención |
|---|---|
| DTOs | Java `record` + Bean Validation (`*Request`, `*Response`, `*Report`) |
| Services | `I<X>Service` + implementación; `@Transactional` por método |
| Mappers | MapStruct `@Mapper(componentModel = "spring")` |
| Excepciones | `BusinessRuleException` (400), `ResourceNotFoundException` (404) → `GlobalExceptionHandler` |
| Controllers | Delgados, `@Tag`, `@Operation`; URLs `/api/<kebab-plural>` |
| Paginación | `PageResponse<T>` en `shared/pagination` |

## GitFlow

- `main` → producción (Render / Railway)
- `develop` → integración
- Features: `feature/BOOK-XX-descripcion` → PR a `develop` (proyecto SaludLink en Jira)

## Jira

Ver [docs/jira-setup.md](docs/jira-setup.md) para épicas, stories HU01–HU24, subtareas y sprints en [Jira SaludLink](https://alvarofelices8.atlassian.net/jira/software/projects/BOOK/boards).

## API (resumen)

- **Auth:** `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me`
- **Paciente:** `GET/PUT /api/patients/me/profile`, `PUT /api/patients/me/notification-preferences`
- **Institución:** `POST /api/institutions/register`, dashboard y reportes en `/api/institutions/me/*`
- **Médicos:** catálogo público `GET /api/doctors`, disponibilidad `POST /api/doctors/me/availability`
- **Citas:** CRUD + `PATCH /api/appointments/{id}/reschedule`
- **Medicación:** medicamentos, recordatorios, intakes; `PATCH` deactivate
- **Documentos:** `GET/POST/DELETE /api/medical-documents`
- **Adherencia:** `GET /api/adherence/patients/{id}`
- **Salud mental:** `POST /api/mental-health/screenings`
- **Emergencia:** `GET /api/emergency/contacts`
- **Dependientes:** `GET/POST /api/patients/me/dependents`
- **Export historial:** `POST /api/medical-records/export`, `GET /api/medical-records/export/{code}/download`
- **Pagos:** `POST /api/payments/appointments/{id}`
- **Reseñas:** `POST /api/reviews`, `GET /api/doctors/{id}/reviews`
- **Telemedicina:** `POST /api/telemedicine/appointments/{id}/join`, mensajes en `/messages`

## Structurizr

Workspace C4 en `docs/structurizr/workspace.dsl`.
