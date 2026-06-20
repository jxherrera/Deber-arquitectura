package com.example.demo.dto

data class EnrollmentResponse(
    val id: Long,
    val status: String,
    val createdAt: String,
    val student: StudentResponse,
    val subject: SubjectResponse
)