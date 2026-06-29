package com.example.demo.service

import com.example.demo.dto.StudentRequest
import com.example.demo.dto.StudentResponse
import com.example.demo.Mappers.StudentMapper
import com.example.demo.entity.Student
import com.example.demo.repository.StudentRepository
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.StudentNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock
    private lateinit var repository: StudentRepository

    @Mock
    private lateinit var mapper: StudentMapper

    @InjectMocks
    private lateinit var service: StudentService

    @Test
    fun `create lanza BlankMesaggeException cuando el nombre esta vacio`() {
        val req = StudentRequest(name = "   ", email = "test@test.com")
        assertThrows<BlankMesaggeException> { service.create(req) }
    }

    @Test
    fun `create retorna respuesta cuando el nombre es valido`() {
        val req = StudentRequest(name = "Ana Lopez", email = "ana@puce.edu")
        val entity = Student(name = "Ana Lopez", email = "ana@puce.edu")
        val savedEntity = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val response = StudentResponse(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")

        `when`(mapper.toEntity(req)).thenReturn(entity)
        `when`(repository.save(entity)).thenReturn(savedEntity)
        `when`(mapper.toResponse(savedEntity)).thenReturn(response)

        val result = service.create(req)
        assertEquals(1L, result.id)
    }

    @Test
    fun `getAll retorna lista de respuestas`() {
        val entity = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val response = StudentResponse(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        `when`(repository.findAll()).thenReturn(listOf(entity))
        `when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getAll()
        assertEquals(1, result.size)
        assertEquals("Ana Lopez", result[0].name)
    }

    @Test
    fun `getById retorna respuesta cuando existe`() {
        val entity = Student(id = 1L, name = "Ana", email = "a@a.com")
        val response = StudentResponse(id = 1L, name = "Ana", email = "a@a.com")
        `when`(repository.findById(1L)).thenReturn(Optional.of(entity))
        `when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getById(1L)
        assertEquals("Ana", result.name)
    }

    @Test
    fun `getById lanza excepcion cuando no existe`() {
        `when`(repository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<StudentNotFoundException> { service.getById(99L) }
    }

    @Test
    fun `update lanza excepcion cuando el nombre esta vacio`() {
        val req = StudentRequest(name = " ", email = "test@test.com")
        assertThrows<BlankMesaggeException> { service.update(1L, req) }
    }

    @Test
    fun `update lanza excepcion cuando no existe`() {
        val req = StudentRequest(name = "Ana", email = "test@test.com")
        `when`(repository.findById(1L)).thenReturn(Optional.empty())
        assertThrows<StudentNotFoundException> { service.update(1L, req) }
    }

    @Test
    fun `update retorna respuesta cuando es valido`() {
        val req = StudentRequest(name = "Ana Mod", email = "test@test.com")
        val existing = Student(id = 1L, name = "Ana", email = "a@a.com")
        val updated = Student(id = 1L, name = "Ana Mod", email = "test@test.com")
        val response = StudentResponse(id = 1L, name = "Ana Mod", email = "test@test.com")

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(repository.save(any(Student::class.java))).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        val result = service.update(1L, req)
        assertEquals("Ana Mod", result.name)
    }

    @Test
    fun `delete lanza excepcion cuando no existe`() {
        `when`(repository.existsById(99L)).thenReturn(false)
        assertThrows<StudentNotFoundException> { service.delete(99L) }
    }

    @Test
    fun `delete elimina cuando existe`() {
        `when`(repository.existsById(1L)).thenReturn(true)
        service.delete(1L)
        verify(repository, times(1)).deleteById(1L)
    }
}
