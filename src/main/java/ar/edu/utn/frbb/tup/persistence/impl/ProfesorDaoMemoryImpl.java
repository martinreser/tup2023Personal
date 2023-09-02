package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.RandomNumberCreator;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao {

    private static final Map<Long, Profesor> repositorioProfesores = new HashMap<>();

    // POST
    // Guarda el profesor.
    @Override
    public Profesor save(final Profesor profesor) throws YaExistenteException {
        profesorExistente(profesor);
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
    public List<Profesor> deleteProfesorById(final Long id) throws ProfesorNotFoundException, ProfesorEliminadoCorrectamente {
        Profesor profesor = findProfesorById(id);
        if (profesor == null) {
            throw new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: " + id + ".");
        }
        repositorioProfesores.remove(id);
        List<Profesor> listaProfesores = new ArrayList<>();
        for (Profesor profesor1 : repositorioProfesores.values()) {
            listaProfesores.add(profesor1);
        }
        if (listaProfesores.isEmpty()){
            throw new ProfesorEliminadoCorrectamente ("El profesor " + profesor.getNombre() + " " + profesor.getApellido() +
                    " [ID: " + profesor.getId() + "], fue eliminado correctamente.\nYa no quedan profesores disponibles.");
        }
        return listaProfesores;
    }

    private boolean profesorExistente(final Profesor profesor) throws YaExistenteException {
        for (Profesor profesor1 : repositorioProfesores.values()) {
            if (profesor.getNombre().equals(profesor1.getNombre()) &&
                    profesor.getApellido().equals(profesor1.getApellido())) {
                throw new YaExistenteException("Ya existe un profesor con los datos ingresados [" +
                        profesor.getNombre() + " " + profesor.getApellido() + "].");
            }
        }
        return false;
    }
}
