package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaConNombreYaCreadoException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    // MÉTODOS POST
    // Crear materia según body (DTO).
    @PostMapping
    public Materia crearMateria(@RequestBody MateriaDto materiaDto) throws ProfesorNotFoundException, MateriaNotFoundException, MateriaConNombreYaCreadoException {
        return materiaService.crearMateria(materiaDto);
    }

    // MÉTODOS GET
    // Buscar materia según ID.
    @GetMapping("/{idMateria}")
    public Materia buscarMateriaPorID(@PathVariable("idMateria") Integer id) throws MateriaNotFoundException {
        return materiaService.buscarMateriaPorId(id);
    }

    // Buscar materia según cadena ingresada.
    @GetMapping
    public List<Materia> buscarMateriaPorCadena(@RequestParam String nombre) throws MateriaNotFoundException {
        return materiaService.buscarMateriaPorCadena(nombre);
    }
}
