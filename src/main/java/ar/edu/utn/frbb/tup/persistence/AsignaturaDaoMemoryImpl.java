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
    public void update(Asignatura a) throws AsignaturaNotFoundException {
        Asignatura a2 = getAsignaturaPorId(a.getAsignaturaId());
        repositorioAsignaturas.put(a2.getAsignaturaId(), a2);
    }

    @Override
    public Asignatura getAsignaturaPorId(long id) throws AsignaturaNotFoundException {
        Asignatura a = repositorioAsignaturas.get(id);
        if (a == null){
            throw new AsignaturaNotFoundException("No se encuentra ninguna asignatura con el ID: " + id);
        }
        return a;
    }

    @Override
    public Asignatura saveAsignatura(Materia m) {
        Asignatura a = new Asignatura(m, RandomNumberCreator.getInstance().generateRandomNumber(999));
        repositorioAsignaturas.put(a.getAsignaturaId(), a);
        return a;
    }

    @Override
    public void saveAsignaturas(List<Materia> listaMaterias){
        List<Asignatura> listaAsignaturas = new ArrayList<>();
        for (Materia m : listaMaterias){
            saveAsignatura(m);
        }
    }

    @Override
    public List<Asignatura> getListAsignaturas(){
        List<Asignatura> listaAsignaturas = new ArrayList<>();
        for (Asignatura a : repositorioAsignaturas.values()){
            listaAsignaturas.add(a);
        }
        return listaAsignaturas;
    }
}
