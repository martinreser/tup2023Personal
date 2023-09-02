package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.RandomNumberCreator;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.CambiarEstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaNoValidaException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AlumnoTest {

    private static Alumno alumno;
    private static Materia m1;
    private static Materia m2;
    private static Materia m3;
    private static Materia m4;
    private static Profesor profesor1;
    private static Asignatura a1;
    private static Asignatura a2;
    private static Asignatura a3;
    private static Asignatura a4;


    @BeforeAll
    public static void setUp() {

        profesor1 = new Profesor("Luciano", "Salotto", "Lic.");
        m1 = new Materia("Laboratorio 1", 1, 1, profesor1);
        m2 = new Materia("Laboratorio 2", 1, 2, profesor1);
        m3 = new Materia("Laboratorio 3", 2, 1, profesor1);
        m4 = new Materia("Laboratorio 4", 2, 2, profesor1);
        m2.agregarCorrelatividad(m1);
        m3.agregarCorrelatividad(m1);
        m3.agregarCorrelatividad(m2);
        m4.agregarCorrelatividad(m1);
        m4.agregarCorrelatividad(m2);
        m4.agregarCorrelatividad(m3);
        a1 = new Asignatura(m1, RandomNumberCreator.getInstance().generateRandomNumber(999));
        a2 = new Asignatura(m2, RandomNumberCreator.getInstance().generateRandomNumber(999));
        a3 = new Asignatura(m3, RandomNumberCreator.getInstance().generateRandomNumber(999));
        a4 = new Asignatura(m4, RandomNumberCreator.getInstance().generateRandomNumber(999));
    }

    @Test
    public void testNewAlumno() {
        alumno = new Alumno("Martin", "Reser", 45319502);
        assertEquals("Martin", alumno.getNombre());
        assertEquals("Reser", alumno.getApellido());
        assertEquals(45319502, alumno.getDni());
    }

    @Test
    public void testAlumnoSetearAtributos() {
        alumno = new Alumno();
        alumno.setId(1);
        alumno.setDni(45319502);
        alumno.setNombre("Martin");
        alumno.setApellido("Reser");
        alumno.setAsignaturas(new ArrayList<>());
        assertEquals(alumno.getId(), 1);
        assertEquals(alumno.getDni(), 45319502);
        assertEquals(alumno.getNombre(), "Martin");
        assertEquals(alumno.getApellido(), "Reser");
        assertEquals(alumno.obtenerListaAsignaturas(), new ArrayList<>());
    }

    @Test
    public void testAlumnoAgregarAsignatura() {
        alumno = new Alumno("Martin", "Reser", 45319502);
        alumno.agregarAsignatura(a1);
        assertEquals(alumno.obtenerListaAsignaturas().size(), 1);
        assertEquals(alumno.obtenerListaAsignaturas().get(0), a1);
    }
    @Test
    public void testAlumnoCursarAsignaturaSinQueEsteEnSusAsignaturas() {
        alumno = new Alumno("Martin", "Reser", 45319502);
        assertThrows(AsignaturaNotFoundException.class, () -> {
            alumno.cursarAsignatura(a1);
        });
    }

    @Test
    public void testAlumnoCursarSinCorrelativasCursadas() throws CambiarEstadoAsignaturaException, AsignaturaNotFoundException {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        a2 = new Asignatura(m2,2);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        assertThrows(CorrelatividadesNoAprobadasException.class, () -> {
            alumno.cursarAsignatura(a2);
        });
    }

    @Test
    public void testAlumnoCursarSinCorrelativasAprobadas() throws CambiarEstadoAsignaturaException, AsignaturaNotFoundException, CorrelatividadesNoAprobadasException {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        a2 = new Asignatura(m2,2);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        alumno.cursarAsignatura(a1);
        assertThrows(CorrelatividadesNoAprobadasException.class, () -> {
            alumno.cursarAsignatura(a2);
        });
    }

    @Test
    public void testAlumnoAprobarAsignaturaSinQueEsteEnSusAsignaturas() {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        assertThrows(AsignaturaNotFoundException.class, () -> {
            alumno.aprobarAsignatura(a1, 10);
        });
    }

    @Test
    public void testAlumnoAprobarAsignaturaSinCursarla() {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        alumno.agregarAsignatura(a1);
        assertThrows(EstadoIncorrectoException.class, () -> {
            alumno.aprobarAsignatura(a1, 10);
        });
    }

    @Test
    public void testAlumnoAprobarSinCorrelativasAprobadas() throws CambiarEstadoAsignaturaException, AsignaturaNotFoundException, CorrelatividadesNoAprobadasException {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        a2 = new Asignatura(m2,2);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        alumno.cursarAsignatura(a1);
        assertThrows(CorrelatividadesNoAprobadasException.class, () -> {
            alumno.aprobarAsignatura(a2, 10);
        });
    }

    @Test
    public void testAlumnoAprobarSinCorrelativasCursadas() {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        a2 = new Asignatura(m2,2);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        assertThrows(CorrelatividadesNoAprobadasException.class, () -> {
            alumno.aprobarAsignatura(a2, 10);
        });
    }

    @Test
    public void testAlumnoCursarAsignatura() throws CambiarEstadoAsignaturaException, AsignaturaNotFoundException, CorrelatividadesNoAprobadasException, EstadoIncorrectoException, NotaNoValidaException {
        alumno = new Alumno("Martin", "Reser", 45319502);
        a1 = new Asignatura(m1,1);
        a2 = new Asignatura(m2,2);
        alumno.agregarAsignatura(a1);
        alumno.agregarAsignatura(a2);
        alumno.cursarAsignatura(a1);
        alumno.aprobarAsignatura(a1,10);
        alumno.cursarAsignatura(a2);
        assertEquals(a1.getEstado(), EstadoAsignatura.APROBADA);
        assertEquals(a1.getNota().get(), 10);
        assertEquals(a2.getEstado(), EstadoAsignatura.CURSADA);
    }
}