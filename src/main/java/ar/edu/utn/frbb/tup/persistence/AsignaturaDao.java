package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public interface AsignaturaDao {
    void update(Asignatura a) throws AsignaturaNotFoundException;

    Asignatura getAsignaturaPorId(long id) throws AsignaturaNotFoundException;

    void saveAsignaturas(List<Materia> listaMaterias);

    Asignatura saveAsignatura(Materia m);

    List<Asignatura> getListAsignaturas();


}
