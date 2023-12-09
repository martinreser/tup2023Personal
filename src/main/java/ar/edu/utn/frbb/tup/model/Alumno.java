package ar.edu.utn.frbb.tup.model;




import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.CambiarEstadoAsignaturaException;
import ar.edu.utn.frbb.tup.persistence.exception.NotaNoValidaException;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Alumno {
    private long id;
    private String nombre;
    private String apellido;
    private long dni;
    private List<Asignatura> asignaturas;

    public Alumno() {
    }

    public Alumno(String nombre, String apellido, long dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;

        asignaturas = new ArrayList<>();
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public long getDni() {
        return dni;
    }

    public void agregarAsignatura(Asignatura a){
        this.asignaturas.add(a);
    }

    public List<Asignatura> obtenerListaAsignaturas(){
        return this.asignaturas;
    }

    public void aprobarAsignatura(Asignatura asignatura, int nota) throws EstadoIncorrectoException, NotaNoValidaException, CorrelatividadesNoAprobadasException, AsignaturaNotFoundException {
        chequearAsignatura(asignatura);
        for (Materia correlativa :
                asignatura.getCorrelatividades()) {
            chequearCorrelatividad(correlativa, asignatura);
        }
        asignatura.aprobarAsignatura(nota);
    }

    public void cursarAsignatura (Asignatura asignatura) throws CambiarEstadoAsignaturaException, AsignaturaNotFoundException, CorrelatividadesNoAprobadasException {
        chequearAsignatura(asignatura);
        for (Materia correlativa :
                asignatura.getCorrelatividades()) {
            chequearCorrelatividad(correlativa, asignatura);
        }
        asignatura.cursarAsignatura();
    }

    private void chequearCorrelatividad(Materia correlativa, Asignatura asignatura) throws CorrelatividadesNoAprobadasException {
        for (Asignatura a:
                asignaturas) {
            if (correlativa.getNombre().equals(a.getNombreAsignatura())) {
                if (!EstadoAsignatura.APROBADA.equals(a.getEstado())) {
                    throw new CorrelatividadesNoAprobadasException("La asignatura " + correlativa.getNombre() + " [ID: " + a.getAsignaturaId() + "] debe estar aprobada para cursar/aprobar " + asignatura.getNombreAsignatura());
                }
            }
        }
    }

    private void chequearAsignatura(Asignatura asignatura) throws AsignaturaNotFoundException {
        if(!asignaturas.contains(asignatura)) {
            throw new AsignaturaNotFoundException("El alumno " + this.nombre + " " + this.apellido + " (ID: " + this.id + "), no tiene " +
                    "a la asignatura: " + asignatura.getNombreAsignatura());
        }
    }

    public void actualizarAsignatura(Asignatura asignatura) {
        for (Asignatura a:
             asignaturas) {
            if (a.getNombreAsignatura().equals(asignatura.getNombreAsignatura())) {
                if (asignatura.getNota().isPresent() || asignatura.getEstado().equals(EstadoAsignatura.APROBADA)){
                    a.setNota(asignatura.getNota().get());
                }
                a.setEstado(asignatura.getEstado());
            }
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return id == alumno.id && dni == alumno.dni && nombre == alumno.nombre && apellido == alumno.apellido && asignaturas == alumno.asignaturas;
    }
}
