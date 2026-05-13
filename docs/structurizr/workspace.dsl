workspace "SaludLink" "Vista C4 mínima del backend y su entorno (Structurizr / DSL)." {

    model {
        patient = person "Paciente" "Usuario autenticado (rol PATIENT)."
        staff = person "Personal de salud" "Médico o administrador (DOCTOR / ADMIN)."

        saludlink = softwareSystem "SaludLink" "Citas, medicamentos, catálogo de especialidades y médicos." {
            spa = container "Aplicación web" "Angular (localhost:4200)." "TypeScript"
            api = container "API REST" "Spring Boot 3, Spring Security JWT, validación, springdoc-openapi." "Java 17"
            db = container "Base de datos" "PostgreSQL 15." "SQL"
        }

        patient -> spa "Usa"
        staff -> spa "Usa"
        spa -> api "HTTPS, JSON, Bearer JWT"
        api -> db "JDBC / JPA"
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

        theme default
    }
}
