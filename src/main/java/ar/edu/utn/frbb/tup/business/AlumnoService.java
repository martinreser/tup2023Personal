package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;

import java.util.List;

public interface AlumnoService {
    Alumno crearAlumno(AlumnoDto alumno) throws DatoInvalidoException;

    List<Alumno> buscarAlumnoPorCadena(String apellidoAlumno) throws AlumnoNotFoundException;
    Alumno buscarAlumnoPorId(Long id) throws AlumnoNotFoundException;
    List<Asignatura> obtenerAsignaturasAlumnoPorId(Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException;
    Asignatura obtenerAsignaturaAlumnoPorId(Long id, Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException;
    Alumno actualizarAlumnoPorId(Long idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException;
    Asignatura actualizarEstadoAsignaturaPorID(Long idAlumno, Long idAsignatura, AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AlumnoNotFoundException,
            AsignaturaNotFoundException, NotaNoValidaException, CambiarEstadoAsignaturaException;
    List<Alumno> borrarAlumnoPorId(Long id) throws AlumnoNotFoundException, AlumnoEliminadoCorrectamente;

}
