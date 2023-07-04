package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.NotFoundException;

public interface MateriaDao {
    public Materia save(Materia materia);

    Materia buscarMateriaPorId(Integer id) throws NotFoundException;
}
