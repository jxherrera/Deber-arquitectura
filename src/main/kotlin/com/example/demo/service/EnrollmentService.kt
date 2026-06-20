package com.example.demo.service

import com.example.demo.dto.EnrollmentRequest
import com.example.demo.dto.EnrollmentResponse
import com.example.demo.entity.Enrollment
import com.example.demo.Mappers.EnrollmentMapper
import com.example.demo.repository.*
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val repo: EnrollmentRepository,
    private val sRepo: StudentRepository,
    private val subRepo: SubjectRepository,
    private val mapper: EnrollmentMapper
) {
    fun enroll(req: EnrollmentRequest): EnrollmentResponse {
        val s = sRepo.findById(req.studentId).orElseThrow { com.example.demo.exceptions.StudentNotFoundException("Estudiante no encontrado") }
        val sub = subRepo.findById(req.subjectId).orElseThrow { com.example.demo.exceptions.SubjectNotFoundException("Materia no encontrada") }
        return mapper.toResponse(repo.save(Enrollment(student = s, subject = sub)))
    }

    fun getAll() = repo.findAll().map { mapper.toResponse(it) }
    fun getById(id: Long) = mapper.toResponse(repo.findById(id).orElseThrow { com.example.demo.exceptions.EnrollmentNotFoundException("Inscripción no encontrada") })

    fun updateStatus(id: Long, newStatus: String): EnrollmentResponse {
        val e = repo.findById(id).orElseThrow { com.example.demo.exceptions.EnrollmentNotFoundException("Inscripción no encontrada") }
        e.status = newStatus
        return mapper.toResponse(repo.save(e))
    }

    fun delete(id: Long) {
        if (!repo.existsById(id)) throw com.example.demo.exceptions.EnrollmentNotFoundException("Inscripción no encontrada")
        repo.deleteById(id)
    }
}