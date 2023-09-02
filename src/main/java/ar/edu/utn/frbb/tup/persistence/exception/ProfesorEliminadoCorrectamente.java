package ar.edu.utn.frbb.tup.persistence.exception;

public class ProfesorEliminadoCorrectamente extends Exception {

    // Esta excepción se arroja, cuando queremos eliminar un profesor,
    // y es el único que estaba guardado dentro del repositorio.

    public ProfesorEliminadoCorrectamente(String s){super(s);}
}
