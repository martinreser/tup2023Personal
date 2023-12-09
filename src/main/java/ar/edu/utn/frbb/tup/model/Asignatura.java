package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.CambiarEstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaNoValidaException;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.Optional;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "asignaturaId")
public class Asignatura {
    private Long asignaturaId;
    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota;

    public Asignatura() {
    }
    public Asignatura(Materia materia, long asignaturaId) {
        this.asignaturaId = asignaturaId;
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Optional<Integer> getNota() {
        return Optional.ofNullable(nota);
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public EstadoAsignatura getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public String getNombreAsignatura(){
        return this.materia.getNombre();
    }

    public Materia getMateria() {
        return materia;
    }

    public void cursarAsignatura() throws CambiarEstadoAsignaturaException {
        if (this.estado.equals(EstadoAsignatura.CURSADA)){
            throw new CambiarEstadoAsignaturaException("La materia " + materia.getNombre() + " ya se encuentra cursada.");
        }
        if (this.estado.equals(EstadoAsignatura.APROBADA)){
            throw new CambiarEstadoAsignaturaException("La materia " + materia.getNombre() + " ya se encuentra aprobada.");
        }
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException, NotaNoValidaException {
        comprobarNota(nota);
        if (this.estado.equals(EstadoAsignatura.APROBADA)){
            throw new EstadoIncorrectoException("La asignatura " + materia.getNombre() + " ya está aprobada [NOTA: " + this.nota + "].");
        }
        if (!this.estado.equals(EstadoAsignatura.CURSADA)) {
            throw new EstadoIncorrectoException("La asignatura " + materia.getNombre() + " debe estar cursada, para poder aprobarse.");
        }
        if (nota<4){
            throw new NotaNoValidaException("Si desea aprobar la asignatura, la nota debe ser igual o mayor a 4.");
        }
        if (nota>=4) {
            this.estado = EstadoAsignatura.APROBADA;
            this.nota = nota;
        }
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public List<Materia> getCorrelatividades(){
        return this.materia.getCorrelatividades();
    }

    private boolean comprobarNota(final int nota) throws NotaNoValidaException {
        if (nota == 0){
            throw new NotaNoValidaException("La nota no puede ser nula.");
        }
        if (nota > 10 || nota < 0){
            throw new NotaNoValidaException("La nota debe ser mayor a 0 y menor a 10.");
        }
        return true;
    }

}
