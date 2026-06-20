package com.example.demo.service

import com.example.demo.dto.SubjectRequest
import com.example.demo.dto.SubjectResponse
import com.example.demo.Mappers.SubjectMapper
import com.example.demo.entity.Subject
import com.example.demo.repository.ProfessorRepository
import com.example.demo.repository.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val repository: SubjectRepository,
    private val profRepository: ProfessorRepository,
    private val mapper: SubjectMapper
) {
    fun create(req: SubjectRequest): SubjectResponse {
        if (req.name.isBlank() || req.code.isBlank()) throw com.example.demo.exceptions.BlankMesaggeException("Nombre y código no pueden estar vacíos")
        val prof = profRepository.findById(req.professorId).orElseThrow { com.example.demo.exceptions.ProfessorNotFoundException("Profesor no encontrado") }
        return mapper.toResponse(repository.save(mapper.toEntity(req, prof)))
    }

    fun getAll() = repository.findAll().map { mapper.toResponse(it) }

    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { com.example.demo.exceptions.SubjectNotFoundException("Materia no encontrada") })

    fun update(id: Long, req: SubjectRequest): SubjectResponse {
        if (req.name.isBlank() || req.code.isBlank()) throw com.example.demo.exceptions.BlankMesaggeException("Nombre y código no pueden estar vacíos")
        val existing = repository.findById(id).orElseThrow { com.example.demo.exceptions.SubjectNotFoundException("Materia no encontrada") }
        val prof = profRepository.findById(req.professorId).orElseThrow { com.example.demo.exceptions.ProfessorNotFoundException("Profesor no encontrado") }

        
        val updated = Subject(id = existing.id, name = req.name, code = req.code, professor = prof)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw com.example.demo.exceptions.SubjectNotFoundException("Materia no encontrada")
        repository.deleteById(id)
    }
}