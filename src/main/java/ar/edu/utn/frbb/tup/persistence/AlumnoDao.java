package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

import java.util.List;

public interface AlumnoDao {

    // POST
    Alumno saveAlumno(Alumno a);

    // GET
    List<Asignatura> getAsignaturasAlumnoPorId(Long id) throws AlumnoNotFoundException;
    Asignatura getAsignaturaAlumnoPorId(Long id, Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException;
    List<Alumno> findAlumnoByChain(String apellidoAlumno) throws AlumnoNotFoundException;
    Alumno findAlumnoById(Long id) throws AlumnoNotFoundException;

    // PUT
    void update(Long id, Alumno alumno);

    // DELETE
    List<Alumno> deleteAlumnoById(Long id) throws AlumnoNotFoundException;


}
