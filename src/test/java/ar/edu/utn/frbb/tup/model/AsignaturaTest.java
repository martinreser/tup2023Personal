package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.CambiarEstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaNoValidaException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AsignaturaTest{
    private static Asignatura asignatura;

    private static Materia materia;
    private static Profesor profesor;

    @BeforeAll
    public static void setUp(){
        profesor = new Profesor("Luciano", "Salotto", "Lic.");
        materia = new Materia("Laboratorio 3", 2, 1, profesor);
    }

    @Test
    public void testNewAsignatura() {
        Asignatura asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA, asignatura.getEstado());
        assertFalse(asignatura.getNota().isPresent());
        assertEquals("Laboratorio 3", asignatura.getNombreAsignatura());
    }

    @Test
    public void testAsignaturaSetearAtributos() {
        asignatura = new Asignatura();
        asignatura.setAsignaturaId((long) 1);
        asignatura.setEstado(EstadoAsignatura.APROBADA);
        asignatura.setNota(10);
        asignatura.setMateria(materia);
        assertEquals(asignatura.getAsignaturaId(), 1);
        assertEquals(asignatura.getEstado(), EstadoAsignatura.APROBADA);
        assertEquals(asignatura.getNota().get(), 10);
        assertEquals(asignatura.getMateria(), materia);
    }

    @Test
    public void testAprobarAasignatura() throws CambiarEstadoAsignaturaException, NotaNoValidaException, EstadoIncorrectoException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        asignatura.aprobarAsignatura(10);
        assertEquals(EstadoAsignatura.APROBADA,asignatura.getEstado());
        assertEquals(asignatura.getNota().get(), 10);
    }

    @Test
    public void testCursarAsignaturaYaCursada() throws CambiarEstadoAsignaturaException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        assertThrows(CambiarEstadoAsignaturaException.class, () -> {
            asignatura.cursarAsignatura();
        });
        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);
    }

    @Test
    public void testCursarAsignaturaYaAprobada() throws CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        asignatura.aprobarAsignatura(10);
        assertThrows(CambiarEstadoAsignaturaException.class, () -> {
            asignatura.cursarAsignatura();
        });
        assertEquals(asignatura.getEstado(), EstadoAsignatura.APROBADA);
    }

    @Test
    public void testAprobarAsignaturaYaAprobada() throws CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        asignatura.aprobarAsignatura(10);
        assertThrows(EstadoIncorrectoException.class, () -> {
            asignatura.aprobarAsignatura(10);
        });
        assertEquals(asignatura.getEstado(), EstadoAsignatura.APROBADA);
    }

    @Test
    public void testAprobarAsignaturaConNotaMayorA10() throws CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        assertThrows(NotaNoValidaException.class, () -> {
            asignatura.aprobarAsignatura(11);
        });
        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);
    }

    @Test
    public void testAprobarAsignaturaConNotaMenorA0() throws CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        assertThrows(NotaNoValidaException.class, () -> {
            asignatura.aprobarAsignatura(-5);
        });
        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);
    }

    @Test
    public void testAprobarAsignaturaConNotaMenorA4() throws CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        asignatura = new Asignatura(materia, 1);
        assertEquals(EstadoAsignatura.NO_CURSADA,asignatura.getEstado());
        asignatura.cursarAsignatura();
        assertThrows(NotaNoValidaException.class, () -> {
            asignatura.aprobarAsignatura(2);
        });
        assertEquals(asignatura.getEstado(), EstadoAsignatura.CURSADA);
    }

}