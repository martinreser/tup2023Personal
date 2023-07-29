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
    public Materia save(final Materia materia, final int[] correlatividades) throws MateriaNotFoundException {
        materia.setMateriaId(RandomNumberCreator.getInstance().generateRandomNumber(999));
        repositorioMateria.put(materia.getMateriaId(), materia);
        final List<Materia> listaCorrelatividades = new ArrayList<>();
        for (Integer i : correlatividades) {
            Materia materia2 = findMateriaPorId(i);
            listaCorrelatividades.add(materia2);
        }
        materia.setCorrelatividades(listaCorrelatividades);
        return materia;
    }

    // GET
    // Encuentra una materia según un ID.
    @Override
    public Materia findMateriaPorId(final Integer id) throws MateriaNotFoundException {
        final Materia materia = repositorioMateria.get(id);
        if (materia == null) {
            throw new MateriaNotFoundException("No se encuentra ninguna materia con el ID: " + id);
        }
        return materia;
    }

    // Encuentra una materia según una cadena.
    @Override
    public List<Materia> findMateriaPorCadena(final String nombreMateria) throws MateriaNotFoundException {
        final List<Materia> listaFiltrada = new ArrayList<>();
        for (Materia materia : repositorioMateria.values()) {
            if (materia.getNombre().toLowerCase().startsWith(nombreMateria.toLowerCase())) {
                listaFiltrada.add(materia);
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
        final List<Materia> listaMaterias = new ArrayList<>();
        for (Materia materia : repositorioMateria.values()){
            listaMaterias.add(materia);
        }
        return listaMaterias;
    }

    public boolean comprobarNombreMaterias(final MateriaDto materia){
        for (Materia materia1: repositorioMateria.values()){
            if (materia1.getNombre().equals(materia.getNombre())){
                return false;
            }
        }
        return true;
    }
}
