package com.example.demo.service

import com.example.demo.dto.StudentRequest
import com.example.demo.dto.StudentResponse
import com.example.demo.Mappers.StudentMapper
import com.example.demo.entity.Student
import com.example.demo.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val repository: StudentRepository,
    private val mapper: StudentMapper
) {
    fun create(req: StudentRequest): StudentResponse {
        if (req.name.isBlank()) throw com.example.demo.exceptions.BlankMesaggeException("El nombre no puede estar vacío")
        return mapper.toResponse(repository.save(mapper.toEntity(req)))
    }

    fun getAll() = repository.findAll().map { mapper.toResponse(it) }

    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { com.example.demo.exceptions.StudentNotFoundException("Estudiante no encontrado") })

    fun update(id: Long, req: StudentRequest): StudentResponse {
        if (req.name.isBlank()) throw com.example.demo.exceptions.BlankMesaggeException("El nombre no puede estar vacío")
        val existing = repository.findById(id).orElseThrow { com.example.demo.exceptions.StudentNotFoundException("Estudiante no encontrado") }
        val updated = Student(id = existing.id, name = req.name, email = req.email)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw com.example.demo.exceptions.StudentNotFoundException("Estudiante no encontrado")
        repository.deleteById(id)
    }
}