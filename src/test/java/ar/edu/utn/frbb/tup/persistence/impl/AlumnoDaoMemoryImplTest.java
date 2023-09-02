package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.controller.AlumnoController;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class AlumnoDaoMemoryImplTest {
    @InjectMocks
    AlumnoDaoMemoryImpl alumnoDaoMemory;

    MockMvc mockMvc;
    Materia materia;
    Asignatura asignatura;
    List<Asignatura> asignaturas;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(alumnoDaoMemory)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();

        materia = new Materia();
        asignatura = new Asignatura(materia, 1);
        asignaturas = Arrays.asList(asignatura);
    }

    @Test
    public void testAlumnoSave() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumno);
        Alumno alumnoEncontrado = alumnoDaoMemory.findAlumnoById(alumno.getId());
        assertEquals(alumno, alumnoEncontrado);
    }

    @Test
    public void testAlumnoByIDInexistente() throws Exception {
        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoDaoMemory.findAlumnoById(1000L);
        });
        assertEquals("No se encuentra ningún alumno con el ID: 1000.", exception.getMessage());

    }

    @Test
    public void testAlumnoGetAsignaturasPorId() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumno.setAsignaturas(asignaturas);
        alumnoDaoMemory.saveAlumno(alumno);
        List asignaturasEncontradas = alumnoDaoMemory.getAsignaturasAlumnoPorId(alumno.getId());
        assertEquals(asignaturas, asignaturasEncontradas);
    }

    @Test
    public void testAlumnoGetAsignaturasPorIdInexistente() throws Exception {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumno.setAsignaturas(asignaturas);
        alumnoDaoMemory.saveAlumno(alumno);
        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoDaoMemory.getAsignaturasAlumnoPorId(1000L);
        });
        assertEquals("No se encuentra ningún alumno con el ID: 1000.", exception.getMessage());
    }

    @Test
    public void testAlumnoGetAsignaturasPorIdVacía() throws Exception {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumno);
        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> {
            alumnoDaoMemory.getAsignaturasAlumnoPorId(alumno.getId());
        });
        assertEquals("La lista de asignaturas del alumno " + alumno.getNombre() +
                alumno.getApellido() + " está vacía.", exception.getMessage());
    }

    @Test
    public void testAlumnoGetAsignaturaPorId() throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumno.setAsignaturas(asignaturas);
        alumnoDaoMemory.saveAlumno(alumno);
        Asignatura asignaturaEncontrada = alumnoDaoMemory.getAsignaturaAlumnoPorId(alumno.getId(), 1L);
        assertEquals(asignatura, asignaturaEncontrada);
    }

    @Test
    public void testAlumnoGetAsignaturaPorIdInexistente() throws Exception {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumno.setAsignaturas(asignaturas);
        alumnoDaoMemory.saveAlumno(alumno);
        AsignaturaNotFoundException exception = assertThrows(AsignaturaNotFoundException.class, () -> {
            alumnoDaoMemory.getAsignaturaAlumnoPorId(alumno.getId(), 1000L);
        });
        assertEquals("El alumno " + alumno.getNombre() + " " + alumno.getApellido() +
                " (ID: " + alumno.getId() + "), no tiene ninguna asignatura con el ID: 1000.", exception.getMessage());
    }

    @Test
    public void testAlumnoFindAlumnoByChain() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("Martin", "Reserva", 45319502);
        Alumno alumno2 = new Alumno("Martin", "Reservo", 45319502);
        alumnoDaoMemory.saveAlumno(alumno);
        alumnoDaoMemory.saveAlumno(alumno2);
        List<Alumno> alumnosEncontrados = alumnoDaoMemory.findAlumnoByChain("Reserv");
        assertEquals(true, alumnosEncontrados.contains(alumno));
        assertEquals(true, alumnosEncontrados.contains(alumno2));
    }

    @Test
    public void testAlumnoFindAlumnoByChainInexistente() throws AlumnoNotFoundException {
        Alumno alumno1 = new Alumno("Martin", "Reser", 45319502);
        Alumno alumno2 = new Alumno("Martin", "Redondo", 45319502);
        alumnoDaoMemory.saveAlumno(alumno1);
        alumnoDaoMemory.saveAlumno(alumno2);
        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoDaoMemory.findAlumnoByChain("Ra");
        });
        assertEquals("No se encuentra ningún alumno que comience con el apellido 'Ra'.", exception.getMessage());
    }

    @Test
    public void testAlumnoFindAlumnoById() throws AlumnoNotFoundException {
        Alumno alumnoCargado = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumnoCargado);
        Alumno alumnoEncontrado = alumnoDaoMemory.findAlumnoById(alumnoCargado.getId());
        assertEquals(alumnoCargado, alumnoEncontrado);
    }

    @Test
    public void testAlumnoFindAlumnoByIdInexistente() throws AlumnoNotFoundException {
        Alumno alumno1 = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumno1);
        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoDaoMemory.findAlumnoById(1000L);
        });
        assertEquals("No se encuentra ningún alumno con el ID: 1000.", exception.getMessage());
    }

    @Test
    public void testAlumnoUpdateById() throws AlumnoNotFoundException {
        Alumno alumnoCargado = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumnoCargado);
        alumnoCargado.setApellido("Perez");
        alumnoDaoMemory.update(alumnoCargado.getId(), alumnoCargado);
        assertEquals(alumnoCargado.getApellido(), "Perez");
    }

    @Test
    public void testAlumnoDeleteById() throws AlumnoNotFoundException, AlumnoEliminadoCorrectamente {
        Alumno alumnoCargado = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumnoCargado);
        assertEquals(alumnoCargado, alumnoDaoMemory.findAlumnoById(alumnoCargado.getId()));

        alumnoDaoMemory.deleteAlumnoById(alumnoCargado.getId());

        // Comprobamos de buscar el alumno, para verificar que realmente se eliminó.
        AlumnoNotFoundException exception2 = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoDaoMemory.findAlumnoById(alumnoCargado.getId());
        });
        assertEquals("No se encuentra ningún alumno con el ID: " + alumnoCargado.getId() +
                ".", exception2.getMessage());
    }

    @Test
    public void testAlumnoDeleteByIdInexistente() throws AlumnoNotFoundException {
        Alumno alumnoCargado = new Alumno("Martin", "Reser", 45319502);
        alumnoDaoMemory.saveAlumno(alumnoCargado);
        assertEquals(alumnoCargado, alumnoDaoMemory.findAlumnoById(alumnoCargado.getId()));
        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoDaoMemory.deleteAlumnoById(1000L);
        });
        assertEquals("No se encuentra ningún alumno con el ID: 1000.", exception.getMessage());
    }
}