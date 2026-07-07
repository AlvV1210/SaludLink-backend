-- Limpieza de CHECK constraints del esquema legacy en desarrollo local.
-- Hibernate valida enums en Java; estos constraints viejos usan valores distintos
-- (ej. PRESENCIAL/VIRTUAL, PENDIENTE/CONFIRMADA) y bloquean IN_PERSON/SCHEDULED.
--
-- Ejecutar en pgAdmin conectado a saludlink_db, o:
-- docker exec -i saludlink_db psql -U postgres -d saludlink_db -f - < docs/local-db-legacy-constraints.sql

-- Ver constraints actuales en appointments
SELECT conname, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = 'appointments'::regclass
  AND contype = 'c';

-- Eliminar constraints legacy conocidos
ALTER TABLE appointments DROP CONSTRAINT IF EXISTS appointments_modality_check;
ALTER TABLE appointments DROP CONSTRAINT IF EXISTS appointments_status_check;

-- Columna users que a veces falta tras migraciones parciales
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER NOT NULL DEFAULT 0;
