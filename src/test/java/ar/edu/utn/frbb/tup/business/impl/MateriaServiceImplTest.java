package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
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
public class MateriaServiceImplTest {

    @InjectMocks
    MateriaServiceImpl materiaService;

    @Mock
    MateriaDao materiaDao;

    @Mock
    ProfesorService profesorService;

    @Test
    public void crearMateria() throws ProfesorNotFoundException, MateriaNotFoundException, YaExistenteException {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("Lab III");
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(1);
        materiaDto.setProfesorId(1L);

        Profesor profesor = new Profesor();

        when(profesorService.buscarProfesorPorId(1L)).thenReturn(profesor);
        doNothing().when(profesorService).actualizarProfesor(profesor);

        Materia materiaCreada = materiaService.crearMateria(materiaDto);

        assertEquals(materiaCreada.getProfesor(), profesor);
        assertEquals(materiaCreada.getNombre(), "Lab III");
    }

    @Test
    public void buscarMateriaPorIdCorrecto() throws MateriaNotFoundException {
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        materia.setMateriaId(1);
        when(materiaDao.findMateriaById(1)).thenReturn(materia);
        Materia materiaEncontrada = materiaService.buscarMateriaPorId(1);
        assertEquals(materiaEncontrada, materia);
        verify(materiaDao, times(1)).findMateriaById(1);
    }

    @Test
    public void buscarMateriaPorIdIncorrecto() throws MateriaNotFoundException {
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        materia.setMateriaId(1);
        when(materiaDao.findMateriaById(2)).thenThrow(new MateriaNotFoundException("No se encuentra ninguna materia con el ID: 806"));
        assertThrows(MateriaNotFoundException.class, () -> {
            materiaService.buscarMateriaPorId(2);
        });

    }

    @Test
    public void buscarMateriaPorCadenaCorrecta() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>();
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        materias.add(materia);
        when(materiaDao.findMateriaByChain("Lab")).thenReturn(materias);
        List<Materia> materiasEncontradas = materiaService.buscarMateriaPorCadena("Lab");
        assertEquals(materiasEncontradas, materias);
        verify(materiaDao, times(1)).findMateriaByChain("Lab");
    }

    @Test
    public void buscarMateriaPorCadenaIncorrecta() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>();
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        materias.add(materia);
        when(materiaDao.findMateriaByChain("Lub")).thenThrow(new MateriaNotFoundException("No se encuentra ninguna materia que comience con el nombre 'Lub'."));
        assertThrows(MateriaNotFoundException.class, () -> {
            materiaService.buscarMateriaPorCadena("Lub");
        });
    }

    @Test
    public void getAllMaterias() {
        List<Materia> materias = new ArrayList<>();
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        materias.add(materia);
        when(materiaDao.getAllMaterias()).thenReturn(materias);
        List<Materia> materiasEncontradas = materiaService.getAllMaterias();
        assertEquals(materiasEncontradas, materias);
        verify(materiaDao, times(1)).getAllMaterias();

    }
}