package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.controller.handler.UtnResponseEntityExceptionHandler;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.YaExistenteException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class MateriaControllerTest {

    @InjectMocks
    MateriaController materiaController;

    @Mock
    MateriaService materiaService;

    MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(materiaController)
                .setControllerAdvice(UtnResponseEntityExceptionHandler.class)
                .build();
    }

    @Test
    public void crearMateriaTest() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(new Materia());
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setAnio(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setNombre("Laboratorio II");
        materiaDto.setProfesorId(345);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(materiaDto))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful())
                .andReturn();

        Assertions.assertEquals(new Materia(), mapper.readValue(result.getResponse().getContentAsString(), Materia.class));
    }

    @Test
    public void crearMateriaBadRequestAnio() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(new Materia());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nombre\" : \"Laboratorio II\",\n" +
                        "    \"anio\" : \"segundo\", \n" +
                        "    \"cuatrimestre\" : 1,\n" +
                        "    \"profesorId\" : 2 \n"+
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void crearMateriaBadRequestCuatrimestre() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(new Materia());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nombre\" : \"Laboratorio II\",\n" +
                        "    \"anio\" : 2, \n" +
                        "    \"cuatrimestre\" : \"segundo\",\n" +
                        "    \"profesorId\" : 2 \n"+
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void crearMateriaBadRequestProfesorId() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenReturn(new Materia());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nombre\" : \"Laboratorio II\",\n" +
                        "    \"anio\" : 2, \n" +
                        "    \"cuatrimestre\" : 2,\n" +
                        "    \"profesorId\" : \"tres\" \n"+
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void crearMateriaNotFoundProfesorId() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenThrow(new ProfesorNotFoundException("No se pudo encontrar un profesor con el ID: 3."));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nombre\" : \"Laboratorio II\",\n" +
                        "    \"anio\" : \"2\", \n" +
                        "    \"cuatrimestre\" : \"2\",\n" +
                        "    \"profesorId\" : \"3\",\n"+
                        "    \"correlatividades\" : [] \n"+
                        "}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"errorMessage\": \"No se pudo encontrar un profesor con el ID: 3.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void crearMateriaCorrelativaIncorrecta() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenThrow(new MateriaNotFoundException("No se encuentra ninguna materia con el ID: 3"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nombre\" : \"Laboratorio II\",\n" +
                        "    \"anio\" : \"2\", \n" +
                        "    \"cuatrimestre\" : \"2\",\n" +
                        "    \"profesorId\" : \"3\",\n"+
                        "    \"correlatividades\" : [3] \n"+
                        "}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"errorMessage\": \"No se encuentra ninguna materia con el ID: 3\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void crearMateriaConNombreYaExistente() throws Exception {
        Mockito.when(materiaService.crearMateria(any(MateriaDto.class))).thenThrow(new YaExistenteException("Ya existe una materia con el nombre: Laboratorio II."));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/materia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nombre\" : \"Laboratorio II\",\n" +
                        "    \"anio\" : \"2\", \n" +
                        "    \"cuatrimestre\" : \"2\",\n" +
                        "    \"profesorId\" : \"3\",\n"+
                        "    \"correlatividades\" : [] \n"+
                        "}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"errorMessage\": \"Ya existe una materia con el nombre: Laboratorio II.\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void buscarMateriaPorIdIncorrecto() throws Exception {
        Mockito.when(materiaService.buscarMateriaPorId(any(Integer.class))).thenThrow(new MateriaNotFoundException("No se encuentra ninguna materia con el ID: 3."));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/materia/3")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"errorMessage\": \"No se encuentra ninguna materia con el ID: 3.\"}"))
                        .andExpect(status().isNotFound())
                        .andReturn();
    }

    @Test
    public void buscarMateriaPorIdCorrecto() throws Exception {
        Materia materia = new Materia();
        materia.setMateriaId(1);
        Mockito.when(materiaService.buscarMateriaPorId(any(Integer.class))).thenReturn(materia);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/materia/1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().json(mapper.writeValueAsString(materia)))
                        .andReturn();
        Assertions.assertEquals(materia, mapper.readValue(result.getResponse().getContentAsString(), Materia.class));

    }


    @Test
    public void buscarMateriasPorCadenaIncorrecta() throws Exception {
        Mockito.when(materiaService.buscarMateriaPorCadena(any(String.class))).thenThrow(new MateriaNotFoundException("No se encuentra ninguna materia que comience con el nombre 'Labi'."));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/materia?nombre=Labi")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorMessage\": \"No se encuentra ninguna materia que comience con el nombre 'Labi'.\"}"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void buscarMateriasPorCadenaCorrecta() throws Exception {
        Materia materia = new Materia();
        materia.setNombre("Laboratorio I");
        List<Materia> listaMaterias = new ArrayList<>();
        listaMaterias.add(materia);
        Mockito.when(materiaService.buscarMateriaPorCadena(any(String.class))).thenReturn(listaMaterias);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/materia?nombre=Lab")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listaMaterias)))
                .andReturn();
        Assertions.assertEquals(listaMaterias.size(), 1);
    }



}
