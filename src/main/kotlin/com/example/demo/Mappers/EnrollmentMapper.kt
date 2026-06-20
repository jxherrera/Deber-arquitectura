package com.example.demo.Mappers

import com.example.demo.dto.EnrollmentResponse
import com.example.demo.entity.Enrollment
import com.example.demo.entity.Student
import com.example.demo.entity.Subject
import org.springframework.stereotype.Component

@Component
class EnrollmentMapper(
    private val studentMapper: StudentMapper,
    private val subjectMapper: SubjectMapper
) {
    fun toResponse(e: Enrollment) = EnrollmentResponse(
        id = e.id,
        status = e.status,
        createdAt = e.createdAt,
        student = studentMapper.toResponse(e.student),
        subject = subjectMapper.toResponse(e.subject)
    )
}