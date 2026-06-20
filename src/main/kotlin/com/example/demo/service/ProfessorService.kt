package com.example.demo.service

import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.Mappers.ProfessorMapper
import com.example.demo.entity.Professor
import com.example.demo.repository.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val repository: ProfessorRepository,
    private val mapper: ProfessorMapper
) {
    fun create(req: ProfessorRequest): ProfessorResponse {
        if (req.name.isBlank()) throw com.example.demo.exceptions.BlankMesaggeException("El nombre no puede estar vacío")
        return mapper.toResponse(repository.save(mapper.toEntity(req)))
    }

    fun getAll() = repository.findAll().map { mapper.toResponse(it) }

    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { com.example.demo.exceptions.ProfessorNotFoundException("Profesor no encontrado") })

    fun update(id: Long, req: ProfessorRequest): ProfessorResponse {
        if (req.name.isBlank()) throw com.example.demo.exceptions.BlankMesaggeException("El nombre no puede estar vacío")
        val existing = repository.findById(id).orElseThrow { com.example.demo.exceptions.ProfessorNotFoundException("Profesor no encontrado") }
        val updated = Professor(id = existing.id, name = req.name, email = req.email)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw com.example.demo.exceptions.ProfessorNotFoundException("Profesor no encontrado")
        repository.deleteById(id)
    }
}