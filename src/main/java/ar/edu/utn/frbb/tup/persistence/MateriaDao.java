package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaConNombreYaCreadoException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface MateriaDao {

    // POST
    Materia save(Materia materia, int[] correlatividades) throws MateriaNotFoundException, MateriaConNombreYaCreadoException;

    // GET
    Materia findMateriaPorId(Integer id) throws MateriaNotFoundException;
    List<Materia> findMateriaPorCadena(String nombreMateria) throws MateriaNotFoundException;
    List<Materia> getAllMaterias();
    boolean comprobarNombreMaterias(MateriaDto m);
}
