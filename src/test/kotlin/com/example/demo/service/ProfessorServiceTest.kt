package com.example.demo.service

import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.Mappers.ProfessorMapper
import com.example.demo.entity.Professor
import com.example.demo.repository.ProfessorRepository
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.ProfessorNotFoundException
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
class ProfessorServiceTest {

    @Mock
    private lateinit var repository: ProfessorRepository

    @Mock
    private lateinit var mapper: ProfessorMapper

    @InjectMocks
    private lateinit var service: ProfessorService

    @Test
    fun `create lanza excepcion cuando el nombre esta vacio`() {
        val req = ProfessorRequest(name = "   ", email = "test@test.com")
        assertThrows<BlankMesaggeException> { service.create(req) }
    }

    @Test
    fun `create retorna respuesta cuando es valido`() {
        val req = ProfessorRequest(name = "Prof", email = "p@p.com")
        val entity = Professor(name = "Prof", email = "p@p.com")
        val savedEntity = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val response = ProfessorResponse(id = 1L, name = "Prof", email = "p@p.com")

        `when`(mapper.toEntity(req)).thenReturn(entity)
        `when`(repository.save(entity)).thenReturn(savedEntity)
        `when`(mapper.toResponse(savedEntity)).thenReturn(response)

        val result = service.create(req)
        assertEquals(1L, result.id)
    }

    @Test
    fun `getAll retorna lista de respuestas`() {
        val entity = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val response = ProfessorResponse(id = 1L, name = "Prof", email = "p@p.com")
        `when`(repository.findAll()).thenReturn(listOf(entity))
        `when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getAll()
        assertEquals(1, result.size)
    }

    @Test
    fun `getById retorna respuesta cuando existe`() {
        val entity = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val response = ProfessorResponse(id = 1L, name = "Prof", email = "p@p.com")
        `when`(repository.findById(1L)).thenReturn(Optional.of(entity))
        `when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getById(1L)
        assertEquals("Prof", result.name)
    }

    @Test
    fun `getById lanza excepcion cuando no existe`() {
        `when`(repository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<ProfessorNotFoundException> { service.getById(99L) }
    }

    @Test
    fun `update lanza excepcion cuando el nombre esta vacio`() {
        val req = ProfessorRequest(name = "", email = "p@p.com")
        assertThrows<BlankMesaggeException> { service.update(1L, req) }
    }

    @Test
    fun `update lanza excepcion cuando no existe`() {
        val req = ProfessorRequest(name = "Prof", email = "p@p.com")
        `when`(repository.findById(1L)).thenReturn(Optional.empty())
        assertThrows<ProfessorNotFoundException> { service.update(1L, req) }
    }

    @Test
    fun `update retorna respuesta cuando es valido`() {
        val req = ProfessorRequest(name = "Prof Mod", email = "test@test.com")
        val existing = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val updated = Professor(id = 1L, name = "Prof Mod", email = "test@test.com")
        val response = ProfessorResponse(id = 1L, name = "Prof Mod", email = "test@test.com")

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(repository.save(any(Professor::class.java))).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        val result = service.update(1L, req)
        assertEquals("Prof Mod", result.name)
    }

    @Test
    fun `delete lanza excepcion cuando no existe`() {
        `when`(repository.existsById(99L)).thenReturn(false)
        assertThrows<ProfessorNotFoundException> { service.delete(99L) }
    }

    @Test
    fun `delete elimina cuando existe`() {
        `when`(repository.existsById(1L)).thenReturn(true)
        service.delete(1L)
        verify(repository, times(1)).deleteById(1L)
    }
}
