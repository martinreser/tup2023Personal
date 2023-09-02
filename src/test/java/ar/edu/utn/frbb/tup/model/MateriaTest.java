package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MateriaTest {
    private static Materia materia;
    private static Profesor profesor;
    @BeforeAll
    public static void setUp(){
        profesor = new Profesor();
        profesor.setNombre("Luciano");
        profesor.setApellido("Salotto");
        profesor.setTitulo("Lic.");
    }

    @Test
    public void testMateriaSetearAtributos() {
        materia = new Materia();
        materia.setMateriaId(1);
        materia.setNombre("Laboratorio II");
        materia.setAnio(1);
        materia.setCuatrimestre(1);
        materia.setProfesor(profesor);
        materia.setCorrelatividades(new ArrayList<>());
        assertEquals(materia.getMateriaId(), 1);
        assertEquals(materia.getNombre(), "Laboratorio II");
        assertEquals(materia.getAnio(), 1);
        assertEquals(materia.getCuatrimestre(), 1);
        assertEquals(materia.getProfesor(), profesor);
        assertEquals(materia.getCorrelatividades(), new ArrayList<>());
        assertEquals(materia.getProfesor().getMateriasDictadas().get(0), materia);
    }


}