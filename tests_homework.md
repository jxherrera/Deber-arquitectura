# Tarea: 100% de cobertura (coverage) en la capa de Services

## Objetivo

Cada **Service** del proyecto debe contar con pruebas unitarias que alcancen el **100% de cobertura** (líneas y ramas). Esto implica que **todas las líneas y todos los caminos posibles** de ejecución (el camino feliz y cada `throw`/`if`/`else`) sean ejercitados por al menos una prueba.

La capa de Services es donde reside la **lógica de negocio**, razón por la cual es la que más nos interesa probar. En esta tarea no se solicita cobertura de controllers, mappers ni entities: **únicamente la capa `services`**.

---

## Qué debe entregar

1. Un archivo de test por cada service dentro de `src/test/kotlin/com/pucetec/students/services/`:
   - `StudentServiceTest.kt` (ya está resuelto; utilícelo como modelo de referencia)
   - `SubjectServiceTest.kt`
   - `ProfessorServiceTest.kt`
   - `EnrollmentServiceTest.kt`
2. **100% de cobertura** en el paquete `com.pucetec.students.services`, demostrado mediante **Run with Coverage** de IntelliJ.
3. Una **captura de pantalla** del reporte de cobertura de IntelliJ donde se observe el paquete `services` en **100%** (líneas y ramas).
4. El **link del repositorio** (GitHub) con todo el código y los tests subidos. Sin el link, la tarea no se evalúa.

### Criterios de evaluación

| Criterio | Puntos |
|---|---|
| Todos los services tienen su archivo de test | 30 |
| Cobertura de **líneas** = 100% en `services` | 25 |
| Cobertura de **ramas (branches)** = 100% en `services` | 25 |
| Tests con nombres claros y patrón Arrange/Act/Assert | 10 |
| Captura del reporte de cobertura en 100% | 10 |

Nota: un test que **no afirma nada** (sin `assertEquals` / `assertThrows`) puede aparecer como cubierto en el reporte, pero **no prueba nada**. Cada test debe contener al menos una aserción.

---

## Qué significa "100% de coverage"

Existen dos métricas que debe observar:

- **Cobertura de líneas:** porcentaje de las líneas de código que se ejecutaron al correr los tests.
- **Cobertura de ramas (branches):** porcentaje de los caminos de decisión que se ejecutaron. Un `if (x) ... else ...` tiene **dos ramas**; se requiere una prueba para cada una.

Alcanzar el 100% de líneas es relativamente sencillo. Lo exigente, y lo verdaderamente importante, es el 100% de **ramas**: por cada `if`, `else`, `orElseThrow { }`, `when`, operador `?:`, etc., se necesita una prueba que ingrese por **cada** alternativa.

---

## Ejemplo guiado (basado en `StudentService`)

### 1. El código a probar

```kotlin
// src/main/kotlin/com/pucetec/students/services/StudentService.kt
fun createStudent(request: StudentRequest): StudentResponse {
    logger.info("Creating student ${request.name}")

    if (request.name.isBlank()) {
        throw BlankNameException("Name cannot be blank")   // RAMA 1 (nombre vacío)
    } else {
        val studentEntity = request.toEntity()
        val savedStudent = studentRepository.save(studentEntity)
        return savedStudent.toResponse()                   // RAMA 2 (nombre válido)
    }
}
```

Este método tiene **dos ramas**. Para alcanzar el 100% se requieren **como mínimo dos tests**: uno con nombre válido y otro con nombre en blanco.

### 2. Test incompleto (deja una rama sin cubrir)

```kotlin
@Test
fun `createStudent retorna respuesta cuando el nombre es valido`() {
    val request = StudentRequest(name = "Ana Lopez", email = "ana@puce.edu")
    val savedStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")

    `when`(studentRepository.save(any(Student::class.java))).thenReturn(savedStudent)

    val response = studentService.createStudent(request)

    assertEquals(1L, response.id)
    assertEquals("Ana Lopez", response.name)
}
```

Si ejecuta **únicamente** este test con cobertura, la línea del `throw BlankNameException(...)` queda **sin cubrir**: nunca se ingresó por la rama del nombre vacío. La cobertura de ramas será del **50%**.

### 3. Test completo (100%)

Se agrega un test para la rama que faltaba:

```kotlin
@Test
fun `createStudent lanza BlankNameException cuando el nombre esta vacio`() {
    val request = StudentRequest(name = "", email = "vacio@puce.edu")

    // assertThrows verifica que el bloque lance EXACTAMENTE esa excepción
    assertThrows<BlankNameException> {
        studentService.createStudent(request)
    }
}
```

Con ambos tests, las dos ramas del `if/else` se ejecutan, la línea del `throw` queda cubierta y se obtiene el **100% de ramas** en ese método.

Regla práctica: **cuente los `if`, `else`, `orElseThrow`, `when`, `?:` y `try/catch` de su service**. Por cada uno necesita un test que ingrese por cada alternativa. Revise `StudentServiceTest.kt`, ya entregado: contiene un test por cada rama de cada método.

### Estructura mínima de un test de service (plantilla)

