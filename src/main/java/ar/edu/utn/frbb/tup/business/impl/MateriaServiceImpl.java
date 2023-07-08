package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
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
    public Materia crearMateria(MateriaDto materia) throws ProfesorNotFoundException {
        Materia m = new Materia();
        m.setNombre(materia.getNombre());
        m.setAnio(materia.getAnio());
        m.setCuatrimestre(materia.getCuatrimestre());
        Profesor p = profesorService.buscarProfesorPorId(materia.getProfesorId());
        m.setProfesor(p);
        profesorService.actualizarProfesor(p);
        dao.save(m, materia.getCorrelatividades());
        return m;
    }

    @Override
    public Materia buscarMateriaPorId(Integer id) throws MateriaNotFoundException {
        return dao.findMateriaPorId(id);
    }

    @Override
    public List<Materia> buscarMateriaPorCadena(String nombreMateria) throws MateriaNotFoundException {
        return dao.findMateriaPorCadena(nombreMateria);
    }
    @Override
    public List<Asignatura> getAllMaterias() {
        return dao.getAllMaterias();
    }


}
