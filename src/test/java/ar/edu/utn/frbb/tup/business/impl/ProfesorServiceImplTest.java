package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;
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
public class ProfesorServiceImplTest {

    @InjectMocks
    ProfesorServiceImpl profesorService;

    @Mock
    ProfesorDao profesorDao;

    @Mock
    MateriaDao materiaDao;

    @Test
    public void crearProfesor() throws YaExistenteException, DatoInvalidoException {
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Martin");
        profesorDto.setApellido("Reser");
        profesorDto.setTitulo("Tecnico");

        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        when(profesorDao.save(profesor)).thenReturn(profesor);

        Profesor profesorCreado = profesorService.crearProfesor(profesorDto);

        assertEquals(profesorCreado.getNombre(), "Martin");
        assertEquals(profesorCreado.getApellido(), "Reser");
    }

    @Test
    public void crearProfesorConErroresEnDTO() throws YaExistenteException, DatoInvalidoException {
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Martin11");
        profesorDto.setApellido("Reser");
        profesorDto.setTitulo("Tecnico");

        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        when(profesorDao.save(profesor)).thenReturn(profesor);

        assertThrows(DatoInvalidoException.class, () -> {
            profesorService.crearProfesor(profesorDto);
        });
    }

    @Test
    public void buscarProfesorPorCadenaCorrecta() throws ProfesorNotFoundException {
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        profesores.add(profesor);
        when(profesorDao.findProfesorByChain("Res")).thenReturn(profesores);
        List<Profesor> profesoresEncontrados = profesorService.buscarProfesorPorCadena("Res");
        assertEquals(profesores, profesoresEncontrados);
        verify(profesorDao, times(1)).findProfesorByChain("Res");
    }

    @Test
    public void buscarProfesorPorCadenaIncorrecta() throws ProfesorNotFoundException {
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        profesores.add(profesor);
        when(profesorDao.findProfesorByChain("Gom")).thenThrow(new ProfesorNotFoundException("No se encuentra ningún profesor que comience con el apellido 'sda'."));
        assertThrows(ProfesorNotFoundException.class, () -> {
            profesorService.buscarProfesorPorCadena("Gom");
        });
    }

    @Test
    public void buscarProfesorPorIdCorrecto() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        when(profesorDao.findProfesorById(1)).thenReturn(profesor);

        Profesor profesorEncontrado = profesorService.buscarProfesorPorId(1L);
        verify(profesorDao, times(1)).findProfesorById(1);
    }

    @Test
    public void buscarProfesorPorIdIncorrecto() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        when(profesorDao.findProfesorById(1)).thenThrow(new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: 1."));
        assertThrows(ProfesorNotFoundException.class, () -> {
            profesorService.buscarProfesorPorId(1L);
        });
    }

    @Test
    public void buscarMateriasDictadasPorIdCorrecto() throws ProfesorNotFoundException {
        List<Materia> materias = new ArrayList<>();
        materias.add(new Materia("Lab", 1, 1, new Profesor()));
        when(profesorDao.getMateriasDictadas(1L)).thenReturn(materias);
        List<Materia> materiasEncontradas = profesorService.buscarMateriasDictadas(1L);
        assertEquals(materias.get(0), materiasEncontradas.get(0));
    }

    @Test
    public void buscarMateriasDictadasPorIdIncorrecto() throws ProfesorNotFoundException {
        List<Materia> materias = new ArrayList<>();
        materias.add(new Materia("Lab", 1, 1, new Profesor()));
        when(profesorDao.getMateriasDictadas(1L)).thenThrow(new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: 1."));
        assertThrows(ProfesorNotFoundException.class, () -> {
            profesorService.buscarMateriasDictadas(1L);
        });
    }

    @Test
    public void actualizarProfesor() throws ProfesorNotFoundException {
        Profesor profesor = new Profesor("Martin", "Reser", "Tecnico");
        doNothing().when(profesorDao).update(profesor.getId(), profesor);
        profesorService.actualizarProfesor(profesor);
        verify(profesorDao, times(1)).update(profesor.getId(), profesor);
    }

    @Test
    public void actualizarProfesorPorId() throws ProfesorNotFoundException, DatoInvalidoException {
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Juan");
        profesorDto.setApellido("Perez");
        profesorDto.setTitulo("Dr.");

        Profesor profesorEncontrado = new Profesor("Antonio", "Gonzalez", "Lic.");
        when(profesorDao.findProfesorById(1L)).thenReturn(profesorEncontrado);

        Profesor profesorActualizado = profesorService.actualizarProfesorPorId(1L, profesorDto);

        assertEquals(profesorDto.getNombre(), profesorActualizado.getNombre());
        assertEquals(profesorDto.getApellido(), profesorActualizado.getApellido());
        assertEquals(profesorDto.getTitulo(), profesorActualizado.getTitulo());
        verify(profesorDao, times(1)).update(1L, profesorActualizado);
    }

    @Test
    public void borrarProfesorPorId() throws ProfesorNotFoundException, ProfesorEliminadoCorrectamente {
        List<Materia> materiasDictadas = new ArrayList<>();
        materiasDictadas.add(new Materia("Materia 1", 1, 1, new Profesor()));
        when(profesorDao.getMateriasDictadas(1L)).thenReturn(materiasDictadas);

       profesorService.borrarProfesorPorId(1L);

       for (Materia materia : materiasDictadas) {
            verify(materiaDao, times(1)).deleteMateriaById(materia.getMateriaId());
        }

       verify(profesorDao, times(1)).deleteProfesorById(1L);
    }
}