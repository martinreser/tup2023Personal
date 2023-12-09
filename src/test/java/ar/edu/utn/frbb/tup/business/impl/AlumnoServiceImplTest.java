package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AlumnoServiceImplTest {

    @InjectMocks
    AlumnoServiceImpl alumnoService;

    @Mock
    AlumnoDao alumnoDao;

    @Mock
    AsignaturaService asignaturaService;

    @Test
    public void crearAlumno() throws DatoInvalidoException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan");
        alumnoDto.setApellido("Perez");
        alumnoDto.setDni(12345678);


        List<Asignatura> asignaturas = new ArrayList<>();
        when(asignaturaService.obtenerListaAsignaturas()).thenReturn(asignaturas);

        Alumno alumnoCreado = alumnoService.crearAlumno(alumnoDto);


        assertEquals(alumnoDto.getNombre(), alumnoCreado.getNombre());
        assertEquals(alumnoDto.getApellido(), alumnoCreado.getApellido());
        assertEquals(alumnoDto.getDni(), alumnoCreado.getDni());

        verify(alumnoDao, times(1)).saveAlumno(any(Alumno.class));
    }

    @Test
    public void crearAlumnoConErroresEnDTO() throws DatoInvalidoException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan1");
        alumnoDto.setApellido("Perez");
        alumnoDto.setDni(123);

        List<Asignatura> asignaturas = new ArrayList<>();
        when(asignaturaService.obtenerListaAsignaturas()).thenReturn(asignaturas);

        assertThrows(DatoInvalidoException.class, () -> {
            alumnoService.crearAlumno(alumnoDto);
        });
    }

    @Test
    public void buscarAlumnoPorCadenaCorrecta() throws AlumnoNotFoundException {
        List<Alumno> alumnos = new ArrayList<>();
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumnos.add(alumno);
        when(alumnoDao.findAlumnoByChain("Res")).thenReturn(alumnos);
        List<Alumno> alumnosEncontrados = alumnoService.buscarAlumnoPorCadena("Res");
        assertEquals(alumnos, alumnosEncontrados);
        verify(alumnoDao, times(1)).findAlumnoByChain("Res");
    }

    @Test
    public void buscarAlumnoPorCadenaIncorrecta() throws AlumnoNotFoundException {
        List<Alumno> alumnos = new ArrayList<>();
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        alumnos.add(alumno);
        when(alumnoDao.findAlumnoByChain("Gom")).thenThrow(new AlumnoNotFoundException(
                "No se encuentra ningún alumno que comience con el apellido 'Gom'."));
        assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoService.buscarAlumnoPorCadena("Gom");
        });
    }

    @Test
    public void buscarAlumnoPorIdCorrecto() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumno);
        Alumno alumnoEncontrado = alumnoService.buscarAlumnoPorId(1L);
        assertEquals(alumno, alumnoEncontrado);
        verify(alumnoDao, times(1)).findAlumnoById(1L);
    }

    @Test
    public void buscarAlumnoPorIdIncorrecto() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno("Martin", "Reser", 45319502);
        when(alumnoDao.findAlumnoById(2L)).thenThrow(new AlumnoNotFoundException(
                "No se encuentra ningún alumno con el ID: 2."));
        assertThrows(AlumnoNotFoundException.class, () -> {
            alumnoService.buscarAlumnoPorId(2L);
        });
    }

    @Test
    public void obtenerAsignaturasAlumnoPorId() throws AsignaturaNotFoundException, AlumnoNotFoundException {
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(new Asignatura());
        when(alumnoDao.getAsignaturasAlumnoPorId(1L)).thenReturn(asignaturas);
        List<Asignatura> asignaturasAlumno = alumnoService.obtenerAsignaturasAlumnoPorId(1L);
        assertEquals(asignaturas, asignaturasAlumno);
        verify(alumnoDao, times(1)).getAsignaturasAlumnoPorId(1L);
    }

    @Test
    public void obtenerAsignaturaAlumnoPorIdCorrecto() throws AsignaturaNotFoundException, AlumnoNotFoundException {
        Asignatura asignatura = new Asignatura();
        when(alumnoDao.getAsignaturaAlumnoPorId(1L, 1L)).thenReturn(asignatura);
        Asignatura asignaturaEncontrada = alumnoService.obtenerAsignaturaAlumnoPorId(1L, 1L);
        assertEquals(asignatura, asignaturaEncontrada);
        verify(alumnoDao, times(1)).getAsignaturaAlumnoPorId(1L, 1L);
    }

    @Test
    public void obtenerAsignaturaAlumnoPorIdIncorrecto() throws AsignaturaNotFoundException, AlumnoNotFoundException {
        Asignatura asignatura = new Asignatura();
        when(alumnoDao.getAsignaturaAlumnoPorId(1L, 1L)).thenReturn(asignatura);
        Asignatura asignaturaEncontrada = alumnoService.obtenerAsignaturaAlumnoPorId(1L, 1L);
        when(alumnoDao.getAsignaturaAlumnoPorId(1L, 1L)).thenThrow(new AsignaturaNotFoundException(
                "El alumno Martin Reser (ID: 1), no tiene ninguna asignatura con el ID: 1."
        ));
        assertThrows(AsignaturaNotFoundException.class, () -> {
            alumnoService.obtenerAsignaturaAlumnoPorId(1L, 1L);
        });
    }

    @Test
    public void actualizarAlumnoPorId() throws AlumnoNotFoundException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan");
        alumnoDto.setApellido("Perez");
        alumnoDto.setDni(45319502);

        Alumno alumnoEncontrado = new Alumno("Juan", "Gomez", 45319502);
        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumnoEncontrado);

        Alumno alumnoActualizado = alumnoService.actualizarAlumnoPorId(1L, alumnoDto);

        assertEquals(alumnoDto.getNombre(), alumnoActualizado.getNombre());
        assertEquals(alumnoDto.getApellido(), alumnoActualizado.getApellido());
        assertEquals(alumnoDto.getDni(), alumnoActualizado.getDni());
        verify(alumnoDao, times(1)).update(1L, alumnoActualizado);
    }

    @Test
    public void actualizarEstadoAsignaturaPorIDCursar() throws AlumnoNotFoundException, AsignaturaNotFoundException, CorrelatividadesNoAprobadasException, CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setCondicion(EstadoAsignatura.CURSADA);

        Asignatura asignaturaEncontrada = new Asignatura(new Materia("Lab", 1, 1, new Profesor()), 1L);
        when(asignaturaService.getAsignaturaPorId(1L)).thenReturn(asignaturaEncontrada);

        Alumno alumnoEncontrado = new Alumno("Juan", "Gomez", 45319502);
        alumnoEncontrado.agregarAsignatura(asignaturaEncontrada);
        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumnoEncontrado);

        Asignatura asignaturaActualizada = alumnoService.actualizarEstadoAsignaturaPorID(1L, 1L, asignaturaDto);

        assertEquals(asignaturaActualizada.getEstado(), EstadoAsignatura.CURSADA);
        verify(alumnoDao, times(1)).saveAlumno(alumnoEncontrado);
    }

    @Test
    public void actualizarEstadoAsignaturaPorIDAprobar() throws AlumnoNotFoundException, AsignaturaNotFoundException, CorrelatividadesNoAprobadasException, CambiarEstadoAsignaturaException, EstadoIncorrectoException, NotaNoValidaException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setCondicion(EstadoAsignatura.APROBADA);
        asignaturaDto.setNota(9);

        Asignatura asignaturaEncontrada = new Asignatura(new Materia("Lab", 1, 1, new Profesor()), 1L);
        asignaturaEncontrada.setEstado(EstadoAsignatura.CURSADA);
        when(asignaturaService.getAsignaturaPorId(1L)).thenReturn(asignaturaEncontrada);

        Alumno alumnoEncontrado = new Alumno("Juan", "Gomez", 45319502);
        alumnoEncontrado.agregarAsignatura(asignaturaEncontrada);
        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumnoEncontrado);

        Asignatura asignaturaActualizada = alumnoService.actualizarEstadoAsignaturaPorID(1L, 1L, asignaturaDto);

        assertEquals(asignaturaActualizada.getEstado(), EstadoAsignatura.APROBADA);
        verify(alumnoDao, times(1)).saveAlumno(alumnoEncontrado);
    }

    @Test
    public void borrarAlumnoPorId() throws AlumnoEliminadoCorrectamente, AlumnoNotFoundException {
        when(alumnoDao.deleteAlumnoById(1L)).thenThrow(new AlumnoEliminadoCorrectamente());
        assertThrows(AlumnoEliminadoCorrectamente.class, () -> {
            alumnoService.borrarAlumnoPorId(1L);
        });
    }
}