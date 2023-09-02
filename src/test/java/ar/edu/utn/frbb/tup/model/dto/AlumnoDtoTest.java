package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.Profesor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlumnoDtoTest {

    private static AlumnoDto alumnoDto;

    @Test
    public void testAlumnoDtoSetearAtributos() {
        alumnoDto = new AlumnoDto();
        alumnoDto.setDni(45319502);
        alumnoDto.setNombre("Martín");
        alumnoDto.setApellido("Reser");
        assertEquals(alumnoDto.getNombre(), "Martín");
        assertEquals(alumnoDto.getApellido(), "Reser");
        assertEquals(alumnoDto.getDni(), 45319502);
    }

}