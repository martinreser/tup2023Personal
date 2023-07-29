package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import org.springframework.stereotype.Component;

@Component
public class AsignaturaDto {
    private int nota;

    private EstadoAsignatura condicion;

    public void setNota(int nota) {
        this.nota = nota;
    }

    public void setCondicion(EstadoAsignatura condicion) {
        this.condicion = condicion;
    }

    public int getNota() {
        return nota;
    }

    public EstadoAsignatura getCondicion() {
        return this.condicion;
    }


}
