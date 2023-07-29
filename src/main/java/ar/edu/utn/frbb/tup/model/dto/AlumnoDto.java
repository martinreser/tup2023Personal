package ar.edu.utn.frbb.tup.model.dto;

import org.springframework.stereotype.Component;

@Component
public class AlumnoDto {
    String nombre;
    String apellido;
    Long dni;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }
}
