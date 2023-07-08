package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("profesor")
public class ProfesorController {

    @Autowired
    ProfesorService profesorService;

    // MÉTODOS POST
    // Crear profesor según body (DTO).
    @PostMapping
    public Profesor crearProfesor(@RequestBody ProfesorDto profesorDto){
        return profesorService.crearProfesor(profesorDto);
    }

    // MÉTODOS GET
    // Buscar materias que un determinado profesor dicta según ID.
    @GetMapping("/{idProfesor}/materias")
    public List<Materia> buscarMateriasDictadas(@PathVariable ("idProfesor") Long id) throws ProfesorNotFoundException {
        return profesorService.buscarMateriasDictadas(id);
    }

    // Buscar profesores según cadena ingresada.
    @GetMapping
    public List<Profesor> buscarProfesorPorCadena(@RequestParam String apellido) throws ProfesorNotFoundException {
        return profesorService.buscarProfesorPorCadena(apellido);
    }

    // Buscar profesor según ID.
    @GetMapping("/{idProfesor}")
    public Profesor buscarProfesorPorId(@PathVariable ("idProfesor") Long id) throws ProfesorNotFoundException {
        return profesorService.buscarProfesorPorId(id);
    }

    // MÉTODOS PUT
    // Actualizar profesor según body (DTO).
    @PutMapping("/{idProfesor}")
    public Profesor actualizarProfesorPorId(@PathVariable("idProfesor") Long idProfesor,
                                          @RequestBody ProfesorDto profesorDto) throws ProfesorNotFoundException {
        return profesorService.actualizarProfesorPorId(idProfesor, profesorDto);
    }

    // MÉTODOS DELETE
    // Borrar profesor según ID.
    @DeleteMapping("/{idProfesor}")
    public List<Profesor> borrarProfesorPorId(@PathVariable("idProfesor") Long id) throws ProfesorNotFoundException {
        return profesorService.borrarProfesorPorId(id);
    }
}
