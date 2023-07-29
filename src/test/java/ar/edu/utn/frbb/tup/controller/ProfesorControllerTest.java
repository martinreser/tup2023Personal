package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public void crearProfesorCorrecta() throws Exception {
        Mockito.when(profesorService.crearProfesor(any(ProfesorDto.class))).thenReturn(new Profesor());
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Martin");
        profesorDto.setApellido("Reser");
        profesorDto.setTitulo("Técnico en Programación");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/profesor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profesorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Profesor profesor = new Profesor();
        profesor.setMateriasDictadas(new Materia("Materia",1,1,profesor));

        Assertions.assertEquals(profesor, mapper.readValue(result.getResponse().getContentAsString(), Profesor.class));
    }

    @Test
    void buscarMateriasDictadas() {
    }

    @Test
    void buscarProfesorPorCadena() {
    }

    @Test
    void buscarProfesorPorId() {
    }

    @Test
    void actualizarProfesorPorId() {
    }

    @Test
    void borrarProfesorPorId() {
    }
}