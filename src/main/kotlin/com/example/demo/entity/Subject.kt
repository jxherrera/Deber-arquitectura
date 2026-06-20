package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "subjects")
class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val code: String,
    @ManyToOne
    @JoinColumn(name = "professor_id")
    val professor: Professor
)