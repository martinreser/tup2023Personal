package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao dao;
    @Autowired
    private ProfesorService profesorService;

    @Override
    public Materia crearMateria(final MateriaDto materiadto) throws ProfesorNotFoundException, MateriaNotFoundException, YaExistenteException {
        Materia materia = new Materia();
        materia.setNombre(materiadto.getNombre());
        materia.setAnio(materiadto.getAnio());
        materia.setCuatrimestre(materiadto.getCuatrimestre());
        Profesor p = profesorService.buscarProfesorPorId(materiadto.getProfesorId());
        materia.setProfesor(p);
        profesorService.actualizarProfesor(p);
        dao.save(materia, materiadto.getCorrelatividades());
        return materia;
    }

    @Override
    public Materia buscarMateriaPorId(final Integer id) throws MateriaNotFoundException {
        return dao.findMateriaById(id);
    }

    @Override
    public List<Materia> buscarMateriaPorCadena(final String nombreMateria) throws MateriaNotFoundException {
        return dao.findMateriaByCadena(nombreMateria);
    }
    @Override
    public List<Materia> getAllMaterias() {
        return dao.getAllMaterias();
    }


}
