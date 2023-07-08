package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import java.util.List;

public interface ProfesorDao {

    // POST
    Profesor save(Profesor profesor);

    // GET
    Profesor findProfesorById(long id) throws ProfesorNotFoundException;
    List<Profesor> findProfesorByChain(String apellidoProfesor) throws ProfesorNotFoundException;
    List<Materia> getMateriasDictadas(Long id) throws ProfesorNotFoundException;

    // PUT
    void update(Long idProfesor, Profesor p);

    // DELETE
    List<Profesor> deleteProfesorPorId(Long id) throws ProfesorNotFoundException;
}
