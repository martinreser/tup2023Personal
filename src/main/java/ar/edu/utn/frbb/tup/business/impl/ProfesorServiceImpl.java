package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    @Autowired
    private ProfesorDao profesorDao;

    @Override
    public Profesor crearProfesor(ProfesorDto profesorDto) {
        Profesor p = new Profesor();
        p.setTitulo(profesorDto.getTitulo());
        p.setApellido(profesorDto.getApellido());
        p.setNombre(profesorDto.getNombre());
        profesorDao.save(p);
        return p;
    }

    @Override
    public List<Profesor> buscarProfesorPorCadena(String apellido) throws ProfesorNotFoundException {
        return profesorDao.findProfesorByChain(apellido);
    }

    @Override
    public Profesor buscarProfesorPorId(Long id) throws ProfesorNotFoundException {
        return profesorDao.findProfesorById(id);
    }

    @Override
    public List<Materia> buscarMateriasDictadas(Long id) throws ProfesorNotFoundException {
        return profesorDao.getMateriasDictadas(id);
    }

    @Override
    public void actualizarProfesor(Profesor p) throws ProfesorNotFoundException {
        profesorDao.update(p.getId(), p);
    }

    @Override
    public Profesor actualizarProfesorPorId(Long idProfesor, ProfesorDto profesorDto) throws ProfesorNotFoundException {
        Profesor p = profesorDao.findProfesorById(idProfesor);
        p.setId(idProfesor);
        if (profesorDto.getNombre() != null){
            p.setNombre(profesorDto.getNombre());
        }
        if (profesorDto.getApellido() != null){
            p.setApellido(profesorDto.getApellido());;
        }
        if (profesorDto.getTitulo() != null){
            p.setTitulo(profesorDto.getTitulo());
        }
        profesorDao.update(idProfesor, p);
        return p;
    }

    @Override
    public List<Profesor> borrarProfesorPorId(Long id) throws ProfesorNotFoundException {
        return profesorDao.deleteProfesorPorId(id);
    }

}
