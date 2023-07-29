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
    public Profesor crearProfesor(final ProfesorDto profesorDto) {
        final Profesor profesor = new Profesor();
        profesor.setTitulo(profesorDto.getTitulo());
        profesor.setApellido(profesorDto.getApellido());
        profesor.setNombre(profesorDto.getNombre());
        profesorDao.save(profesor);
        return profesor;
    }

    @Override
    public List<Profesor> buscarProfesorPorCadena(final String apellido) throws ProfesorNotFoundException {
        return profesorDao.findProfesorByChain(apellido);
    }

    @Override
    public Profesor buscarProfesorPorId(final Long id) throws ProfesorNotFoundException {
        return profesorDao.findProfesorById(id);
    }

    @Override
    public List<Materia> buscarMateriasDictadas(final Long id) throws ProfesorNotFoundException {
        return profesorDao.getMateriasDictadas(id);
    }

    @Override
    public void actualizarProfesor(final Profesor profesor) throws ProfesorNotFoundException {
        profesorDao.update(profesor.getId(), profesor);
    }

    @Override
    public Profesor actualizarProfesorPorId(final Long idProfesor, final ProfesorDto profesorDto) throws ProfesorNotFoundException {
        final Profesor profesor = profesorDao.findProfesorById(idProfesor);
        profesor.setId(idProfesor);
        if (profesorDto.getNombre() != null){
            profesor.setNombre(profesorDto.getNombre());
        }
        if (profesorDto.getApellido() != null){
            profesor.setApellido(profesorDto.getApellido());;
        }
        if (profesorDto.getTitulo() != null){
            profesor.setTitulo(profesorDto.getTitulo());
        }
        profesorDao.update(idProfesor, profesor);
        return profesor;
    }

    @Override
    public List<Profesor> borrarProfesorPorId(Long id) throws ProfesorNotFoundException {
        return profesorDao.deleteProfesorPorId(id);
    }

}
