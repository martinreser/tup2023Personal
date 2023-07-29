package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao{

    private static final Map<Long, Profesor> repositorioProfesores = new HashMap<>();

    // POST
    // Guarda el profesor.
    @Override
    public Profesor save(final Profesor profesor) {
        profesor.setId(RandomNumberCreator.getInstance().generateRandomNumber(999));
        repositorioProfesores.put(profesor.getId(),profesor);
        return profesor;
    }

    // GET
    // Encuentra el profesor según un ID.
    @Override
    public Profesor findProfesorById(final long id) throws ProfesorNotFoundException {
        final Profesor profesor = repositorioProfesores.get(id);
        if (profesor == null){
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        return profesor;
    }

    // Encuentra el profesor según una cadena.
    @Override
    public List<Profesor> findProfesorByChain(final String apellidoProfesor) throws ProfesorNotFoundException {
        final List<Profesor> listaFiltrada = new ArrayList<>();
        for (Profesor profesor : repositorioProfesores.values()){
            if (profesor.getApellido().toLowerCase().startsWith(apellidoProfesor.toLowerCase())){
                listaFiltrada.add(profesor);
            }
        }
        if (listaFiltrada.isEmpty()){
            throw new ProfesorNotFoundException("No se encuentra ningún profesor que comience con el apellido '" + apellidoProfesor + "'.");
        }
        return listaFiltrada;
    }

    // Devuelve las materias que dicta un profesor específico.
    @Override
    public List<Materia> getMateriasDictadas(final Long id) throws ProfesorNotFoundException {
        final Profesor profesor = repositorioProfesores.get(id);
        if (profesor == null){
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        List<Materia> listaMaterias = profesor.getMateriasDictadas();
        Collections.sort(listaMaterias, new Comparator<Materia>() {
            @Override
            public int compare(Materia materia1, Materia materia2) {
                return materia1.getNombre().compareTo(materia2.getNombre());
            }
        });
        return listaMaterias;
    }

    // PUT
    // Actualiza al profesor.
    @Override
    public void update(final Long id, final Profesor p) {
        repositorioProfesores.put(id, p);
    }

    // DELETE
    // Borra al profesor que le pasamos por ID, y nos devuelve la lista de profesores existentes (ya con el profesor eliminado).
    @Override
    public List<Profesor> deleteProfesorPorId(final Long id) throws ProfesorNotFoundException {
        final Profesor profesor = findProfesorById(id);
        if (profesor == null){
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        repositorioProfesores.remove(id);
        List<Profesor> listaProfesores = new ArrayList<>();
        for (Profesor profesor1: repositorioProfesores.values()) {
            listaProfesores.add(profesor1);
        }
        return listaProfesores;
    }
}
