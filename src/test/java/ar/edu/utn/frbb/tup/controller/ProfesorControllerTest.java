package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorEliminadoCorrectamente;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
public class ProfesorControllerTest {

    @InjectMocks
    ProfesorController profesorController;

    @Mock
    ProfesorService profesorService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(profesorController)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();
    }

    @Test
    public void crearProfesorTest() throws Exception {
        Mockito.when(profesorService.crearProfesor(any(ProfesorDto.class))).thenReturn(new Profesor());
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Martin");
        profesorDto.setApellido("Reser");
        profesorDto.setTitulo("Técnico en Programación");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/profesor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profesorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(new Profesor(), mapper.readValue(result.getResponse().getContentAsString(), Profesor.class));
    }

    @Test
    public void buscarProfesorPorCadenaCorrecta() throws Exception {
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor("Martin", "Reser", "Técnico");
        profesores.add(profesor);

        Mockito.when(profesorService.buscarProfesorPorCadena(any(String.class))).thenReturn(profesores);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor?apellido=Res")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(profesores)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(profesores.size(), 1);
    }

    @Test
    public void buscarProfesorPorCadenaIncorrecta() throws Exception {
        List<Profesor> profesores = new ArrayList<>();
        Profesor profesor = new Profesor("Martin", "Reser", "Técnico");
        profesores.add(profesor);

        Mockito.when(profesorService.buscarProfesorPorCadena(any(String.class))).thenThrow(new ProfesorNotFoundException("" +
                "No se encuentra ningún profesor que comience con el apellido 'Gom'."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor?apellido=Gom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ningún profesor que comience con el apellido 'Gom'.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void buscarProfesorPorIdCorrecto() throws Exception {
        Profesor profesor = new Profesor();
        profesor.setId(1);

        Mockito.when(profesorService.buscarProfesorPorId(any(Long.class))).thenReturn(profesor);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(profesor)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(profesor, mapper.readValue(result.getResponse().getContentAsString(), Profesor.class));
    }

    @Test
    public void buscarProfesorPorIdIncorrecto() throws Exception {
        Profesor profesor = new Profesor("Martin", "Reser", "Técnico");
        profesor.setId(1);

        Mockito.when(profesorService.buscarProfesorPorId(any(Long.class))).thenThrow(new ProfesorNotFoundException("" +
                "No se pudo encontrar un profesor con el ID: 1."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se pudo encontrar un profesor con el ID: 1.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void buscarMateriasDictadasProfesorPorIdCorrecto() throws Exception {
        Profesor profesor = new Profesor();
        profesor.setId(1);
        List<Materia> materias = new ArrayList<>();
        materias.add(new Materia("Laboratorio", 1, 1, profesor));
        materias.add(new Materia("Progamacion", 1, 1, profesor));
        materias.add(new Materia("Algebra", 1, 1, profesor));


        Mockito.when(profesorService.buscarMateriasDictadas(any(Long.class))).thenReturn(materias);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor/1/materias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(materias)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void buscarMateriasDictadasProfesorPorIdIncorrecto() throws Exception {
       Mockito.when(profesorService.buscarMateriasDictadas(any(Long.class))).thenThrow(new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: 1."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/profesor/1/materias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se pudo encontrar un profesor con el ID: 1.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    public void actualizarDatosProfesorPorIdCorrecto() throws Exception {
        Profesor profesor = new Profesor("Martin", "Resera", "Tecnico");
        profesor.setId(1);
        Profesor profesorActualizado = profesor;
        profesorActualizado.setApellido("Reser");

        Mockito.when(profesorService.actualizarProfesorPorId(any(Long.class), any(ProfesorDto.class))).thenReturn(profesorActualizado);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/profesor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"nombre\" : \"Martin\",\n" +
                                "    \"apellido\" : \"Reser\",\n" +
                                "    \"titulo\" : \"Tecnico\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(profesorActualizado)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Reser", mapper.readValue(result.getResponse().getContentAsString(), Profesor.class).getApellido());
    }


    @Test
    public void actualizarDatosProfesorPorIdIncorrecto() throws Exception {
        Mockito.when(profesorService.actualizarProfesorPorId(any(Long.class), any(ProfesorDto.class))).thenThrow(new ProfesorNotFoundException(
                "No se pudo encontrar un profesor con el ID: 2."
        ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/profesor/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se pudo encontrar un profesor con el ID: 2.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void borrarProfesorPorIdCorrecto() throws Exception {
        Mockito.when(profesorService.borrarProfesorPorId(any(Long.class))).thenThrow(new ProfesorEliminadoCorrectamente());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void borrarAlumnoPorIdIncorrecto() throws Exception {
        Mockito.when(profesorService.borrarProfesorPorId(any(Long.class))).thenThrow(new ProfesorNotFoundException(
                "No se pudo encontrar un profesor con el ID: 2."));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/profesor/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se pudo encontrar un profesor con el ID: 2.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}