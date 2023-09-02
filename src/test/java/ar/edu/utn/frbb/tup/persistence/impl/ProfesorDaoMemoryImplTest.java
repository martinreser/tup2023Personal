package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
public class ProfesorDaoMemoryImplTest {

    @InjectMocks
    ProfesorDaoMemoryImpl profesorDaoMemory;

    MockMvc mockMvc;
    Materia materia, materia2, materia3;

    Profesor profesorCargado, profesorCargado2;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(profesorDaoMemory)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();

        materia = new Materia();
        materia.setNombre("Programación IV");
        materia2 = new Materia();
        materia2.setNombre("Laboratorio IV");
        materia3 = new Materia();
        materia3.setNombre("Algebra III");
    }

    @Test
    public void testProfesorSave() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor("Martin", "Resera", "Ing. en Software");
        profesorDaoMemory.save(profesorCargado);
        Profesor profesorEncontrado = profesorDaoMemory.findProfesorById(profesorCargado.getId());
        assertEquals(profesorCargado, profesorEncontrado);
    }

    @Test
    public void testProfesorFindProfesorById() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor("Martin", "Resere", "Ing. en Software");
        profesorDaoMemory.save(profesorCargado);
        Profesor profesorEncontrado = profesorDaoMemory.findProfesorById(profesorCargado.getId());
        assertEquals(profesorCargado, profesorEncontrado);
    }

    @Test
    public void testProfesorFindProfesorByIdInexistente() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor("Martin", "Resero", "Ing. en Software");
        profesorDaoMemory.save(profesorCargado);
        ProfesorNotFoundException exception = assertThrows(ProfesorNotFoundException.class, () -> {
            profesorDaoMemory.findProfesorById(1000L);
        });
        assertEquals("No se pudo encontrar un profesor con el ID: 1000.", exception.getMessage());
    }

    @Test
    public void testProfesorFindProfesorByChain() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor("Martina", "Reser", "Ing. en Software");
        profesorCargado2 = new Profesor("Julian", "Resardini", "Ing. en Informática");
        profesorDaoMemory.save(profesorCargado);
        profesorDaoMemory.save(profesorCargado2);
        List<Profesor> profesoresEncontrados = profesorDaoMemory.findProfesorByChain("Res");
        assertEquals(true, profesoresEncontrados.contains(profesorCargado));
        assertEquals(true, profesoresEncontrados.contains(profesorCargado2));
    }

    @Test
    public void testProfesorFindProfesorByChainInexistente() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor("Martini", "Reser", "Ing. en Software");
        profesorCargado2 = new Profesor("Julian", "Resardón", "Ing. en Informática");
        profesorDaoMemory.save(profesorCargado);
        profesorDaoMemory.save(profesorCargado2);
        ProfesorNotFoundException exception = assertThrows(ProfesorNotFoundException.class, () -> {
            profesorDaoMemory.findProfesorByChain("Resor");
        });
        assertEquals("No se encuentra ningún profesor que comience con el apellido 'Resor'.", exception.getMessage());
    }

    @Test
    public void testProfesorGetMateriasDictadasById() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor();
        profesorCargado.setNombre("Roman");
        profesorCargado.setApellido("Martinez");
        profesorCargado.setMateriasDictadas(materia);
        profesorCargado.setMateriasDictadas(materia2);
        profesorCargado.setMateriasDictadas(materia3);
        profesorDaoMemory.save(profesorCargado);
        assertEquals(materia3, profesorDaoMemory.getMateriasDictadas(profesorCargado.getId()).get(0)); // ALGEBRA
        assertEquals(materia2, profesorDaoMemory.getMateriasDictadas(profesorCargado.getId()).get(1)); // LAB IV
        assertEquals(materia, profesorDaoMemory.getMateriasDictadas(profesorCargado.getId()).get(2));  // PROG IV
        // La salida debería de ser de esta forma, ya que se ordenan alfabéticamente.
    }

    @Test
    public void testProfesorGetMateriasDictadasByIdInexistente() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor();
        profesorCargado.setNombre("Martin");
        profesorCargado.setApellido("Romance");
        profesorCargado.setMateriasDictadas(materia);
        profesorCargado.setMateriasDictadas(materia2);
        profesorCargado.setMateriasDictadas(materia3);
        profesorDaoMemory.save(profesorCargado);
        ProfesorNotFoundException exception = assertThrows(ProfesorNotFoundException.class, () -> {
            profesorDaoMemory.getMateriasDictadas(1000L);
        });
        assertEquals("No se pudo encontrar un profesor con el ID: 1000.", exception.getMessage());
    }



    @Test
    public void testProfesorUpdateById() throws YaExistenteException {
        profesorCargado = new Profesor("Martina", "Resera", "Lic. en Ingeniería");
        profesorDaoMemory.save(profesorCargado);
        profesorCargado.setTitulo("Lic. en Economía");
        profesorDaoMemory.update(profesorCargado.getId(), profesorCargado);
        assertEquals("Lic. en Economía", profesorCargado.getTitulo());
    }

    @Test
    public void testProfesorDeleteProfesorById() throws ProfesorNotFoundException, ProfesorEliminadoCorrectamente, YaExistenteException {
        profesorCargado = new Profesor("Martina", "Rese", "Lic. en Ingeniería");
        profesorDaoMemory.save(profesorCargado);
        assertEquals(profesorCargado, profesorDaoMemory.findProfesorById(profesorCargado.getId()));
        profesorDaoMemory.deleteProfesorById(profesorCargado.getId());
        // Comprobamos de buscar el profesor, para verificar que realmente se eliminó.
        ProfesorNotFoundException exception2 = assertThrows(ProfesorNotFoundException.class, () -> {
            profesorDaoMemory.findProfesorById(profesorCargado.getId());
        });
        assertEquals("No se pudo encontrar un profesor con el ID: " + profesorCargado.getId() + ".", exception2.getMessage());
    }

    @Test
    public void testProfesorDeleteProfesorByIdInexistente() throws ProfesorNotFoundException, YaExistenteException {
        profesorCargado = new Profesor("Martinu", "Reser", "Lic. en Ingeniería");
        profesorDaoMemory.save(profesorCargado);
        assertEquals(profesorCargado, profesorDaoMemory.findProfesorById(profesorCargado.getId()));
        ProfesorNotFoundException exception = assertThrows(ProfesorNotFoundException.class, () -> {
            profesorDaoMemory.findProfesorById(1000L);
        });
        assertEquals("No se pudo encontrar un profesor con el ID: 1000.", exception.getMessage());
    }
}