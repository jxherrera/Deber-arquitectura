package com.example.demo.service

import com.example.demo.dto.SubjectRequest
import com.example.demo.dto.SubjectResponse
import com.example.demo.dto.ProfessorResponse
import com.example.demo.Mappers.SubjectMapper
import com.example.demo.entity.Professor
import com.example.demo.entity.Subject
import com.example.demo.repository.ProfessorRepository
import com.example.demo.repository.SubjectRepository
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
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
class SubjectServiceTest {

    @Mock
    private lateinit var repository: SubjectRepository

    @Mock
    private lateinit var profRepository: ProfessorRepository

    @Mock
    private lateinit var mapper: SubjectMapper

    @InjectMocks
    private lateinit var service: SubjectService

    @Test
    fun `create lanza excepcion cuando nombre esta vacio`() {
        val req = SubjectRequest(name = "   ", code = "123", professorId = 1L)
        assertThrows<BlankMesaggeException> { service.create(req) }
    }

    @Test
    fun `create lanza excepcion cuando codigo esta vacio`() {
        val req = SubjectRequest(name = "Math", code = "", professorId = 1L)
        assertThrows<BlankMesaggeException> { service.create(req) }
    }

    @Test
    fun `create lanza excepcion cuando profesor no existe`() {
        val req = SubjectRequest(name = "Math", code = "123", professorId = 99L)
        `when`(profRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<ProfessorNotFoundException> { service.create(req) }
    }

    @Test
    fun `create retorna respuesta cuando es valido`() {
        val req = SubjectRequest(name = "Math", code = "123", professorId = 1L)
        val prof = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val entity = Subject(name = "Math", code = "123", professor = prof)
        val savedEntity = Subject(id = 1L, name = "Math", code = "123", professor = prof)
        val response = SubjectResponse(id = 1L, name = "Math", code = "123", professor = ProfessorResponse(id = 1L, name = "Prof", email = "p@p.com"))

        `when`(profRepository.findById(1L)).thenReturn(Optional.of(prof))
        `when`(mapper.toEntity(req, prof)).thenReturn(entity)
        `when`(repository.save(entity)).thenReturn(savedEntity)
        `when`(mapper.toResponse(savedEntity)).thenReturn(response)

        val result = service.create(req)
        assertEquals(1L, result.id)
    }

    @Test
    fun `getAll retorna lista de respuestas`() {
        val prof = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val entity = Subject(id = 1L, name = "Math", code = "123", professor = prof)
        val response = SubjectResponse(id = 1L, name = "Math", code = "123", professor = ProfessorResponse(id = 1L, name = "Prof", email = "p@p.com"))
        `when`(repository.findAll()).thenReturn(listOf(entity))
        `when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getAll()
        assertEquals(1, result.size)
    }

    @Test
    fun `getById retorna respuesta cuando existe`() {
        val prof = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val entity = Subject(id = 1L, name = "Math", code = "123", professor = prof)
        val response = SubjectResponse(id = 1L, name = "Math", code = "123", professor = ProfessorResponse(id = 1L, name = "Prof", email = "p@p.com"))
        `when`(repository.findById(1L)).thenReturn(Optional.of(entity))
        `when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getById(1L)
        assertEquals("Math", result.name)
    }

    @Test
    fun `getById lanza excepcion cuando no existe`() {
        `when`(repository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<SubjectNotFoundException> { service.getById(99L) }
    }

    @Test
    fun `update lanza excepcion cuando nombre esta vacio`() {
        val req = SubjectRequest(name = "", code = "123", professorId = 1L)
        assertThrows<BlankMesaggeException> { service.update(1L, req) }
    }

    @Test
    fun `update lanza excepcion cuando codigo esta vacio`() {
        val req = SubjectRequest(name = "Math", code = "  ", professorId = 1L)
        assertThrows<BlankMesaggeException> { service.update(1L, req) }
    }

    @Test
    fun `update lanza excepcion cuando materia no existe`() {
        val req = SubjectRequest(name = "Math", code = "123", professorId = 1L)
        `when`(repository.findById(1L)).thenReturn(Optional.empty())
        assertThrows<SubjectNotFoundException> { service.update(1L, req) }
    }
    
    @Test
    fun `update lanza excepcion cuando profesor no existe`() {
        val req = SubjectRequest(name = "Math", code = "123", professorId = 99L)
        val existing = Subject(id = 1L, name = "Math", code = "123", professor = Professor(id = 1L, name = "P", email = "p"))
        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(profRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<ProfessorNotFoundException> { service.update(1L, req) }
    }

    @Test
    fun `update retorna respuesta cuando es valido`() {
        val req = SubjectRequest(name = "Math Mod", code = "124", professorId = 2L)
        val oldProf = Professor(id = 1L, name = "Prof", email = "p@p.com")
        val newProf = Professor(id = 2L, name = "Prof2", email = "p2@p.com")
        val existing = Subject(id = 1L, name = "Math", code = "123", professor = oldProf)
        val updated = Subject(id = 1L, name = "Math Mod", code = "124", professor = newProf)
        val response = SubjectResponse(id = 1L, name = "Math Mod", code = "124", professor = ProfessorResponse(id = 2L, name = "Prof2", email = "p2@p.com"))

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(profRepository.findById(2L)).thenReturn(Optional.of(newProf))
        `when`(repository.save(any(Subject::class.java))).thenReturn(updated)
        `when`(mapper.toResponse(updated)).thenReturn(response)

        val result = service.update(1L, req)
        assertEquals("Math Mod", result.name)
    }

    @Test
    fun `delete lanza excepcion cuando no existe`() {
        `when`(repository.existsById(99L)).thenReturn(false)
        assertThrows<SubjectNotFoundException> { service.delete(99L) }
    }

    @Test
    fun `delete elimina cuando existe`() {
        `when`(repository.existsById(1L)).thenReturn(true)
        service.delete(1L)
        verify(repository, times(1)).deleteById(1L)
    }
}
