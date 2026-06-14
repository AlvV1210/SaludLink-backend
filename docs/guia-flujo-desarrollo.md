# Guía de flujo de desarrollo — Backend (SaludLink)

Documento de referencia para compilar, ejecutar y desplegar el backend Spring Boot de SaludLink.

---

## 1. Arquitectura en producción (tu setup actual)

```
┌─────────────────────┐         ┌──────────────────────────────┐
│  Tu PC (local)      │  HTTP   │  Railway (nube)              │
│                     │ ◄────── │                              │
│  Angular frontend   │         │  Backend Spring Boot (este)  │
│  localhost:4200     │         │  saludlink-production...     │
└─────────────────────┘         │         │                    │
                                │         ▼                    │
                                │  PostgreSQL (Railway)        │
                                └──────────────────────────────┘
```

| Componente | Dónde corre | ¿Lo levantas tú? |
|------------|-------------|------------------|
| **Backend (este repo)** | Railway (producción) | No en uso normal |
| **Backend local** | Tu PC (`mvn spring-boot:run`) | Solo al desarrollar Java |
| **Base de datos** | Railway Postgres | No en uso normal |
| **Postgres local** | Docker (`docker compose up db`) | Solo al desarrollar backend local |

---

## 2. Requisitos previos

| Herramienta | Versión | Para qué |
|-------------|---------|----------|
| **Java (JDK)** | 21 | Compilar y ejecutar Spring Boot |
| **Maven** | 3.9+ | Build y `spring-boot:run` |
| **Docker Desktop** | Cualquiera reciente | Solo para Postgres local |
| **Git** | — | Versionado y deploy a Railway |

### Verificar Java y Maven

```powershell
java -version    # debe mostrar 21.x
mvn -v           # Java version: 21.x
```

Si Maven muestra Java 17, revisa `JAVA_HOME` o la configuración de terminal en Cursor/IntelliJ.

---

## 3. Flujo diario recomendado (Railway — sin levantar nada local)

**Cuándo:** el backend ya está en Railway y solo trabajas en frontend u otras tareas. **No necesitas abrir este repo ni Docker.**

