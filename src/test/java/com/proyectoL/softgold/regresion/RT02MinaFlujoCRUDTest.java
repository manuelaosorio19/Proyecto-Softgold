package com.proyectoL.softgold.regresion;

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
 * RT-02 — Regresión CRUD Minas: Verificar que las operaciones de crear, editar
 * y eliminar no rompen el listado de minas existentes.
 */
@WebMvcTest(MinaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RT-02: Regresión CRUD de Minas — Flujo completo sin romper listado")
class RT02MinaFlujoCRUDTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private MinaDAO minaDAO;
    @MockBean private MapaDAO mapaDAO;
    @MockBean private RiesgoDAO riesgoDAO;

    @Test
    @DisplayName("RT-02a: Listar minas devuelve vista correcta después de operaciones previas")
    void rt02a_listarMinasDespuesDeOperaciones() throws Exception {
        Mina m1 = new Mina(); m1.setCodMina(1L); m1.setNombre("Mina A"); m1.setDepartamento("Antioquia");
        Mina m2 = new Mina(); m2.setCodMina(2L); m2.setNombre("Mina B"); m2.setDepartamento("Bolívar");
        when(minaDAO.findAll()).thenReturn(Arrays.asList(m1, m2));

        mockMvc.perform(get("/admin/minas"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/listarMinas"))
                .andExpect(model().attributeExists("minas"));
    }

    @Test
    @DisplayName("RT-02b: Crear mina con datos válidos no altera flujo del listado")
    void rt02b_crearMinaNoRompeListado() throws Exception {
        when(mapaDAO.findAll()).thenReturn(List.of());
        when(riesgoDAO.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/admin/minas/crear")
                        .param("nombre", "Mina Regresión")
                        .param("departamento", "Chocó"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/minas"));

        verify(minaDAO, times(1)).save(any(Mina.class));
    }

    @Test
    @DisplayName("RT-02c: Editar mina inexistente redirige sin lanzar excepción")
    void rt02c_editarMinaInexistenteRedirigeSinError() throws Exception {
        when(minaDAO.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/minas/editar/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/minas"));
    }

    @Test
    @DisplayName("RT-02d: Eliminar mina inexistente redirige sin lanzar excepción")
    void rt02d_eliminarMinaInexistenteRedirigeSinError() throws Exception {
        when(minaDAO.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/minas/eliminar/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/minas"));
    }

    @Test
    @DisplayName("RT-02e: Búsqueda con parámetro vacío retorna todas las minas")
    void rt02e_busquedaVaciaRetornaTodas() throws Exception {
        Mina m = new Mina(); m.setNombre("Mina General"); m.setDepartamento("Cundinamarca");
        when(minaDAO.findAll()).thenReturn(List.of(m));

        mockMvc.perform(get("/admin/minas/buscar").param("departamento", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/listarMinas"))
                .andExpect(model().attributeExists("minas"));
    }
}
