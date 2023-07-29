package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoDao alumnoDao;

    @Autowired
    private AsignaturaService asignaturaService;

    @Override
    public Alumno crearAlumno(final AlumnoDto alumno) {
        final Alumno a = new Alumno();
        a.setNombre(alumno.getNombre());
        a.setApellido(alumno.getApellido());
        a.setDni(alumno.getDni());
        final List<Asignatura> asignaturas = asignaturaService.obtenerListaAsignaturas();
        a.setAsignaturas(asignaturas);
        alumnoDao.saveAlumno(a);
        return a;
    }

    @Override
    public List<Alumno> buscarAlumnoPorCadena(final String apellido) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoByChain(apellido);
    }

    @Override
    public Alumno buscarAlumnoPorId(final Long id) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoById(id);
    }

    @Override
    public List<Asignatura> obtenerAsignaturasAlumnoPorId(final Long id) throws AlumnoNotFoundException {
        return alumnoDao.getAsignaturasAlumnoPorId(id);
    }

    @Override
    public Asignatura obtenerAsignaturaAlumnoPorId(final Long id, final Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoDao.getAsignaturaAlumnoPorId(id, idAsignatura);
    }

    @Override
    public Alumno actualizarAlumnoPorId(final Long idAlumno, final AlumnoDto alumnoDto) throws AlumnoNotFoundException {
        final Alumno a = alumnoDao.findAlumnoById(idAlumno);
        a.setId(idAlumno);
        if (alumnoDto.getNombre() != null){
            a.setNombre(alumnoDto.getNombre());
        }
        if (alumnoDto.getApellido() != null){
            a.setApellido(alumnoDto.getApellido());;
        }
        if (alumnoDto.getDni() != 0){
            a.setDni(alumnoDto.getDni());
        }
        alumnoDao.update(idAlumno, a);
        return a;
    }

    @Override
    public Asignatura actualizarEstadoAsignaturaPorID(final Long idAlumno, final Long idAsignatura, final AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException,
            AlumnoNotFoundException, AsignaturaNotFoundException, NotaNoValidaException, CambiarEstadoAsignaturaException {
        final Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        final Asignatura asignatura = asignaturaService.getAsignaturaPorId(idAsignatura);
        if (asignaturaDto.getCondicion().equals(EstadoAsignatura.APROBADA)){
            comprobarNota(asignaturaDto.getNota());
            alumno.aprobarAsignatura(asignatura, asignaturaDto.getNota());
        }
        else if (asignaturaDto.getCondicion().equals(EstadoAsignatura.CURSADA)){
            alumno.cursarAsignatura(asignatura);
        }
        else {
            throw new CambiarEstadoAsignaturaException("La condición de la materia solo puede ser cambiada a 'Cursada' o 'Aprobada'.");
        }
        asignaturaService.actualizarAsignatura(asignatura);
        alumno.actualizarAsignatura(asignatura);
        alumnoDao.saveAlumno(alumno);
        return asignatura;
    }

    @Override
    public List<Alumno> borrarAlumnoPorId(final Long id) throws AlumnoNotFoundException {
        return alumnoDao.deleteAlumnoById(id);
    }

    private boolean comprobarNota(final int nota) throws NotaNoValidaException {
        if (nota == 0){
            throw new NotaNoValidaException("La nota no puede ser nula.");
        }
        if (nota > 10 || nota < 0){
            throw new NotaNoValidaException("La nota debe ser mayor a 0 y menor a 10.");
        }
        return true;
    }
}
