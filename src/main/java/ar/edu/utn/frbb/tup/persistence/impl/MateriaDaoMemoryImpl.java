package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.RandomNumberCreator;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();

    // POST
    // Guarda una materia.
    @Override
    public Materia save(final Materia materia, final int[] correlatividades) throws MateriaNotFoundException, YaExistenteException {
        comprobarNombreMaterias(materia);
        materia.setMateriaId(RandomNumberCreator.getInstance().generateRandomNumber(999));
        repositorioMateria.put(materia.getMateriaId(), materia);
        final List<Materia> listaCorrelatividades = new ArrayList<>();
        for (Integer i : correlatividades) {
            Materia materia2 = findMateriaById(i);
            listaCorrelatividades.add(materia2);
        }
        materia.setCorrelatividades(listaCorrelatividades);
        return materia;
    }

    // GET
    // Encuentra una materia según un ID.
    @Override
    public Materia findMateriaById(final Integer id) throws MateriaNotFoundException {
        final Materia materia = repositorioMateria.get(id);
        if (materia == null) {
            throw new MateriaNotFoundException("No se encuentra ninguna materia con el ID: " + id);
        }
        return materia;
    }

    // Encuentra una materia según una cadena.
    @Override
    public List<Materia> findMateriaByChain(final String nombreMateria) throws MateriaNotFoundException {
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

    @Override
    public void deleteMateriaById(final int materiaId) {
        repositorioMateria.remove(materiaId);
    }

    private boolean comprobarNombreMaterias(final Materia materia) throws YaExistenteException {
        for (Materia materia1: repositorioMateria.values()){
            if (materia1.getNombre().equals(materia.getNombre())){
                throw new YaExistenteException("Ya existe una materia con el nombre " + materia.getNombre());
            }
        }
        return true;
    }
}
