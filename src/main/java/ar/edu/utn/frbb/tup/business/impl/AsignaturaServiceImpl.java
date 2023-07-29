package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    AsignaturaDao asignaturaDao;

    @Autowired
    MateriaService materiaService;

    @Override
    public Asignatura getAsignaturaPorId(final long idAsignatura) throws AsignaturaNotFoundException {
        return asignaturaDao.getAsignaturaPorId(idAsignatura);
    }

    @Override
    public void actualizarAsignatura(final Asignatura a) throws AsignaturaNotFoundException {
        asignaturaDao.update(a);
    }

    @Override
    public List<Asignatura> obtenerListaAsignaturas() {
        final List<Materia> listaMaterias = materiaService.getAllMaterias();
        asignaturaDao.saveAsignaturas(listaMaterias);
        return asignaturaDao.getListAsignaturas();
    }
}
