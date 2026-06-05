package com.proyectoL.softgold.casos;

import com.proyectoL.softgold.controller.MinaController;
import com.proyectoL.softgold.model.Mina;
import com.proyectoL.softgold.repository.MapaDAO;
import com.proyectoL.softgold.repository.MinaDAO;
import com.proyectoL.softgold.repository.RiesgoDAO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CP-03 a CP-07 — Módulo de Gestión de Minas
 */
@WebMvcTest(MinaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Gestión de Minas — CP-03 a CP-07")
class CP03CP07MinaTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private MinaDAO minaDAO;
    @MockBean private MapaDAO mapaDAO;
    @MockBean private RiesgoDAO riesgoDAO;

    @Test
    @DisplayName("CP-03: Crear mina con datos válidos redirige a listado")
    void cp03_crearMinaValida() throws Exception {
        when(mapaDAO.findAll()).thenReturn(List.of());
        when(riesgoDAO.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/admin/minas/crear")
                        .param("nombre", "Mina El Dorado")
                        .param("departamento", "Antioquia"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/minas"));

        verify(minaDAO, times(1)).save(any(Mina.class));
    }

    @Test
    @DisplayName("CP-04: Crear mina con nombre vacío regresa al formulario con error")
    void cp04_crearMinaNombreVacio() throws Exception {
        when(mapaDAO.findAll()).thenReturn(List.of());
        when(riesgoDAO.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/admin/minas/crear")
                        .param("nombre", "")
                        .param("departamento", "Antioquia"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/crearMina"));

        verify(minaDAO, never()).save(any());
    }

    @Test
    @DisplayName("CP-05: Editar mina existente actualiza datos correctamente")
    void cp05_editarMinaExistente() throws Exception {
        Mina mina = new Mina();
        mina.setCodMina(1L);
        mina.setNombre("Mina Original");
        mina.setDepartamento("Antioquia");

        when(minaDAO.findById(1L)).thenReturn(Optional.of(mina));
        when(mapaDAO.findAll()).thenReturn(List.of());
        when(riesgoDAO.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/admin/minas/editar/1")
                        .param("nombre", "Mina Actualizada")
                        .param("departamento", "Córdoba"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/minas"));

        verify(minaDAO, atLeastOnce()).save(any(Mina.class));
    }

    @Test
    @DisplayName("CP-06: Eliminar mina existente redirige al listado")
    void cp06_eliminarMinaExistente() throws Exception {
        Mina mina = new Mina();
        mina.setCodMina(1L);
        mina.setNombre("Mina Temporal");
        mina.setDepartamento("Caldas");

        when(minaDAO.findById(1L)).thenReturn(Optional.of(mina));

        mockMvc.perform(get("/admin/minas/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/minas"));

        verify(minaDAO, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("CP-07: Buscar minas por departamento retorna lista filtrada")
    void cp07_buscarMinasPorDepartamento() throws Exception {
        Mina mina = new Mina();
        mina.setNombre("Mina Norte");
        mina.setDepartamento("Antioquia");

        when(minaDAO.findByDepartamentoContainingIgnoreCase("Antioquia"))
                .thenReturn(Arrays.asList(mina));

        mockMvc.perform(get("/admin/minas/buscar")
                        .param("departamento", "Antioquia"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/listarMinas"))
                .andExpect(model().attributeExists("minas"));
    }
}
