package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.exception.DatoInvalidoException;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class AlumnoControllerTest {

    @InjectMocks
    AlumnoController alumnoController;

    @Mock
    AlumnoService alumnoService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(alumnoController)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();
    }

    @Test
    public void crearAlumnoTest() throws Exception {
        Mockito.when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenReturn(new Alumno());
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(45319502);
        alumnoDto.setNombre("Martín");
        alumnoDto.setApellido("Reser");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/alumno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new Alumno(), mapper.readValue(result.getResponse().getContentAsString(), Alumno.class));
    }

    @Test
    public void crearAlumnoBadRequestNombreConNumeros() throws Exception {
        Mockito.when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenThrow(new DatoInvalidoException("El nombre no puede contener números."));
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(45319502);
        alumnoDto.setNombre("Martín2");
        alumnoDto.setApellido("Reser");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/alumno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"El nombre no puede contener números.\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void crearAlumnoBadRequestNombreNulo() throws Exception {
        Mockito.when(alumnoService.crearAlumno(any(AlumnoDto.class))).thenThrow(new DatoInvalidoException("El nombre no puede ser nulo ni estar vacío."));
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(45319502);
        alumnoDto.setApellido("Reser");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/alumno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(alumnoDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"El nombre no puede ser nulo ni estar vacío.\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void buscarAlumnoPorIdCorrecto() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setId(1);
        Mockito.when(alumnoService.buscarAlumnoPorId(any(Long.class))).thenReturn(alumno);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(alumno)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(alumno, mapper.readValue(result.getResponse().getContentAsString(), Alumno.class));
    }

    @Test
    public void buscarAlumnoPorIdIncorrecto() throws Exception {
        Mockito.when(alumnoService.buscarAlumnoPorId(any(Long.class))).thenThrow(new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: 1."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ningún alumno con el ID: 1.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void buscarAlumnoPorCadenaCorrecta() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setApellido("Reser");
        List<Alumno> listaAlumnos = new ArrayList<>();
        listaAlumnos.add(alumno);
        Mockito.when(alumnoService.buscarAlumnoPorCadena(any(String.class))).thenReturn(listaAlumnos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno?apellido=Res")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(listaAlumnos)))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(listaAlumnos.size(), 1);
    }

    @Test
    public void buscarAlumnoPorCadenaIncorrecta() throws Exception {
        Mockito.when(alumnoService.buscarAlumnoPorCadena(any(String.class))).thenThrow(new AlumnoNotFoundException("No se encuentra ningún alumno que comience con el apellido 'A'."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno?apellido=A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ningún alumno que comience con el apellido 'A'.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void obtenerAsignaturasAlumnoPorIdCorrecto() throws Exception {
        List<Asignatura> asignaturas = new ArrayList<>();
        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setAsignaturas(asignaturas);
        Mockito.when(alumnoService.obtenerAsignaturasAlumnoPorId(any(Long.class))).thenReturn(asignaturas);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(asignaturas)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void obtenerAsignaturasAlumnoPorIdIncorrecto() throws Exception {
        Mockito.when(alumnoService.obtenerAsignaturasAlumnoPorId(any(Long.class))).thenThrow(new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: 1."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ningún alumno con el ID: 1.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /* PROBLEMA
    @Test
    public void obtenerAsignaturaAlumnoPorIdCorrecto() throws Exception {
        Materia materia = new Materia();
        materia.setNombre("Laboratorio III");
        Asignatura asignatura = new Asignatura();
        asignatura.setMateria(materia);
        asignatura.setAsignaturaId(1L);
        List<Asignatura> asignaturas = new ArrayList<>();
        asignaturas.add(asignatura);
        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setAsignaturas(asignaturas);
        Mockito.when(alumnoService.obtenerAsignaturaAlumnoPorId(any(Long.class), any(Long.class))).thenReturn(asignatura);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1/asignaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(asignatura)))
                .andExpect(status().isOk())
                .andReturn();
    }*/

    @Test
    public void obtenerAsignaturaAlumnoPorIdAsignaturaIncorrecto() throws Exception {
        Mockito.when(alumnoService.obtenerAsignaturaAlumnoPorId(any(Long.class), any(Long.class))).thenThrow(new AsignaturaNotFoundException(
                "El alumno Martin Reser (ID: 1), no tiene ninguna asignatura con el ID: 1."
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/alumno/1/asignaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"El alumno Martin Reser (ID: 1), no tiene ninguna asignatura con el ID: 1.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void actualizarDatosAlumnoPorIdCorrecto() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setId(1);
        alumno.setNombre("Julian");
        alumno.setApellido("Reser");
        alumno.setDni(45319502);

        Alumno alumnoActualizado = new Alumno();
        alumnoActualizado.setNombre("Martin");
        alumnoActualizado.setApellido("Reser");
        alumnoActualizado.setDni(45319502);

        Mockito.when(alumnoService.actualizarAlumnoPorId(any(Long.class), any(AlumnoDto.class))).thenReturn(alumnoActualizado);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"nombre\" : \"Martin\",\n" +
                                "    \"apellido\" : \"Reser\",\n" +
                                "    \"dni\" : \"45319502\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(alumnoActualizado)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Martin", mapper.readValue(result.getResponse().getContentAsString(), Alumno.class).getNombre());
    }


    @Test
    public void actualizarDatosAlumnoPorIdIncorrecto() throws Exception {
        Mockito.when(alumnoService.actualizarAlumnoPorId(any(Long.class), any(AlumnoDto.class))).thenThrow(new AlumnoNotFoundException(
                "No se encuentra ningún alumno con el ID: 1."
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ningún alumno con el ID: 1.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void actualizarEstadoAsignaturaAlumnoPorIdIncorrecto() throws Exception {
        Mockito.when(alumnoService.actualizarEstadoAsignaturaPorID(any(Long.class), any(Long.class),
                any(AsignaturaDto.class))).thenThrow(new AsignaturaNotFoundException("No se encuentra ninguna asignatura con el ID: 1"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/alumno/1/asignaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ninguna asignatura con el ID: 1\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void borrarAlumnoPorIdCorrecto() throws Exception {
        Mockito.when(alumnoService.borrarAlumnoPorId(any(Long.class))).thenThrow(new AlumnoEliminadoCorrectamente());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/alumno/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void borrarAlumnoPorIdIncorrecto() throws Exception {
        Mockito.when(alumnoService.borrarAlumnoPorId(any(Long.class))).thenThrow(new AlumnoNotFoundException("No se encuentra ningún alumno con el ID: 2."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/alumno/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ningún alumno con el ID: 2.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}