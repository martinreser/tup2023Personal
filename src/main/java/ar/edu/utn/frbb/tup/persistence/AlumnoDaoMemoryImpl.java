package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Long, Alumno> repositorioAlumnos = new HashMap<>();

    // POST
    // Guarda el alumno.
    @Override
    public Alumno saveAlumno(Alumno alumno) {
        Random random = new Random();
        alumno.setId(random.nextLong(999));
        return repositorioAlumnos.put(alumno.getId(), alumno);
    }

    // GET
    // Devuelve la lista de Asignaturas de un Alumno.
    @Override
    public List<Asignatura> getAsignaturasAlumnoPorId(Long id) throws AlumnoNotFoundException {
        Alumno a = findAlumnoById(id);
        if (a == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        return a.obtenerListaAsignaturas();
    }

    // Devuelve una Asignatura en específico de un Alumno.
    @Override
    public Asignatura getAsignaturaAlumnoPorId(Long id, Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        Alumno a = findAlumnoById(id);
        if (a == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        for (Asignatura asig: a.obtenerListaAsignaturas()) {
            if (asig.getAsignaturaId().equals(idAsignatura)){
                return asig;
            }
        }
        throw new AsignaturaNotFoundException("El alumno " + a.getNombre() + " " + a.getApellido() + " (ID: " + id + "), no tiene " +
                "ninguna asignatura con el ID: " + idAsignatura + ".");
    }

    // Encuentra el alumno según una cadena.
    @Override
    public List<Alumno> findAlumnoByChain(String apellidoAlumno) throws AlumnoNotFoundException {
        List<Alumno> listaFiltrada = new ArrayList<>();
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getApellido().toLowerCase().startsWith(apellidoAlumno.toLowerCase())){
                listaFiltrada.add(a);
            }
        }
        if (listaFiltrada.isEmpty()){
            throw new AlumnoNotFoundException ("No se encuentra ningún alumno que comience con el apellido '" + apellidoAlumno + "'.");
        }
        return listaFiltrada;
    }

    // Encuentra el alumno según un ID.
    @Override
    public Alumno findAlumnoById(Long id) throws AlumnoNotFoundException {
        Alumno a = repositorioAlumnos.get(id);
        if (a == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        return a;
    }

    // PUT
    // Actualiza al alumno.
    @Override
    public void update(Long id, Alumno a) {
        repositorioAlumnos.put(id, a);
    }

    // DELETE
    // Borra al alumno que le pasamos por ID, y nos devuelve la lista de alumnos existentes (ya con el alumno eliminado).
    @Override
    public List<Alumno> deleteAlumnoById(Long id) throws AlumnoNotFoundException{
        Alumno a = repositorioAlumnos.get(id);
        if (a == null){
            throw new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: " + id + ".");
        }
        repositorioAlumnos.remove(id);
        List<Alumno> listaAlumnos = new ArrayList<>();
        for (Alumno a2: repositorioAlumnos.values()) {
            listaAlumnos.add(a2);
        }
        return listaAlumnos;
    }
}
