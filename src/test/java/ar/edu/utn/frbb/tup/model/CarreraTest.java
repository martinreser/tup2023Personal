package ar.edu.utn.frbb.tup.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarreraTest {
    private static Carrera carrera;
    private static Profesor profesor;
    private static Materia materia, materia2;

    @BeforeAll
    public static void setUp(){
        profesor = new Profesor("Luciano", "Salotto", "Lic.");
        materia = new Materia("Laboratorio I", 1, 1, profesor);
        materia2 = new Materia("Laboratorio II", 1, 2, profesor);
    }

    @Test
    public void testCarreraSetearAtributos() {
        carrera = new Carrera("Ingeniería Electrónica", 5);
        carrera.agregarMateria(materia);
        carrera.agregarMateria(materia2);
        assertEquals(carrera.getNombre(), "Ingeniería Electrónica");
        assertEquals(carrera.getCantidadAnios(), 5);
        assertEquals(carrera.getMateriasList().get(0), materia);
        assertEquals(carrera.getMateriasList().get(1), materia2);
    }

}