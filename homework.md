# Tarea: CRUD completo para todas las entidades

## Objetivo

Completar la implementación de la API REST del proyecto `students` añadiendo los endpoints faltantes para lograr un CRUD completo (4 endpoints por entidad) en las 4 entidades del sistema.

El proyecto ya cuenta con una implementación parcial de `Student` como referencia. Debes seguir el mismo patrón de capas: **Entity → DTO → Repository → Mapper → Service → Controller**.

---

## Entidades del sistema

| Entidad      | Tabla         | Relaciones                          |
|--------------|---------------|-------------------------------------|
| `Student`    | `students`    | —                                   |
| `Professor`  | `professors`  | tiene muchas `Subject`              |
| `Subject`    | `subjects`    | pertenece a un `Professor`          |
| `Enrollment` | `enrollments` | pertenece a un `Student` y `Subject`|

---

## Endpoints requeridos (16 en total)

### Student — `/api/students`

| # | Verbo    | Ruta                  | Descripción                          | HTTP Status |
|---|----------|-----------------------|--------------------------------------|-------------|
| 1 | `POST`   | `/api/students`       | Crear un estudiante *(ya existe)*    | 201         |
| 2 | `GET`    | `/api/students`       | Listar todos los estudiantes *(ya existe)* | 200   |
| 3 | `GET`    | `/api/students/{id}`  | Obtener un estudiante por ID *(ya existe)* | 200  |
| 4 | `PUT`    | `/api/students/{id}`  | Actualizar nombre y/o email          | 200         |
| 5 | `DELETE` | `/api/students/{id}`  | Eliminar un estudiante               | 204         |

> Los endpoints 1, 2 y 3 ya están implementados. Debes agregar el **PUT** y el **DELETE**.

---

### Professor — `/api/professors`

| # | Verbo    | Ruta                    | Descripción                       | HTTP Status |
|---|----------|-------------------------|-----------------------------------|-------------|
| 6 | `POST`   | `/api/professors`       | Crear un profesor                 | 201         |
| 7 | `GET`    | `/api/professors`       | Listar todos los profesores       | 200         |
| 8 | `GET`    | `/api/professors/{id}`  | Obtener un profesor por ID        | 200         |
| 9 | `PUT`    | `/api/professors/{id}`  | Actualizar nombre y/o email       | 200         |
| 10| `DELETE` | `/api/professors/{id}`  | Eliminar un profesor              | 204         |

---

### Subject — `/api/subjects`

| # | Verbo    | Ruta                   | Descripción                                   | HTTP Status |
|---|----------|------------------------|-----------------------------------------------|-------------|
| 11| `POST`   | `/api/subjects`        | Crear una materia (requiere `professorId`) *(ya existe en Service)*  | 201 |
| 12| `GET`    | `/api/subjects`        | Listar todas las materias                     | 200         |
| 13| `GET`    | `/api/subjects/{id}`   | Obtener una materia por ID                    | 200         |
| 14| `PUT`    | `/api/subjects/{id}`   | Actualizar nombre, código o profesor asignado | 200         |
| 15| `DELETE` | `/api/subjects/{id}`   | Eliminar una materia                          | 204         |

---

### Enrollment — `/api/enrollments`

| # | Verbo    | Ruta                      | Descripción                           | HTTP Status |
|---|----------|---------------------------|---------------------------------------|-------------|
| 16| `POST`   | `/api/enrollments`        | Inscribir un estudiante en una materia| 201         |
| 17| `GET`    | `/api/enrollments`        | Listar todas las inscripciones        | 200         |
| 18| `GET`    | `/api/enrollments/{id}`   | Obtener una inscripción por ID        | 200         |
| 19| `PUT`    | `/api/enrollments/{id}`   | Actualizar el estado (`status`)       | 200         |
| 20| `DELETE` | `/api/enrollments/{id}`   | Eliminar una inscripción              | 204         |

---

## Estructura de requests y responses esperados

### Student

```json
// POST /api/students  — StudentRequest
{ "name": "Ana Torres", "email": "ana@puce.edu.ec" }

// PUT /api/students/{id}  — StudentRequest
{ "name": "Ana Torres Ruiz", "email": "ana.torres@puce.edu.ec" }

// StudentResponse
{ "id": 1, "name": "Ana Torres", "email": "ana@puce.edu.ec" }
```

### Professor

```json
// POST /api/professors  — ProfessorRequest
{ "name": "Dr. García", "email": "garcia@puce.edu.ec" }

// ProfessorResponse
{ "id": 1, "name": "Dr. García", "email": "garcia@puce.edu.ec" }
```

### Subject

```json
// POST /api/subjects  — SubjectRequest
{ "name": "Arquitectura Empresarial", "code": "AE-101", "professorId": 1 }

// SubjectResponse
{
  "id": 1,
  "name": "Arquitectura Empresarial",
  "code": "AE-101",
  "professor": { "id": 1, "name": "Dr. García", "email": "garcia@puce.edu.ec" }
}
```

### Enrollment

