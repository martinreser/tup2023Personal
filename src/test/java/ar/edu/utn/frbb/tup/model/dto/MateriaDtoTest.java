package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MateriaDtoTest {

    private static MateriaDto materiaDto;
    private static Profesor profesor;

    @Test
    public void testMateriaSetearAtributos() {
        materiaDto = new MateriaDto();
        materiaDto.setNombre("Laboratorio II");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(12);
        materiaDto.setCorrelatividades(null);
        assertEquals(materiaDto.getNombre(), "Laboratorio II");
        assertEquals(materiaDto.getAnio(), 1);
        assertEquals(materiaDto.getCuatrimestre(), 1);
        assertEquals(materiaDto.getProfesorId(), 12);
        assertEquals(materiaDto.getCorrelatividades(), null);
    }

}