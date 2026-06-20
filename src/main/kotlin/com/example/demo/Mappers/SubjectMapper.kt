package com.example.demo.Mappers

import com.example.demo.dto.SubjectRequest
import com.example.demo.dto.SubjectResponse
import com.example.demo.entity.Professor
import com.example.demo.entity.Subject
import org.springframework.stereotype.Component

@Component
class SubjectMapper(private val professorMapper: ProfessorMapper) {

    fun toEntity(req: SubjectRequest, prof: Professor) = Subject(
        name = req.name,
        code = req.code,
        professor = prof
    )

    fun toResponse(sub: Subject) = SubjectResponse(
        sub.id,
        sub.name,
        sub.code,
        professorMapper.toResponse(sub.professor)
    )
}