package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.controller.AlumnoController;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class AsignaturaServiceImplTest {

    @InjectMocks
    AsignaturaServiceImpl asignaturaService;

    @Mock
    AsignaturaDao asignaturaDao;

    @Mock
    MateriaService materiaService;

    @Test
    public void getAsignaturaPorIdCorrecto() throws AsignaturaNotFoundException {
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        Asignatura asignaturaExistente = new Asignatura(materia, 1L);
        when(asignaturaDao.getAsignaturaPorId(1L)).thenReturn(asignaturaExistente);
        Asignatura asignaturaEncontrada = asignaturaService.getAsignaturaPorId(1L);
        assertEquals(asignaturaExistente, asignaturaEncontrada);
    }

    @Test
    public void getAsignaturaPorIdIncorrecto() throws AsignaturaNotFoundException {
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        Asignatura asignaturaExistente = new Asignatura(materia, 1L);
        when(asignaturaDao.getAsignaturaPorId(2L)).thenThrow(new AsignaturaNotFoundException("No se encuentra ninguna asignatura con el ID: 2"));
        assertThrows(AsignaturaNotFoundException.class, () -> {
            asignaturaService.getAsignaturaPorId(2L);
        });
    }

    @Test
    public void actualizarAsignatura() throws AsignaturaNotFoundException {
        Materia materia = new Materia("Lab III", 1, 1, new Profesor());
        Asignatura asignaturaExistente = new Asignatura(materia, 1L);
        doNothing().when(asignaturaDao).update(asignaturaExistente);
        asignaturaService.actualizarAsignatura(asignaturaExistente);
        verify(asignaturaDao, times(1)).update(asignaturaExistente);
    }

    @Test
    public void obtenerListaAsignaturas() {
        List<Asignatura> asignaturasCargadas = new ArrayList<>();
        asignaturasCargadas.add(new Asignatura());
        when(asignaturaDao.getListAsignaturas()).thenReturn(asignaturasCargadas);
        List<Asignatura> asignaturasEncontradas = asignaturaService.obtenerListaAsignaturas();
        assertEquals(asignaturasEncontradas, asignaturasCargadas);
    }
}