package ar.edu.utn.frbb.tup.persistence.exception;

public class AlumnoEliminadoCorrectamente extends Exception {

    // Esta excepción se arroja, cuando queremos eliminar un alumno,
    // y es el único que estaba guardado dentro del repositorio.

    public AlumnoEliminadoCorrectamente (){}
}
