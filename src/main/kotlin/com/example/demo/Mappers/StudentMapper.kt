package com.example.demo.Mappers

import com.example.demo.dto.StudentRequest
import com.example.demo.dto.StudentResponse
import com.example.demo.entity.Student
import org.springframework.stereotype.Component

@Component
class StudentMapper {
    fun toEntity(req: StudentRequest) = Student(name = req.name, email = req.email)

    fun toResponse(s: Student) = StudentResponse(
        id = s.id,
        name = s.name,
        email = s.email
    )
}