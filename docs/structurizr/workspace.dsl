workspace "SaludLink" "Vista C4 del backend SaludLink (package by feature)." {

    model {
        admin = person "Administrador" "Rol ADMIN."
        patient = person "Paciente" "Rol PATIENT."
        clinician = person "Medico" "Rol DOCTOR."
        institutionAdmin = person "Admin institucional" "Rol INSTITUTION_ADMIN."

        saludlink = softwareSystem "SaludLink" "Plataforma de salud digital." {
            spa = container "Aplicacion web" "SPA responsive." "TypeScript"

            restApi = container "API REST" "Spring Boot 3, JWT, package by feature." "Java 21" {
                auth = component "auth" "Registro, login, /api/auth/me, bloqueo por intentos." "Spring MVC"
                patientModule = component "patient" "Perfil de salud y preferencias de alertas." "Spring MVC"
                doctor = component "doctor" "Catalogo, credenciales CMP, disponibilidad." "Spring MVC"
                appointment = component "appointment" "Citas, reprogramacion, anti-solapamiento." "Spring MVC"
                medication = component "medication" "Medicamentos, recordatorios, intakes, scheduler MISSED." "Spring MVC"
                medicalrecord = component "medicalrecord" "Documentos medicos por URL." "Spring MVC"
                institution = component "institution" "Registro RUC, dashboard, reportes, medicos afiliados." "Spring MVC"
                adherence = component "adherence" "Tablero de adherencia para medicos." "Spring MVC"
                mentalhealth = component "mentalhealth" "Cribado de salud mental." "Spring MVC"
                telemedicine = component "telemedicine" "Videoconsulta, chat post-consulta y emergencia." "Spring MVC"
                payment = component "payment" "Pagos de citas en linea." "Spring MVC"
                review = component "review" "Calificaciones de medicos." "Spring MVC"
                shared = component "shared" "Security, CORS, OpenAPI, excepciones, paginacion." "Spring"
            }

            db = container "Base de datos" "PostgreSQL." "PostgreSQL"
        }

        patient -> spa "Usa"
        clinician -> spa "Usa"
        admin -> spa "Usa"
        institutionAdmin -> spa "Usa"
        spa -> restApi "HTTPS JSON Bearer JWT"
        restApi -> db "JPA"
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
