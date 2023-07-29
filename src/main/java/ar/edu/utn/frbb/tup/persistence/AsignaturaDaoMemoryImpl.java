package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private static final Map<Long, Asignatura> repositorioAsignaturas = new HashMap<>();


    @Override
    public void update(final Asignatura asignatura) throws AsignaturaNotFoundException {
        Asignatura asignatura2 = getAsignaturaPorId(asignatura.getAsignaturaId());
        repositorioAsignaturas.put(asignatura2.getAsignaturaId(), asignatura2);
    }

    @Override
    public Asignatura getAsignaturaPorId(final long id) throws AsignaturaNotFoundException {
        final Asignatura asignatura = repositorioAsignaturas.get(id);
        if (asignatura == null){
            throw new AsignaturaNotFoundException("No se encuentra ninguna asignatura con el ID: " + id);
        }
        return asignatura;
    }

    @Override
    public Asignatura saveAsignatura(final Materia materia) {
        final Asignatura asignatura = new Asignatura(materia, RandomNumberCreator.getInstance().generateRandomNumber(999));
        repositorioAsignaturas.put(asignatura.getAsignaturaId(), asignatura);
        return asignatura;
    }

    @Override
    public void saveAsignaturas(final List<Materia> listaMaterias){
        final List<Asignatura> listaAsignaturas = new ArrayList<>();
        for (Materia materia : listaMaterias){
            saveAsignatura(materia);
        }
    }

    @Override
    public List<Asignatura> getListAsignaturas(){
        final List<Asignatura> listaAsignaturas = new ArrayList<>();
        for (Asignatura asignatura : repositorioAsignaturas.values()){
            listaAsignaturas.add(asignatura);
        }
        return listaAsignaturas;
    }
}
