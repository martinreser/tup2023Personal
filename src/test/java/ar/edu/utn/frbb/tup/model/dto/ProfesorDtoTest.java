package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfesorDtoTest {
    private static ProfesorDto profesorDto;

    @Test
    public void testProfesorSetearAtributos() {
        profesorDto = new ProfesorDto();
        profesorDto.setNombre("Martín");
        profesorDto.setApellido("Reser");
        profesorDto.setTitulo("Diseñador de Videojuegos");
        assertEquals(profesorDto.getNombre(), "Martín");
        assertEquals(profesorDto.getApellido(), "Reser");
        assertEquals(profesorDto.getTitulo(), "Diseñador de Videojuegos");
    }

}