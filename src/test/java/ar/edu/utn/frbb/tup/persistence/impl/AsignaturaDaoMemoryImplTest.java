package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.CambiarEstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaNoValidaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class AsignaturaDaoMemoryImplTest {

    @InjectMocks
    AsignaturaDaoMemoryImpl asignaturaDaoMemory;

    MockMvc mockMvc;
    Materia materia;

    Materia materia2;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(asignaturaDaoMemory)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();
        materia = new Materia();
        materia2 = new Materia();
    }

    @Test
    public void testAsignaturaUpdate() throws AsignaturaNotFoundException, EstadoIncorrectoException, NotaNoValidaException, CambiarEstadoAsignaturaException {
        Asignatura asignatura = asignaturaDaoMemory.saveAsignatura(materia);
        asignatura.cursarAsignatura();
        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);
        asignatura.aprobarAsignatura(10);
        asignaturaDaoMemory.update(asignatura);
        assertEquals(asignatura.getEstado(), EstadoAsignatura.APROBADA);
        assertEquals(asignatura.getNota().get(), 10);
    }

    @Test
    public void testAsignaturaSaveAsignatura() throws AsignaturaNotFoundException {
        Asignatura asignaturaCargada = asignaturaDaoMemory.saveAsignatura(materia);
        Asignatura asignaturaEncontrada = asignaturaDaoMemory.getAsignaturaPorId(asignaturaCargada.getAsignaturaId());
        assertEquals(asignaturaCargada, asignaturaEncontrada);
    }

    @Test
    public void testAsignaturaSaveAsignaturas() throws AsignaturaNotFoundException {
        Asignatura asignaturaCargada1 = asignaturaDaoMemory.saveAsignatura(materia);
        Asignatura asignaturaCargada2 = asignaturaDaoMemory.saveAsignatura(materia2);
        Asignatura asignaturaCargada3 = asignaturaDaoMemory.saveAsignatura(materia);
        List<Asignatura> asignaturasEncontradas  = asignaturaDaoMemory.getListAsignaturas();
        assertEquals(true, asignaturasEncontradas.contains(asignaturaCargada1));
        assertEquals(true, asignaturasEncontradas.contains(asignaturaCargada2));
        assertEquals(true, asignaturasEncontradas.contains(asignaturaCargada3));
    }

    @Test
    public void testAsignaturaGetAsignaturaPorId() throws AsignaturaNotFoundException {
        Asignatura asignaturaCargada = asignaturaDaoMemory.saveAsignatura(materia);
        Asignatura asignaturaEncontrada = asignaturaDaoMemory.getAsignaturaPorId(asignaturaCargada.getAsignaturaId());
        assertEquals(asignaturaCargada, asignaturaEncontrada);
    }

    @Test
    public void testAsignaturaGetAsignaturaPorIdInexistente() throws AsignaturaNotFoundException {
        Asignatura asignaturaCargada = asignaturaDaoMemory.saveAsignatura(materia);
        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> {
            asignaturaDaoMemory.getAsignaturaPorId(1000L);
        });
        assertEquals("No se encuentra ninguna asignatura con el ID: 1000", exception.getMessage());
    }

    @Test
    public void testAsignaturaGetListAsignaturas() {
        Asignatura asignaturaCargada1 = asignaturaDaoMemory.saveAsignatura(materia);
        Asignatura asignaturaCargada2 = asignaturaDaoMemory.saveAsignatura(materia2);
        List<Asignatura> asignaturasEncontradas  = asignaturaDaoMemory.getListAsignaturas();
        assertEquals(true, asignaturasEncontradas.contains(asignaturaCargada1));
        assertEquals(true, asignaturasEncontradas.contains(asignaturaCargada2));
    }

}