1. Railway mantiene **Postgres** y **SaludLink** online.
2. Verifica en [railway.app](https://railway.app) que ambos servicios estén **Online**.
3. URL pública: `https://saludlink-production.up.railway.app`
4. Health check:

   ```powershell
   curl https://saludlink-production.up.railway.app/actuator/health
   ```

5. El frontend (repo `saludlink-frontend`) consume esa URL vía `.env`.

**No necesitas:** Docker, `mvn spring-boot:run`, IntelliJ.

---

## 4. Flujo de desarrollo backend local

**Cuándo:** modificas código Java, controllers, servicios, seguridad, etc.

### Opción A — Postgres en Docker + Spring Boot en terminal (recomendado)

**1. Abrir Docker Desktop** (debe estar en ejecución).

**2. Terminal 1 — Base de datos:**

```powershell
cd C:\CC235-TP-TF-2025-1\SaludLink
docker compose up db
```

Levanta PostgreSQL en `localhost:5433` con:
- Base de datos: `saludlink_db`
- Usuario: `postgres`
- Contraseña: `admin123`

**3. Terminal 2 — Backend:**

```powershell
cd C:\CC235-TP-TF-2025-1\SaludLink
mvn spring-boot:run
```

Backend local: [http://localhost:8080](http://localhost:8080)

**4. Frontend apuntando a local:**

En `saludlink-frontend/.env`:

```env
VITE_API_URL=http://localhost:8080
```

Luego `npm start` en el repo frontend.

### Opción B — IntelliJ IDEA

1. Abrir proyecto `SaludLink` en IntelliJ.
2. **File → Project Structure → Project SDK** → Java **21** (Eclipse Temurin).
3. `docker compose up db` (igual que arriba).
4. Ejecutar `com.saludlink.SaludLinkApplication` (Run) o Maven → `spring-boot:run`.

---

## 5. Configuración de base de datos

Definida en `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/saludlink_db   # valor por defecto local
    username: postgres
    password: admin123
```

### Entornos

| Entorno | URL de BD | Cómo se configura |
|---------|-----------|-------------------|
| **Local (Docker)** | `localhost:5433/saludlink_db` | Por defecto en `application.yml` |
| **Railway** | URL interna de Postgres | Variables `JDBC_DATABASE_URL`, `JDBC_DATABASE_USERNAME`, `JDBC_DATABASE_PASSWORD` en Railway |

En Railway, el backend y Postgres están en la misma red; **no uses la BD de Railway desde tu PC** para desarrollo habitual (riesgo de tocar datos de producción).

---

## 6. Variables de entorno

| Variable | Local (default) | Railway |
|----------|-----------------|---------|
| `JDBC_DATABASE_URL` | `jdbc:postgresql://localhost:5433/saludlink_db` | Configurada en Railway → Variables |
| `JDBC_DATABASE_USERNAME` | `postgres` | Configurada en Railway |
| `JDBC_DATABASE_PASSWORD` | `admin123` | Configurada en Railway |
| `JWT_SECRET` | Valor por defecto en yml | Debe definirse en Railway (producción) |
| `PORT` / `SERVER_PORT` | `8080` | Railway asigna `PORT` automáticamente |

---

## 7. Deploy a Railway (Git + GitHub)

El backend en Railway está conectado a **GitHub**. Al hacer push/merge a la rama configurada, Railway redeploya automáticamente.

### Flujo

```
1. Trabajar en rama: git checkout -b feature/mi-endpoint
2. Probar local (opcional): docker compose up db + mvn spring-boot:run
3. git add / git commit
4. git push origin feature/mi-endpoint
5. GitHub → Pull Request → merge a develop/main
6. Railway → Deployments → esperar "Deployment successful"
7. Probar: curl https://saludlink-production.up.railway.app/actuator/health
8. Frontend con VITE_API_URL apuntando a Railway
```

### Qué revisar en Railway tras un deploy

- Pestaña **Deployments** → estado **ACTIVE**
- Pestaña **Variables** → `JDBC_*`, `JWT_SECRET`, etc.
- Pestaña **Metrics / Logs** si hay errores de arranque

---

## 8. Estructura del proyecto (DDD)

```
src/main/java/com/saludlink/
├── presentation/controller/     # REST controllers
├── application/
│   ├── service/                 # Interfaces de servicio
│   ├── service/impl/            # Implementaciones
│   └── dto/                     # DTOs
├── domain/model/
│   ├── entity/                  # Entidades JPA
│   └── enums/                   # Enumeraciones
└── infrastructure/
    ├── persistence/repository/  # Repositorios JPA
    ├── security/                # JWT, filtros
    └── config/                  # SecurityConfig, etc.
```

---

## 9. Checklist rápido

### Solo usar la app (sin tocar backend)

```
☐ Railway → Postgres y SaludLink Online
☐ curl health en URL de Railway
☐ Frontend con npm start (otro repo)
```

### Desarrollar backend local

```
☐ Java 21 + Maven 21 verificados (mvn -v)
☐ Docker Desktop abierto
☐ docker compose up db
☐ mvn spring-boot:run
☐ Frontend .env → http://localhost:8080 (si pruebas integración)
```

### Publicar cambios a producción

```
☐ mvn clean compile (local)
☐ git commit + push
☐ PR + merge en GitHub
☐ Railway deploy exitoso
☐ Health check en producción
```

---

## 10. Comandos de referencia

```powershell
cd C:\CC235-TP-TF-2025-1\SaludLink

# Compilar
mvn clean compile

# Ejecutar (con Postgres local corriendo)
mvn spring-boot:run

# Tests
mvn test

# Solo base de datos (Docker)
docker compose up db

# Backend + BD con Docker (app en contenedor)
docker compose up

# Git
git pull origin develop
git checkout -b feature/nombre
git push origin feature/nombre

# Verificar producción
curl https://saludlink-production.up.railway.app/actuator/health
```

---

## 11. Problemas frecuentes

| Síntoma | Posible causa | Solución |
|---------|---------------|----------|
| `Connection refused` al arrancar | Postgres no corriendo | `docker compose up db` |
| Maven usa Java 17 | `JAVA_HOME` incorrecto | Apuntar a JDK 21; reiniciar terminal/Cursor |
| `BUILD FAILURE` compile | Código o dependencias | `mvn clean compile` y leer el error |
| Deploy Railway falla | Variables BD o JWT faltantes | Revisar Variables en Railway |
| CORS en frontend | Origen no permitido | Revisar `SecurityConfig` / CORS en backend |
| Puerto 8080 ocupado | Otra instancia corriendo | Cerrar proceso o cambiar `SERVER_PORT` |

---

## 12. Docker Compose — qué levanta cada servicio

| Comando | Qué inicia | Cuándo usarlo |
|---------|------------|---------------|
| `docker compose up db` | Solo PostgreSQL en `:5433` | Desarrollo local con `mvn spring-boot:run` |
| `docker compose up` | Postgres + app en contenedor | Probar stack completo containerizado |
| Sin Docker | — | Uso normal con Railway |

---

## 13. Repos relacionados

| Repo | Ruta local | Guía |
|------|------------|------|
| Backend (este) | `SaludLink` | Este documento |
| Frontend | `saludlink-frontend` | `saludlink-frontend/docs/guia-flujo-desarrollo.md` |

---

## Resumen en una frase

**Uso normal:** Railway ya tiene backend y BD — no levantas nada aquí. **Desarrollo Java:** Docker (`db`) + `mvn spring-boot:run`. **Producción:** push a GitHub → Railway redeploya solo.
