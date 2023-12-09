package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;

import java.util.List;

public interface MateriaDao {

    // POST
    Materia save(Materia materia, int[] correlatividades) throws MateriaNotFoundException, YaExistenteException;

    // GET
    Materia findMateriaById(Integer id) throws MateriaNotFoundException;
    List<Materia> findMateriaByChain(String nombreMateria) throws MateriaNotFoundException;
    List<Materia> getAllMaterias();

    void deleteMateriaById(int materiaId);
}
