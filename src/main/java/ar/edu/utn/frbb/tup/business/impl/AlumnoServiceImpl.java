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
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

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
        List<Asignatura> asignaturas = asignaturaService.crearListaAsignaturas();
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
    public void aprobarAsignatura(int materiaId, int nota, long id) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AlumnoNotFoundException {
        Asignatura a = asignaturaService.getAsignatura(materiaId, id);
        for (Materia m:
             a.getMateria().getCorrelatividades()) {
            Asignatura correlativa = asignaturaService.getAsignatura(m.getMateriaId(), id);
            if (!EstadoAsignatura.APROBADA.equals(correlativa.getEstado())) {
                throw new CorrelatividadesNoAprobadasException("La materia " + m.getNombre() + " debe estar aprobada para aprobar " + a.getNombreAsignatura());
            }
        }
        a.aprobarAsignatura(nota);
        asignaturaService.actualizarAsignatura(a);
        Alumno alumno = alumnoDao.findAlumnoById(id);
        alumno.actualizarAsignatura(a);
        alumnoDao.saveAlumno(alumno);
    }

    @Override
    public List<Alumno> borrarAlumnoPorId(Long id) throws AlumnoNotFoundException {
        return alumnoDao.deleteAlumnoById(id);
    }
}
