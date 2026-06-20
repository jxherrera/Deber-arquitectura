package com.example.demo.exceptions

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Globalexeption {

    @ExceptionHandler(BlankMesaggeException::class)
    fun handleBlankNameException(e: BlankMesaggeException): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "El nombre no puede estar vacío",
            source = "StudentService"
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(e: StudentNotFoundException): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Estudiante no encontrado",
            source = "StudentService"
        )
        return ResponseEntity.status(404).body(response)
    }


    @ExceptionHandler(ProfessorNotFoundException::class)
    fun handleProfessorNotFound(e: ProfessorNotFoundException): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Profesor no encontrado",
            source = "ProfessorService"
        )
        return ResponseEntity.status(404).body(response)
    }

    @ExceptionHandler(SubjectNotFoundException::class)
    fun handleSubjectNotFound(e: SubjectNotFoundException): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Materia no encontrada",
            source = "SubjectService"
        )
        return ResponseEntity.status(404).body(response)
    }

    @ExceptionHandler(EnrollmentNotFoundException::class)
    fun handleEnrollmentNotFound(e: EnrollmentNotFoundException): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Inscripción no encontrada",
            source = "EnrollmentService"
        )
        return ResponseEntity.status(404).body(response)
    }
}

data class ExceptionMessage(
    val message: String,
    val source: String
)