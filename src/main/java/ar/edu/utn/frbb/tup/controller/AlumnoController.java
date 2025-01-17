package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    // MÉTODOS POST
    // Crear alumno según body (DTO).
    @PostMapping
    public Alumno crearAlumno(@RequestBody AlumnoDto alumnoDto) throws DatoInvalidoException {
        return alumnoService.crearAlumno(alumnoDto);
    }

    // MÉTODOS GET
    // Buscar asignaturas de un alumno por ID.
    @GetMapping("/{idAlumno}/asignaturas")
    public List<Asignatura> obtenerAsignaturasAlumnoPorId(@PathVariable("idAlumno") Long id) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoService.obtenerAsignaturasAlumnoPorId(id);
    }

    // Buscar una asignatura específica de un alumno.
    @GetMapping("/{idAlumno}/asignaturas/{idAsignatura}")
    public Asignatura obtenerAsignaturaAlumnoPorId(@PathVariable("idAlumno") Long id,
                                                   @PathVariable ("idAsignatura") Long idAsignatura) throws AlumnoNotFoundException, AsignaturaNotFoundException {
        return alumnoService.obtenerAsignaturaAlumnoPorId(id, idAsignatura);
    }

    // Buscar alumno según cadena ingresada.
    @GetMapping
    public List<Alumno> buscarAlumnoPorCadena(@RequestParam String apellido) throws AlumnoNotFoundException {
       return alumnoService.buscarAlumnoPorCadena(apellido);
    }

    // Buscar alumno según ID.
    @GetMapping("/{idAlumno}")
    public Alumno buscarAlumnoPorId(@PathVariable("idAlumno") Long id) throws AlumnoNotFoundException {
        return alumnoService.buscarAlumnoPorId(id);
    }


    // MÉTODOS PUT
    // Actualizar alumno según body (DTO).
    @PutMapping("/{idAlumno}")
    public Alumno actualizarAlumnoPorId(@PathVariable("idAlumno") Long idAlumno,
                                        @RequestBody AlumnoDto alumnoDto) throws AlumnoNotFoundException {
        return alumnoService.actualizarAlumnoPorId(idAlumno, alumnoDto);
    }

    // Actualizar estado de la asignatura de un alumno según ID.
    @PutMapping("/{idAlumno}/asignaturas/{idAsignatura}")
    public Asignatura actualizarEstadoAsignaturaPorId(@PathVariable("idAlumno") Long idAlumno,
                                                  @PathVariable("idAsignatura") Long idAsignatura,
                                                  @RequestBody AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException,
            CorrelatividadesNoAprobadasException, EstadoIncorrectoException, NotaNoValidaException, CambiarEstadoAsignaturaException {
        return alumnoService.actualizarEstadoAsignaturaPorID(idAlumno, idAsignatura, asignaturaDto);
    }

    // MÉTODOS DELETE
    // Borrar alumno según ID.
    @DeleteMapping("/{idAlumno}")
    public List<Alumno> borrarAlumnoPorId(@PathVariable("idAlumno") Long id) throws AlumnoNotFoundException, AlumnoEliminadoCorrectamente {
        return alumnoService.borrarAlumnoPorId(id);
    }
}
