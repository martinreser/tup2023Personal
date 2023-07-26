package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaConNombreYaCreadoException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();

    // POST
    // Guarda una materia.
    @Override
    public Materia save(Materia materia, int[] correlatividades) throws MateriaNotFoundException {
        materia.setMateriaId(RandomNumberCreator.getInstance().generateRandomNumber(999));
        repositorioMateria.put(materia.getMateriaId(), materia);
        List<Materia> listaCorrelatividades = new ArrayList<>();
        for (Integer i : correlatividades) {
            Materia m = findMateriaPorId(i);
            listaCorrelatividades.add(m);
        }
        materia.setCorrelatividades(listaCorrelatividades);
        return materia;
    }

    // GET
    // Encuentra una materia según un ID.
    @Override
    public Materia findMateriaPorId(Integer id) throws MateriaNotFoundException {
        Materia m = repositorioMateria.get(id);
        if (m == null) {
            throw new MateriaNotFoundException("No se encuentra ninguna materia con el ID: " + id);
        }
        return m;
    }

    // Encuentra una materia según una cadena.
    @Override
    public List<Materia> findMateriaPorCadena(String nombreMateria) throws MateriaNotFoundException {
        List<Materia> listaFiltrada = new ArrayList<>();
        for (Materia m : repositorioMateria.values()) {
            if (m.getNombre().toLowerCase().startsWith(nombreMateria.toLowerCase())) {
                listaFiltrada.add(m);
            }
        }
        if (listaFiltrada.isEmpty()) {
            throw new MateriaNotFoundException("No se encuentra ninguna materia que comience con el nombre '" + nombreMateria + "'.");
        }
        return listaFiltrada;
    }

    // Devuelve todas las materias.
    @Override
    public List<Materia> getAllMaterias() {
        List<Materia> listaMaterias = new ArrayList<>();
        for (Materia m : repositorioMateria.values()){
            listaMaterias.add(m);
        }
        return listaMaterias;
    }

    public boolean comprobarNombreMaterias(MateriaDto materia){
        for (Materia m: repositorioMateria.values()){
            if (m.getNombre().equals(materia.getNombre())){
                return false;
            }
        }
        return true;
    }
}
