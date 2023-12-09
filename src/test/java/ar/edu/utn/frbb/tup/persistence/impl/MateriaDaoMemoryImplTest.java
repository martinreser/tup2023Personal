package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
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
public class MateriaDaoMemoryImplTest {

    @InjectMocks
    MateriaDaoMemoryImpl materiaDaoMemory;

    MockMvc mockMvc;
    Profesor profesor;

    Materia materiaCargada1;
    Materia materiaCargada2;


    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(materiaDaoMemory)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();
        profesor = new Profesor("Martin", "Reser", "Ing. Mecánico");
    }

    @Test
    public void testMateriaSave() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Matemática",1,1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        Materia materiaEncontrada = materiaDaoMemory.findMateriaById(materiaCargada1.getMateriaId());
        assertEquals(materiaCargada1, materiaEncontrada);
    }

    @Test
    public void testMateriaFindMateriaById() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Filosofía",1,1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        Materia materiaEncontrada = materiaDaoMemory.findMateriaById(materiaCargada1.getMateriaId());
        assertEquals(materiaCargada1, materiaEncontrada);
    }

    @Test
    public void testMateriaFindMateriaByIdInexistente() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Mecánica",1,1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> {
            materiaDaoMemory.findMateriaById(1000);
        });
        assertEquals("No se encuentra ninguna materia con el ID: 1000", exception.getMessage());
    }

    @Test
    public void testMateriaFindMateriaByCadena() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Mecánica-Técnica",1,1, profesor);
        materiaCargada2 = new Materia("Metodología",1,1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        materiaDaoMemory.save(materiaCargada2, arrayVacio);
        List<Materia> materiasEncontradas = materiaDaoMemory.findMateriaByChain("Me");
        assertEquals(true, materiasEncontradas.contains(materiaCargada1));
        assertEquals(true, materiasEncontradas.contains(materiaCargada2));
    }

    @Test
    public void testMateriaFindMateriaByCadenaInexistente() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Mecánicos",1,1, profesor);
        materiaCargada2 = new Materia("Metodología de Datos",1,1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        materiaDaoMemory.save(materiaCargada2, arrayVacio);
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> {
            materiaDaoMemory.findMateriaByChain("Mer");
        });
        assertEquals("No se encuentra ninguna materia que comience con el nombre 'Mer'.", exception.getMessage());
    }

    @Test
    public void testMateriaGetAllMaterias() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Mecánicalogía",1,1, profesor);
        materiaCargada2 = new Materia("Metodología de Sistemas",1,1, profesor);
        Materia materiaCargada3 = new Materia("Matemáticas", 1, 1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        materiaDaoMemory.save(materiaCargada2, arrayVacio);
        materiaDaoMemory.save(materiaCargada3, arrayVacio);
        List<Materia> materiasEncontradas = materiaDaoMemory.getAllMaterias();
        assertEquals(true, materiasEncontradas.contains(materiaCargada1));
        assertEquals(true, materiasEncontradas.contains(materiaCargada2));
        assertEquals(true, materiasEncontradas.contains(materiaCargada3));
    }

    @Test
    public void testMateriaDeleteMateriaById() throws MateriaNotFoundException, YaExistenteException {
        materiaCargada1 = new Materia("Mecanística",1,1, profesor);
        int[] arrayVacio = new int[0];
        materiaDaoMemory.save(materiaCargada1, arrayVacio);
        materiaDaoMemory.deleteMateriaById(materiaCargada1.getMateriaId());
        MateriaNotFoundException exception = assertThrows(MateriaNotFoundException.class, () -> {
            materiaDaoMemory.findMateriaById(materiaCargada1.getMateriaId());
        });
        assertEquals("No se encuentra ninguna materia con el ID: " + materiaCargada1.getMateriaId(), exception.getMessage());
    }
}