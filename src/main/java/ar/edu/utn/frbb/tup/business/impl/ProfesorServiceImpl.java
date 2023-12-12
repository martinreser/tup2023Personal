package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    @Autowired
    private ProfesorDao profesorDao;

    @Autowired
    private MateriaDao materiaDao;

    @Override
    public Profesor crearProfesor(final ProfesorDto profesorDto) throws DatoInvalidoException, YaExistenteException {
        comprobarNombre(profesorDto);
        comprobarApellido(profesorDto);
        comprobarTitulo(profesorDto);
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
    public Profesor actualizarProfesorPorId(final Long idProfesor, final ProfesorDto profesorDto) throws ProfesorNotFoundException, DatoInvalidoException {
        final Profesor profesor = profesorDao.findProfesorById(idProfesor);
        profesor.setId(idProfesor);
        if (profesorDto.getNombre() != null &&
                !profesorDto.getNombre().equals("") && !profesorDto.getNombre().matches(".*\\d+.*")){
            profesor.setNombre(profesorDto.getNombre());
        }
        if (profesorDto.getApellido() != null &&
                !profesorDto.getApellido().equals("") && !profesorDto.getApellido().matches(".*\\d+.*")){
            profesor.setApellido(profesorDto.getApellido());;
        }
        if (profesorDto.getTitulo() != null &&
                !profesorDto.getTitulo().equals("") && !profesorDto.getTitulo().matches(".*\\d+.*")){
            profesor.setTitulo(profesorDto.getTitulo());
        }
        profesorDao.update(idProfesor, profesor);
        return profesor;
    }

    @Override
    public List<Profesor> borrarProfesorPorId(Long id) throws ProfesorNotFoundException, ProfesorEliminadoCorrectamente {
        for (Materia materia: profesorDao.getMateriasDictadas(id)) {
            materiaDao.deleteMateriaById(materia.getMateriaId());
        }
        return profesorDao.deleteProfesorById(id);
    }

    private boolean comprobarNombre(final ProfesorDto profesorDto) throws DatoInvalidoException {
        if (null == profesorDto.getNombre() || profesorDto.getNombre().equals("")){
            throw new DatoInvalidoException("El nombre no puede ser nulo ni estar vacío.");
        }
        if (profesorDto.getNombre().matches(".*\\d+.*")){
            throw new DatoInvalidoException("El nombre no puede contener números.");
        }
        return true;
    }
    private boolean comprobarApellido(final ProfesorDto profesorDto) throws DatoInvalidoException {
        if (null == profesorDto.getApellido() || profesorDto.getApellido().equals("")){
            throw new DatoInvalidoException("El apellido no puede ser nulo ni estar vacío.");
        }
        if (profesorDto.getApellido().matches(".*\\d+.*")){
            throw new DatoInvalidoException("El apellido no puede contener números.");
        }
        return true;
    }
    private boolean comprobarTitulo(final ProfesorDto profesorDto) throws DatoInvalidoException {
        if (null == profesorDto.getTitulo() || profesorDto.getTitulo().equals("")){
            throw new DatoInvalidoException("El título no puede ser nulo ni estar vacío.");
        }
        if (profesorDto.getTitulo().matches(".*\\d+.*")){
            throw new DatoInvalidoException("El título no puede contener números decimales, recuerde anotarlo con" +
                    " números romanos. Ejemplo: 4 --> IV");
        }
        return true;
    }
}
