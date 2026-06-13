workspace "SaludLink" "Vista C4 del backend SaludLink y su entorno (Structurizr DSL): contexto, contenedores y componentes de la API." {

    model {
        admin = person "Administrador" "Rol ADMIN: alta de médicos, reprogramación de citas y operaciones con privilegios."
        patient = person "Paciente" "Rol PATIENT: citas propias, medicamentos, recordatorios, tomas (intakes), perfil de salud y documentos médicos (metadatos/URL)."
        clinician = person "Personal clínico" "Rol DOCTOR: apoyo sobre medicamentos, recordatorios e intakes cuando la API lo permite; consulta de catálogo."

        saludlink = softwareSystem "SaludLink" "Plataforma de salud: agenda médica, adherencia (recordatorios e intakes), documentos por enlace externo, catálogo de especialidades y médicos verificados, autenticación JWT y administración de altas de médico." {
            spa = container "Aplicación web" "SPA Angular (p. ej. localhost:4200); consume la API con Authorization: Bearer JWT." "TypeScript / Angular"

            restApi = container "API REST" "Spring Boot 3; Spring Security + JWT y @PreAuthorize por rol; springdoc-openapi; Bean Validation; JPA/Hibernate sobre PostgreSQL (ddl-auto update); Actuator health." "Java 17" {
                auth = component "Autenticación" "POST /api/auth/register y /login; validación JWT; GET /api/auth/me con ids de perfil." "Spring Security"
                patientCare = component "Paciente: perfil y documentos" "GET/PUT /api/patients/me/profile; GET/POST/DELETE /api/medical-documents (metadatos y fileUrl, sin binario en servidor)." "Spring MVC"
                scheduling = component "Citas" "Listado, alta, cancelación y PATCH /api/appointments/{id}/reschedule según dueño y rol." "Spring MVC"
                therapy = component "Medicamentos y adherencia" "Medicamentos (CRUD operativo, baja lógica PUT/PATCH deactivate); recordatorios bajo /api/medications/{id}/reminders y PATCH /api/medication-reminders/{id}/taken; intakes bajo /api/medications/{id}/intakes; reglas de acceso por propietario o roles elevados." "Spring MVC"
                catalog = component "Catálogo médico" "GET /api/doctors y /api/specialties para agendar (médicos verificados en listados públicos de catálogo)." "Spring MVC"
                administration = component "Administración" "POST /api/admin/doctors: usuario DOCTOR + ficha en doctors (verified=false hasta flujo futuro de verificación)." "Spring MVC"
            }

            db = container "Base de datos" "PostgreSQL 15 (Docker local típico puerto 5433): usuarios, pacientes, médicos, citas, medicamentos, recordatorios, intakes, documentos médicos." "SQL / PostgreSQL"
        }

        patient -> spa "Usa"
        clinician -> spa "Usa"
        admin -> spa "Usa"

        spa -> restApi "HTTPS, JSON, Bearer JWT (consume todos los recursos REST)"

        restApi -> db "JDBC / JPA (Hibernate)"
    }

    views {
        systemContext saludlink "01-Contexto" {
            include *
            autolayout lr
        }

        container saludlink "02-Contenedores" {
            include *
            autolayout lr
        }

        component restApi "03-Componentes-API" {
            include *
            autolayout lr
        }

        theme default
    }
}