```json
// POST /api/enrollments  — EnrollmentRequest
{ "studentId": 1, "subjectId": 1 }

// PUT /api/enrollments/{id}  — solo el campo status
{ "status": "APROBADO" }

// EnrollmentResponse
{
  "id": 1,
  "createdAt": "2026-06-15T10:00:00",
  "status": "INSCRITO",
  "student": { "id": 1, "name": "Ana Torres", "email": "ana@puce.edu.ec" },
  "subject": {
    "id": 1,
    "name": "Arquitectura Empresarial",
    "code": "AE-101",
    "professor": { "id": 1, "name": "Dr. García", "email": "garcia@puce.edu.ec" }
  }
}
```

---

## Configuración de base de datos PostgreSQL

El proyecto viene configurado con H2 (base de datos en memoria). Debes cambiar la configuración para apuntar a una base de datos **PostgreSQL** local.

### 1. Levantar PostgreSQL con Docker

La forma recomendada es usar Docker, sin instalar PostgreSQL localmente. Crea un archivo `docker-compose.yml` en la raíz del proyecto con el siguiente contenido:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: studentsdb
    environment:
      POSTGRES_DB: studentsdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - students_data:/var/lib/postgresql/data

volumes:
  students_data:
```

Luego ejecuta:

```bash
docker compose up -d
```

Para detenerlo sin borrar los datos:

```bash
docker compose stop
```

Para detenerlo y eliminar los datos:

```bash
docker compose down -v
```

### 2. Reemplazar `application.yaml`

Edita el archivo `src/main/resources/application.yaml` con el siguiente contenido:

```yaml
spring:
  application:
    name: students

  datasource:
    url: jdbc:postgresql://localhost:5432/studentsdb
    driver-class-name: org.postgresql.Driver
    username: postgres       # debe coincidir con POSTGRES_USER del docker-compose.yml
    password: postgres       # debe coincidir con POSTGRES_PASSWORD del docker-compose.yml

  jpa:
    hibernate:
      ddl-auto: update       # crea/actualiza las tablas automáticamente al arrancar
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8787
```

> `ddl-auto: update` hace que Hibernate cree o actualice las tablas automáticamente al iniciar la aplicación. No se usan migraciones.

### 3. Agregar la dependencia de PostgreSQL

En `build.gradle.kts`, reemplaza la dependencia de H2 por el driver de PostgreSQL:

```kotlin
// elimina o comenta esta línea:
// runtimeOnly("com.h2database:h2")

// agrega esta:
runtimeOnly("org.postgresql:postgresql")
```

### 4. Verificar que la aplicación levanta

```bash
./gradlew bootRun
```

Si la conexión es exitosa verás en los logs:

```
HikariPool-1 - Start completed.
```

Y Hibernate creará automáticamente las tablas `students`, `professors`, `subjects` y `enrollments` en la base de datos.

---

## Capas que debes implementar por entidad

Sigue exactamente el mismo patrón que ya existe para `Student`:

```
Entity      → ya existe (no modificar)
DTO         → Request + Response  (ya existen los data classes, revisa si necesitas ajustar)
Repository  → interfaz que extiende JpaRepository
Mapper      → funciones de extensión toEntity() y toResponse()
Service     → lógica de negocio (validaciones, búsqueda de dependencias)
Controller  → anotaciones @RestController, @GetMapping, @PostMapping, etc.
```

---

## Manejo de errores obligatorio

Cada endpoint que reciba un `{id}` debe lanzar una excepción personalizada si el recurso no existe. Sigue el patrón de `StudentNotFoundException` y `GlobalExceptionHandler`:

| Entidad      | Excepción a crear (si no existe) |
|--------------|----------------------------------|
| `Professor`  | `ProfessorNotFound` *(ya existe)*|
| `Subject`    | `SubjectNotFound` *(ya existe)*  |
| `Student`    | `StudentNotFoundException` *(ya existe)* |
| `Enrollment` | `EnrollmentNotFound` *(debes crear)* |

El `GlobalExceptionHandler` ya maneja `StudentNotFoundException` con un `404`. Registra las nuevas excepciones de la misma forma.

---

## Validaciones mínimas

- `name` no puede estar en blanco en `Student`, `Professor` ni `Subject`.
- `code` no puede estar en blanco en `Subject`.
- `professorId` debe corresponder a un `Professor` existente al crear o actualizar una `Subject`.
- `studentId` y `subjectId` deben existir al crear un `Enrollment`.
- El `status` inicial de un `Enrollment` al crearse debe ser `"INSCRITO"`.

---

## Criterios de evaluación

| Criterio                                                        | Puntos |
|-----------------------------------------------------------------|--------|
| Los 16 endpoints responden con el HTTP status correcto          | 3      |
| Requests y responses siguen los DTOs definidos                  | 2      |
| Manejo de errores con excepciones personalizadas y 404          | 2      |
| Validaciones de negocio implementadas                           | 1      |
| `docker-compose.yml` y `application.yaml` apuntando a PostgreSQL| 1      |
| Colección de Postman con los 16 endpoints subida al repositorio | 1      |
| **Total**                                                       | **10** |

---

## Entrega

- Sube tu código a un repositorio de GitHub.
- El proyecto debe compilar y ejecutarse con `./gradlew bootRun` sin errores.
- Exporta y sube al repositorio una colección de Postman (`*.json`) con los **16 endpoints** correctamente configurados (URL, método, body de ejemplo donde aplique).

**Fecha de entrega:** a confirmar por el profesor.
