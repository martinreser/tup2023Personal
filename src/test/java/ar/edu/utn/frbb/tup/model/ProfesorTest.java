package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfesorTest {
    private static Profesor profesor;

    private static Materia materia;

    @BeforeAll
    public static void setUp(){
        materia = new Materia();
    }

    @Test
    public void testProfesorSetearAtributos() {
        profesor = new Profesor();
        profesor.setId(1);
        profesor.setNombre("Martín");
        profesor.setApellido("Reser");
        profesor.setTitulo("Diseñador de Videojuegos");
        profesor.setMateriasDictadas(materia);
        assertEquals(profesor.getId(), 1);
        assertEquals(profesor.getNombre(), "Martín");
        assertEquals(profesor.getApellido(), "Reser");
        assertEquals(profesor.getTitulo(), "Diseñador de Videojuegos");
        assertEquals(profesor.getMateriasDictadas().get(0), materia);
    }

}