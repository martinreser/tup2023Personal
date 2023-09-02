package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AsignaturaDtoTest {
    private static AsignaturaDto asignaturaDto;
    @Test
    public void testAsignaturaDtoSetearAtributos() {
        asignaturaDto = new AsignaturaDto();
        asignaturaDto.setCondicion(EstadoAsignatura.APROBADA);
        asignaturaDto.setNota(10);
        assertEquals(asignaturaDto.getNota(), 10);
        assertEquals(asignaturaDto.getCondicion(), EstadoAsignatura.APROBADA);
    }
}