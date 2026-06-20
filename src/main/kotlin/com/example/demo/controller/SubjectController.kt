package com.example.demo.controller

import com.example.demo.dto.SubjectRequest
import com.example.demo.service.SubjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(private val service: SubjectService) {

    @PostMapping
    fun create(@RequestBody req: SubjectRequest) =
        ResponseEntity(service.create(req), HttpStatus.CREATED)

    @GetMapping
    fun getAll() =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        ResponseEntity.ok(service.getById(id))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: SubjectRequest) =
        ResponseEntity.ok(service.update(id, req))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}