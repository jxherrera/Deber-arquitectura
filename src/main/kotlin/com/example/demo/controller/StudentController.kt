package com.example.demo.controller

import com.example.demo.dto.StudentRequest
import com.example.demo.service.StudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(private val service: StudentService) {

    @PostMapping
    fun create(@RequestBody request: StudentRequest): ResponseEntity<Any> {
        return ResponseEntity(service.create(request), HttpStatus.CREATED)
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
    fun update(@PathVariable id: Long, @RequestBody request: StudentRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(service.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}