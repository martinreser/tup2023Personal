package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    public Alumno crearAlumno(AlumnoDto alumno) {
        Alumno a = new Alumno();
        a.setNombre(alumno.getNombre());
        a.setApellido(alumno.getApellido());
        a.setDni(alumno.getDni());
        List<Asignatura> asignaturas = asignaturaService.obtenerListaAsignaturas();
        a.setAsignaturas(asignaturas);
        alumnoDao.saveAlumno(a);
        return a;
    }

    @Override
    public List<Alumno> buscarAlumnoPorCadena(String apellido) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoByChain(apellido);
    }

    @Override
    public Alumno buscarAlumnoPorId(Long id) throws AlumnoNotFoundException {
        return alumnoDao.findAlumnoById(id);
    }

    @Override
    public List<Asignatura> obtenerAsignaturasAlumnoPorId(Long id) throws AlumnoNotFoundException {
        return alumnoDao.getAsignaturasAlumnoPorId(id);
    }

    @Override
    public Asignatura obtenerAsignaturaAlumnoPorId(Long id, Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoDao.getAsignaturaAlumnoPorId(id, idAsignatura);
    }

    @Override
    public Alumno actualizarAlumnoPorId(Long idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException {
        Alumno a = alumnoDao.findAlumnoById(idAlumno);
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
    public Asignatura actualizarEstadoAsignaturaPorID(Long idAlumno, Long idAsignatura, AsignaturaDto asignaturaDto) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException,
            AlumnoNotFoundException, AsignaturaNotFoundException, NotaNoValidaException, CambiarEstadoAsignaturaException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        Asignatura a = asignaturaService.getAsignaturaPorId(idAsignatura);
        if (asignaturaDto.getCondicion().toLowerCase().equals("aprobada")){
            for (Materia m: a.getMateria().getCorrelatividades()) {
                Asignatura correlativa = asignaturaService.getAsignaturaPorId(idAsignatura);
                if (!EstadoAsignatura.APROBADA.equals(correlativa.getEstado())) {
                    throw new CorrelatividadesNoAprobadasException("La materia " + m.getNombre() + " [ID: " + correlativa.getAsignaturaId() + "] debe estar aprobada para aprobar " + a.getNombreAsignatura());
                }
            }
            comprobarNota(asignaturaDto.getNota());
            a.aprobarAsignatura(asignaturaDto.getNota());
        }
        else if (asignaturaDto.getCondicion().toLowerCase().equals("cursada")){
            a.cursarAsignatura();
        }
        else {
            throw new CambiarEstadoAsignaturaException("La condición de la materia solo puede ser cambiada a 'Cursada' o 'Aprobada'.");
        }
        asignaturaService.actualizarAsignatura(a);
        alumno.actualizarAsignatura(a);
        alumnoDao.saveAlumno(alumno);
        return a;
    }

    @Override
    public List<Alumno> borrarAlumnoPorId(Long id) throws AlumnoNotFoundException {
        return alumnoDao.deleteAlumnoById(id);
    }

    private boolean comprobarNota(int nota) throws NotaNoValidaException {
        if (nota == 0){
            throw new NotaNoValidaException("La nota no puede ser nula.");
        }
        if (nota > 10 || nota < 0){
            throw new NotaNoValidaException("La nota debe ser mayor a 0 y menor a 10.");
        }
        return true;
    }
}
