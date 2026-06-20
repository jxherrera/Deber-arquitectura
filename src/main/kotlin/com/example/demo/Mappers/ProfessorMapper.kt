package com.example.demo.Mappers

import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.entity.Professor
import org.springframework.stereotype.Component

@Component
class ProfessorMapper {
    fun toEntity(req: ProfessorRequest) = Professor(name = req.name, email = req.email)

    fun toResponse(prof: Professor) = ProfessorResponse(
        id = prof.id,
        name = prof.name,
        email = prof.email
    )
}