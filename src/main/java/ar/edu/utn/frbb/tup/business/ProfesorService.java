package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;

import java.util.List;

public interface ProfesorService {
    Profesor crearProfesor(ProfesorDto profesorDto) throws DatoInvalidoException, YaExistenteException;

    List<Profesor> buscarProfesorPorCadena(String apellido) throws ProfesorNotFoundException;
    Profesor buscarProfesorPorId(Long id) throws ProfesorNotFoundException;
    List<Materia> buscarMateriasDictadas(Long id) throws ProfesorNotFoundException;

    void actualizarProfesor(Profesor p) throws ProfesorNotFoundException;
    Profesor actualizarProfesorPorId(Long idProfesor, ProfesorDto profesorDto) throws ProfesorNotFoundException, DatoInvalidoException;

    List<Profesor> borrarProfesorPorId(Long id) throws ProfesorNotFoundException, ProfesorEliminadoCorrectamente;

}