```kotlin
@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    @Mock private lateinit var subjectRepository: SubjectRepository
    @Mock private lateinit var professorRepository: ProfessorRepository

    @InjectMocks private lateinit var subjectService: SubjectService

    @Test
    fun `getSubjectById retorna la materia cuando existe`() {
        // Arrange
        // ...stub de los mocks con `when`(...).thenReturn(...)
        // Act
        // ...llamar al método real del service
        // Assert
        // ...assertEquals / assertThrows
    }

    // Recuerde el camino de error:
    @Test
    fun `getSubjectById lanza SubjectNotFoundException cuando no existe`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<SubjectNotFoundException> { subjectService.getSubjectById(99L) }
    }
}
```

---

## Cómo usar "Run with Coverage" en IntelliJ IDEA

### Paso 1 — Ejecutar los tests con cobertura

Dispone de tres formas equivalentes:

- **Toda la carpeta de tests:** clic derecho sobre `src/test/kotlin/com/pucetec/students/services`, luego **More Run/Debug** y **Run 'Tests in services...' with Coverage**.
- **Una sola clase de test:** abra el archivo (por ejemplo `SubjectServiceTest.kt`), haga clic en la flecha verde junto al nombre de la clase y seleccione **Run '...' with Coverage**.
- **Con el ícono de la barra superior:** seleccione la configuración del test y haga clic en el ícono de **cobertura (el del escudo)**, ubicado junto a Run y Debug.

Tenga presente: es el botón **con el escudo**, no el de ejecución normal. Si ejecuta con el botón de ejecución normal **no** verá la información de cobertura.

### Paso 2 — Leer la información en el editor

Al finalizar, en el **margen izquierdo** del código del **service** (no del test) aparecen marcas de color por línea:

| Marca | Significado |
|---|---|
| **Verde** | La línea fue ejecutada por algún test. |
| **Rojo** | La línea **nunca** se ejecutó. Falta un test que pase por ahí. |
| **Amarillo** | Rama **parcialmente** cubierta: el `if` se ejecutó solo por un lado (falta el otro `true`/`false`). |

Su meta es que todo el código de los services quede en verde, sin marcas rojas ni amarillas.

### Paso 3 — Ver el porcentaje exacto por paquete y clase

Abra la ventana **Coverage** (menú **View → Tool Windows → Coverage**, o aparece automáticamente al finalizar). Allí encontrará una tabla:

```
Element                      Class %   Method %   Line %   Branch %
com.pucetec.students.services  4/4      18/18      96%      88%    (aún NO es 100%)
  StudentService              100%      100%      100%     100%    (correcto)
  SubjectService              100%       80%       90%      75%    (faltan ramas)
```

- **Line %** debe ser **100%**.
- **Branch %** (en ocasiones rotulado "Branch" o "Cobertura de ramas") debe ser **100%**. Esta es la métrica más exigente.
- Doble clic en una clase para saltar al código y localizar exactamente la línea sin cubrir.

### Paso 4 — Iterar hasta el 100%

1. Identifique la línea que quedó **roja o amarilla**.
2. Pregúntese: ¿qué condición provoca que esa línea se ejecute? (por ejemplo, `Optional.empty()` para ingresar al `orElseThrow`).
3. Escriba el test que cumple esa condición.
4. Vuelva a ejecutar **with Coverage** y repita hasta que el paquete `services` quede en **100% / 100%**.

Sugerencia: para observar el porcentaje **solo de services**, ejecute la cobertura sobre la carpeta `services` (Paso 1, primera opción). De este modo el reporte no se mezcla con controllers ni mappers.

---

## Errores comunes y cómo evitarlos

- **"Obtengo 100% de líneas pero 80% de ramas".** Falta el camino de error. Por cada `orElseThrow { ... }` se necesita un test con `Optional.empty()` que dispare la excepción.
- **El test pasa pero no afirma nada.** Que aparezca cubierto no significa que el test sea correcto. Incluya siempre `assertEquals` o `assertThrows`.
- **`UnnecessaryStubbingException` de Mockito.** Está definiendo `when(...).thenReturn(...)` para algo que ese test no utiliza. Defina únicamente los stubs que el camino del test realmente invoca.
- **Ejecuté con el botón normal y no veo la información de cobertura.** Utilice el botón **with Coverage** (el del escudo), no el de ejecución normal.
- **Estoy probando el controller en lugar del service.** Esta tarea corresponde a **services**. Simule (mock) los repositorios e instancie el service con `@InjectMocks`.

---

## Lista de verificación antes de entregar

- [x] Existe un archivo de test para cada service en `services/`.
- [x] Cada `if`, `else`, `orElseThrow`, `when` y `?:` tiene un test que ingresa por **cada** rama.
- [x] Cada test tiene al menos una aserción (`assertEquals` / `assertThrows` / `assertNull`...).
- [x] **Run with Coverage** sobre la carpeta `services` muestra **Line 100%** y **Branch 100%**.
- [x] No hay líneas rojas ni amarillas en el código de los services.
- [ ] Adjunté la captura del reporte de cobertura en 100%.
- [ ] Incluí el link del repositorio con el código y los tests subidos.
