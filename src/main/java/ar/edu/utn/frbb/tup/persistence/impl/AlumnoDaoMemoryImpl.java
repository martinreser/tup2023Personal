package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.RandomNumberCreator;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorEliminadoCorrectamente;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Long, Alumno> repositorioAlumnos = new HashMap<>();

    // POST
    // Guarda el alumno.
    @Override
    public Alumno saveAlumno(final Alumno alumno) {
        alumno.setId(RandomNumberCreator.getInstance().generateRandomNumber(999));
        return repositorioAlumnos.put(alumno.getId(), alumno);
    }

    // GET
    // Devuelve la lista de Asignaturas de un Alumno.
    @Override
    public List<Asignatura> getAsignaturasAlumnoPorId(final Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = findAlumnoById(id);
        if (alumno== null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        List<Asignatura> asignaturas = alumno.obtenerListaAsignaturas();
        if (asignaturas.isEmpty()){
            throw new AsignaturaNotFoundException("La lista de asignaturas del alumno " + alumno.getNombre() +
                    alumno.getApellido() + " está vacía.");
        }
        return asignaturas;
    }

    // Devuelve una Asignatura en específico de un Alumno.
    @Override
    public Asignatura getAsignaturaAlumnoPorId(final Long id, final Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno alumno = findAlumnoById(id);
        if (alumno == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        for (Asignatura asignatura: alumno.obtenerListaAsignaturas()) {
            if (asignatura.getAsignaturaId().equals(idAsignatura)){
                return asignatura;
            }
        }
        throw new AsignaturaNotFoundException("El alumno " + alumno.getNombre() + " " + alumno.getApellido() + " (ID: " + id + "), no tiene " +
                "ninguna asignatura con el ID: " + idAsignatura + ".");
    }

    // Encuentra el alumno según una cadena.
    @Override
    public List<Alumno> findAlumnoByChain(final String apellidoAlumno) throws AlumnoNotFoundException {
        final List<Alumno> listaFiltrada = new ArrayList<>();
        for (Alumno alumno: repositorioAlumnos.values()) {
            if (alumno.getApellido().toLowerCase().startsWith(apellidoAlumno.toLowerCase())){
                listaFiltrada.add(alumno);
            }
        }
        if (listaFiltrada.isEmpty()){
            throw new AlumnoNotFoundException ("No se encuentra ningún alumno que comience con el apellido '" + apellidoAlumno + "'.");
        }
        return listaFiltrada;
    }

    // Encuentra el alumno según un ID.
    @Override
    public Alumno findAlumnoById(final Long id) throws AlumnoNotFoundException {
        Alumno alumno = repositorioAlumnos.get(id);
        if (alumno == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        return alumno;
    }

    // PUT
    // Actualiza al alumno.
    @Override
    public void update(final Long id, final Alumno alumno) {

        repositorioAlumnos.put(id, alumno);
    }

    // DELETE
    // Borra al alumno que le pasamos por ID, y nos devuelve la lista de alumnos existentes (ya con el alumno eliminado).
    @Override
    public List<Alumno> deleteAlumnoById(final Long id) throws AlumnoNotFoundException, AlumnoEliminadoCorrectamente {
        final Alumno alumno = repositorioAlumnos.get(id);
        if (alumno == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        repositorioAlumnos.remove(id);
        final List<Alumno> listaAlumnos = new ArrayList<>();
        for (Alumno a2: repositorioAlumnos.values()) {
            listaAlumnos.add(a2);
        }
        if (listaAlumnos.isEmpty()){
            throw new AlumnoEliminadoCorrectamente("El alumno " + alumno.getNombre() + " " + alumno.getApellido() +
                    " [ID: " + alumno.getId() + "], fue eliminado correctamente.\nYa no quedan alumnos disponibles.");
        }
        return listaAlumnos;
    }


}
