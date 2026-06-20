package com.example.demo.controller

import com.example.demo.dto.EnrollmentRequest
import com.example.demo.service.EnrollmentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(private val service: EnrollmentService) {

    @PostMapping
    fun enroll(@RequestBody request: EnrollmentRequest): ResponseEntity<Any> {
        return ResponseEntity(service.enroll(request), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        return ResponseEntity.ok(service.getAll())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        return ResponseEntity.ok(service.getById(id))
    }

    @PutMapping("/{id}")
    fun updateStatus(@PathVariable id: Long, @RequestBody statusMap: Map<String, String>): ResponseEntity<Any> {
        val newStatus = statusMap["status"] ?: ""
        return ResponseEntity.ok(service.updateStatus(id, newStatus))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}