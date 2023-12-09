package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.RandomNumberCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomNumberCreatorTest {
    @Test
    public void testRandomNumber() {
        int numMaximo = 2;
        int numeroRandom = RandomNumberCreator.getInstance().generateRandomNumber(numMaximo);
        assertEquals(numeroRandom < numMaximo && numeroRandom >= 0, true);
    }
}
